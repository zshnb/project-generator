package com.zshnb.projectgenerator.generator.generator.web.ssm

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
open class SSMBackendGenerator(backendParser: BackendParser,
                               private val ioUtil: IOUtil,
                               private val projectConfig: ProjectConfig,
                               private val pathConfig: PathConfig,
                               private val configuration: Configuration)
    : BaseGenerator, BaseWebProjectGenerator(backendParser, ioUtil, projectConfig, pathConfig, configuration) {
    override fun generateProject(project: Project): Project {
        super.generateProject(project)
        val config = project.webProject!!.config

        val pomTemplate = configuration.getTemplate(SSMBackendFreeMarkerFileConstant.POM_TEMPLATE)
        val springServletTemplate = configuration.getTemplate(SSMBackendFreeMarkerFileConstant.SPRING_SERVLET)

        ioUtil.writeTemplate(springServletTemplate, config,
            "${pathConfig.resourcesDirPath(config)}/spring-servlet.xml")
        ioUtil.writeTemplate(pomTemplate, config, "${projectConfig.projectDir}/${config.artifactId}/pom.xml")
        return project
    }
}
