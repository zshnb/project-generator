package com.zshnb.projectgenerator.web.controller

import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
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
import org.springframework.web.bind.annotation.*
import java.io.*

@RestController
@RequestMapping("/api/project")
class ProjectController(
    private val layuiGenerator: LayuiGenerator,
    private val restfulBackendGenerator: RestfulBackendGenerator,
    private val zipFileWriter: ZipFileWriter,
    private val projectConfig: ProjectConfig
) {
    @PostMapping("/generate")
    fun generate(@RequestBody json: String): ResponseEntity<InputStreamResource> {
        val project = Gson().fromJson<Project>(json, TypeToken.get(Project::class.java).type)
        val fileName = "${project.config.artifactId}.zip"
        layuiGenerator.generateProject(json)
        zipFileWriter.createZipFile(fileName, project.config.artifactId)
        FileUtils.deleteDirectory(File(project.config.artifactId))
        val file = File(projectConfig.tempDir, fileName)
        val resource = InputStreamResource(FileInputStream(file))
        val headers = HttpHeaders()
        headers.add("Content-Disposition", "attachment;fileName=${file.name}")
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