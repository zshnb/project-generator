package com.zshnb.projectgenerator.web.controller

import com.google.gson.Gson
import com.zshnb.projectgenerator.generator.constant.PathConstant
import com.zshnb.projectgenerator.generator.entity.Project
import com.zshnb.projectgenerator.generator.generator.LayuiGenerator
import com.zshnb.projectgenerator.generator.generator.RestfulBackendGenerator
import com.zshnb.projectgenerator.generator.io.ZipFileWriter
import com.zshnb.projectgenerator.web.config.ProjectConfig
import org.apache.commons.io.FileUtils
import org.springframework.core.io.InputStreamResource
import org.springframework.http.*
import org.springframework.util.ResourceUtils
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RestController
import java.io.*

@RestController
class ProjectController(
    private val layuiGenerator: LayuiGenerator,
    private val restfulBackendGenerator: RestfulBackendGenerator,
    private val zipFileWriter: ZipFileWriter,
    private val projectConfig: ProjectConfig
) {
    @PostMapping("/api/project/generate")
    fun generate(@RequestBody project: Project): ResponseEntity<InputStreamResource> {
        val fileName = "${project.config.artifactId}.zip"
        layuiGenerator.generateProject(Gson().toJson(project))
        zipFileWriter.createZipFile(fileName)
        val file = File(projectConfig.tempDir, fileName)
        val resource = InputStreamResource(FileInputStream(file))
        val headers = HttpHeaders()
        headers.add("Content-Disposition", String.format("attachment;fileName=%s", file.name))
        headers.add("Cache-Control", "no-cache,no-store,must-revalidate")
        headers.add("Pragma", "no-cache")
        headers.add("Expires", "0")

        return ResponseEntity.ok()
            .headers(headers)
            .contentLength(file.length())
            .contentType(MediaType.parseMediaType("application/x-zip-compressed"))
            .body(resource)
    }
}