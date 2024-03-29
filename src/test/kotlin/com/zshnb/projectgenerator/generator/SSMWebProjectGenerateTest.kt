package com.zshnb.projectgenerator.generator

import com.squareup.moshi.Moshi
import com.zshnb.projectgenerator.generator.entity.Project
import com.zshnb.projectgenerator.generator.entity.web.WebProject
import com.zshnb.projectgenerator.generator.generator.web.ssm.LayuiSSMBackendGenerator
import com.zshnb.projectgenerator.generator.generator.web.ssm.SSMBackendGenerator
import org.apache.commons.io.FileUtils
import org.junit.jupiter.api.*
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.core.io.ClassPathResource
import java.io.File
import java.nio.charset.StandardCharsets

@SpringBootTest
class SSMWebProjectGenerateTest {
    @Autowired
    private lateinit var ssmBackendGenerator: SSMBackendGenerator

    @Autowired
    private lateinit var layuiSSMProjectGenerator: LayuiSSMBackendGenerator

    @Autowired
    private lateinit var moshi: Moshi

    @BeforeEach
    fun cleanProject() {
        FileUtils.deleteDirectory(File("C:/Users/zsh/Workbench/外包/demo"))
    }
    @Test
    fun generateMysql() {
        val resource = ClassPathResource("ssm/mysql-project.json")
        val json = FileUtils.readLines(resource.file, StandardCharsets.UTF_8).joinToString(separator = "")
        val adapter = moshi.adapter(WebProject::class.java)
        val project = adapter.fromJson(json)!!
        ssmBackendGenerator.generateProject(Project(webProject = project))
        layuiSSMProjectGenerator.generateProject(Project(webProject = project))
    }
}