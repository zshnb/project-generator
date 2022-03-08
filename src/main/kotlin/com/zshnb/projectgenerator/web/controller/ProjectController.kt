package com.zshnb.projectgenerator.web.controller

import com.squareup.moshi.Moshi
import com.zshnb.projectgenerator.generator.entity.Project
import com.zshnb.projectgenerator.generator.entity.web.WebProjectType.SBMP
import com.zshnb.projectgenerator.generator.generator.c.CProjectGenerator
import com.zshnb.projectgenerator.generator.generator.web.ssm.LayuiSSMBackendGenerator
import com.zshnb.projectgenerator.generator.generator.swing.SwingProjectGenerator
import com.zshnb.projectgenerator.generator.generator.web.sbmp.LayuiSBMPBackendGenerator
import com.zshnb.projectgenerator.generator.generator.web.sbmp.SBMPBackendGenerator
import com.zshnb.projectgenerator.generator.generator.web.ssm.SSMBackendGenerator
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
    private val layuiSBMPProjectGenerator: LayuiSBMPBackendGenerator,
    private val layuiSSMProjectGenerator: LayuiSSMBackendGenerator,
    private val ssmBackendGenerator: SSMBackendGenerator,
    private val sbmpBackendGenerator: SBMPBackendGenerator,
    private val cProjectGenerator: CProjectGenerator,
    private val swingProjectGenerator: SwingProjectGenerator,
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
                if (project.webProject.type == SBMP) {
                    sbmpBackendGenerator.generateProject(project)
                    layuiSBMPProjectGenerator.generateProject(project)
                } else {
                    ssmBackendGenerator.generateProject(project)
                    layuiSSMProjectGenerator.generateProject(project)
                }
                zipFileWriter.createZipFile(fileName, filePath)
                File(fileName) to project.webProject.config.artifactId
            }
            project.cProject != null -> {
                val fileName = "${project.cProject.name}.c"
                val file = File(projectConfig.projectDir, fileName)
                FileUtils.deleteQuietly(file)
                cProjectGenerator.generateProject(project)
                file to project.cProject.name
            }
            project.swingProject != null -> {
                val filePath = "${projectConfig.projectDir}/${project.swingProject.config.artifactId}"
                FileUtils.deleteDirectory(File(filePath))
                val fileName = "$filePath.zip"
                swingProjectGenerator.generateProject(project)
                zipFileWriter.createZipFile(fileName, filePath)
                File(fileName) to project.swingProject.config.artifactId
            }
            else -> throw RuntimeException("unknown project type")
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