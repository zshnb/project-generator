package com.zshnb.projectgenerator.generator

import com.zshnb.projectgenerator.generator.generator.LayuiGenerator
import com.zshnb.projectgenerator.generator.io.ZipFileWriter
import org.apache.commons.io.FileUtils
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.core.io.ClassPathResource
import java.io.File
import java.nio.charset.StandardCharsets

@SpringBootTest
class GeneratorTest {
    @Autowired
    private lateinit var layuiGenerator: LayuiGenerator

    @Autowired
    private lateinit var zipFileWriter: ZipFileWriter

    @Test
    fun generate() {
        val resource = ClassPathResource("project.json")
        val json = FileUtils.readLines(resource.file, StandardCharsets.UTF_8).joinToString(separator = "")
        layuiGenerator.generateProject(json)
        zipFileWriter.createZipFile("demo.zip", "demo")
    }
}