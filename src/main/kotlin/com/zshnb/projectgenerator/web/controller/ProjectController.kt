package com.zshnb.projectgenerator.web.controller

import com.google.gson.Gson
import com.zshnb.projectgenerator.generator.constant.PathConstant
import com.zshnb.projectgenerator.generator.entity.Project
import com.zshnb.projectgenerator.generator.generator.LayuiGenerator
import com.zshnb.projectgenerator.generator.generator.RestfulBackendGenerator
import com.zshnb.projectgenerator.generator.io.ZipFileWriter
import org.apache.commons.io.FileUtils
import org.springframework.core.io.InputStreamResource
import org.springframework.http.*
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RestController
import java.io.*

@RestController
class ProjectController(
    private val layuiGenerator: LayuiGenerator,
    private val restfulBackendGenerator: RestfulBackendGenerator,
    private val zipFileWriter: ZipFileWriter
) {
    @PostMapping("/api/project/generate")
    fun generate(@RequestBody project: Project): ResponseEntity<InputStreamResource> {
        layuiGenerator.generateProject(Gson().toJson(project))
        zipFileWriter.createZipFile("${project.config.artifactId}.zip")
        val file = File("${PathConstant.projectDirPath}/${project.config.artifactId}.zip")
        val resource = InputStreamResource(FileInputStream(file))
        val headers = HttpHeaders()
        headers.add("Content-Disposition", String.format("attachment;fileName=%s", file.name))
        headers.add("Cache-Control", "no-cache,no-store,must-revalidate")
        headers.add("Pragma", "no-cache")
        headers.add("Expires", "0")
        FileUtils.deleteQuietly(file)

        return ResponseEntity.ok()
            .headers(headers)
            .contentLength(file.length())
            .contentType(MediaType.parseMediaType("application/x-zip-compressed"))
            .body(resource)
    }
}