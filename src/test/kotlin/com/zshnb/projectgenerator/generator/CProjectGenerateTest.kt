package com.zshnb.projectgenerator.generator

import com.zshnb.projectgenerator.generator.generator.c.CProjectGenerator
import org.apache.commons.io.FileUtils
import org.junit.jupiter.api.*
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.core.io.ClassPathResource
import java.io.File
import java.nio.charset.StandardCharsets

@SpringBootTest
class CProjectGenerateTest {
    @Autowired
    private lateinit var generator: CProjectGenerator

    @BeforeEach
    fun cleanProject() {
        FileUtils.deleteQuietly(File("C:/Users/zsh/Workbench/外包/demo.c"))
    }

    @Test
    fun generate() {
        val resource = ClassPathResource("c-project.json")
        val json = FileUtils.readLines(resource.file, StandardCharsets.UTF_8).joinToString(separator = "")
        generator.generateProject(json)
    }
}