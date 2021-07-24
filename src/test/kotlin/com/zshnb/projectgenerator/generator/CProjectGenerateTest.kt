package com.zshnb.projectgenerator.generator

import com.squareup.moshi.Moshi
import com.zshnb.projectgenerator.generator.entity.Project
import com.zshnb.projectgenerator.generator.entity.c.CProject
import com.zshnb.projectgenerator.generator.entity.web.WebProject
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

    @Autowired
    private lateinit var moshi: Moshi

    @BeforeEach
    fun cleanProject() {
        FileUtils.deleteQuietly(File("C:/Users/zsh/Workbench/外包/demo.c"))
    }

    @Test
    fun generate() {
        val resource = ClassPathResource("c-project.json")
        val json = FileUtils.readLines(resource.file, StandardCharsets.UTF_8).joinToString(separator = "")
        val adapter = moshi.adapter(CProject::class.java)
        generator.generateProject(project = Project(cProject = adapter.fromJson(json)))
    }
}