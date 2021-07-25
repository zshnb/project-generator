package com.zshnb.projectgenerator.web.controller

import com.squareup.moshi.Moshi
import com.zshnb.projectgenerator.generator.entity.Project
import com.zshnb.projectgenerator.generator.generator.c.CProjectGenerator
import com.zshnb.projectgenerator.generator.generator.ssmp.*
import com.zshnb.projectgenerator.generator.io.ZipFileWriter
import com.zshnb.projectgenerator.web.config.ProjectConfig
import com.zshnb.projectgenerator.web.request.AddOrUpdateProjectRequest
import com.zshnb.projectgenerator.web.serviceImpl.ProjectServiceImpl
import org.apache.commons.io.FileUtils
import org.springframework.core.io.InputStreamResource
import org.springframework.http.*
import org.springframework.web.bind.annotation.*
import java.io.*

@RestController
@RequestMapping("/api/project")
class ProjectController(
    private val layuiGenerator: LayuiSSMPProjectGenerator,
    private val cProjectGenerator: CProjectGenerator,
    private val zipFileWriter: ZipFileWriter,
    private val projectConfig: ProjectConfig,
    private val moshi: Moshi,
    private val projectServiceImpl: ProjectServiceImpl
) {
    @PostMapping("/generate")
    fun generate(@RequestBody json: String): ResponseEntity<InputStreamResource> {
        val adapter = moshi.adapter(Project::class.java)
        val project = adapter.fromJson(json)!!
        val (file, name) = when {
            project.webProject != null -> {
                val filePath = "${projectConfig.projectDir}/${project.webProject.config.artifactId}"
                FileUtils.deleteDirectory(File(filePath))
                val fileName = "$filePath.zip"
                layuiGenerator.generateProject(project)
                zipFileWriter.createZipFile(fileName, filePath)
                File(fileName) to project.webProject.config.artifactId
            }
            project.cProject != null -> {
                val fileName = "${project.cProject.name}.c"
                val file = File(projectConfig.projectDir, fileName)
                FileUtils.deleteQuietly(file)
                cProjectGenerator.generateProject(project)
                file to fileName
            }
            else -> File("") to ""
        }

        val resource = InputStreamResource(FileInputStream(file))
        val headers = HttpHeaders()
        headers.add("Content-Disposition", "attachment;fileName=${file.name}")
        headers.add("Cache-Control", "no-cache,no-store,must-revalidate")
        headers.add("Pragma", "no-cache")
        headers.add("Expires", "0")
        projectServiceImpl.addOrUpdateProject(AddOrUpdateProjectRequest(name, json))
        return ResponseEntity.ok()
            .headers(headers)
            .contentLength(file.length())
            .contentType(MediaType.parseMediaType("application/x-zip-compressed"))
            .body(resource)
    }
}