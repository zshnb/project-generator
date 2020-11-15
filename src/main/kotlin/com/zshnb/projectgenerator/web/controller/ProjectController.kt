package com.zshnb.web.controller

import com.google.gson.Gson
import com.zshnb.codegenerator.entity.Project
import com.zshnb.codegenerator.generator.LayuiGenerator
import com.zshnb.codegenerator.generator.RestfulBackendGenerator
import org.springframework.core.io.InputStreamResource
import org.springframework.http.*
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RestController
import java.io.*

@RestController
class ProjectController(
    private val layuiGenerator: LayuiGenerator,
    private val restfulBackendGenerator: RestfulBackendGenerator
) {
    @PostMapping("/api/project/generate")
    fun generate(@RequestBody project: Project): ResponseEntity<InputStreamResource> {
        layuiGenerator.generateProject(Gson().toJson(project))
        val file = File("./generator/test.zip")
        val resource = InputStreamResource(FileInputStream(file))
        val headers = HttpHeaders()
        headers.add ("Content-Disposition",String.format("attachment;fileName=%s", file.name))
        headers.add ("Cache-Control","no-cache,no-store,must-revalidate")
        headers.add ("Pragma","no-cache")
        headers.add ("Expires","0")

        return ResponseEntity.ok()
            .headers ( headers )
            .contentLength ( file.length ())
            .contentType(MediaType.parseMediaType ("application/x-zip-compressed"))
            .body(resource)
    }
}