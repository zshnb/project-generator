package com.zshnb.projectgenerator.generator.generator.web.sbmp

import com.zshnb.projectgenerator.generator.config.PathConfig
import com.zshnb.projectgenerator.generator.constant.*
import com.zshnb.projectgenerator.generator.entity.Project
import com.zshnb.projectgenerator.generator.entity.web.*
import com.zshnb.projectgenerator.generator.extension.*
import com.zshnb.projectgenerator.generator.generator.BaseGenerator
import com.zshnb.projectgenerator.generator.generator.web.BaseWebProjectGenerator
import com.zshnb.projectgenerator.generator.parser.web.BackendParser
import com.zshnb.projectgenerator.generator.util.*
import com.zshnb.projectgenerator.web.config.ProjectConfig
import freemarker.template.Configuration
import org.springframework.stereotype.Component
import java.io.*

@Component
open class SBMPBackendGenerator(backendParser: BackendParser,
                                private val ioUtil: IOUtil,
                                private val projectConfig: ProjectConfig,
                                private val pathConfig: PathConfig,
                                private val configuration: Configuration) :
    BaseGenerator, BaseWebProjectGenerator(backendParser, ioUtil, pathConfig, configuration){
    override fun generateProject(project: Project) {
        super.generateProject(project)
        val config = project.webProject!!.config
        val pomTemplate = configuration.getTemplate(SBMPBackendFreeMarkerFileConstant.POM_TEMPLATE)
        val springbootMainTemplate =
            configuration.getTemplate(SBMPBackendFreeMarkerFileConstant.SPRING_BOOT_MAIN_APPLICATION_TEMPLATE)
        val applicationTemplate = configuration.getTemplate(SBMPBackendFreeMarkerFileConstant.APPLICATION_TEMPLATE)


        ioUtil.writeTemplate(pomTemplate, config, "${projectConfig.projectDir}/${config.artifactId}/pom.xml")

        ioUtil.writeTemplate(springbootMainTemplate, mapOf(
            "packageName" to config.rootPackageName,
            "mapperPackageName" to config.mapperPackagePath()
        ), "${pathConfig.rootDir(config)}/SpringMainApplication.java")

        ioUtil.writeTemplate(applicationTemplate, config,
            "${pathConfig.resourcesDirPath(config)}/application.yml")
    }
}
