package com.zshnb.projectgenerator.generator

import com.google.gson.Gson
import com.zshnb.projectgenerator.generator.generator.LayuiGenerator
import org.apache.commons.io.FileUtils
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.core.io.ClassPathResource
import org.springframework.util.ResourceUtils
import java.io.File
import java.net.URL
import java.nio.charset.StandardCharsets

@SpringBootTest
class GeneratorTest {
    @Autowired
    private lateinit var layuiGenerator: LayuiGenerator

    @Test
    fun generate() {
        File("temp").mkdir()
        val resource = ClassPathResource("project.json")
        val json = FileUtils.readLines(resource.file, StandardCharsets.UTF_8).joinToString(separator = "")
        layuiGenerator.generateProject(json)
    }
}