package com.zshnb.projectgenerator.generator.generator

import com.zshnb.projectgenerator.generator.config.PathConfig
import com.zshnb.projectgenerator.generator.constant.*
import com.zshnb.projectgenerator.generator.parser.BackendParser
import com.zshnb.projectgenerator.generator.util.IOUtil
import com.zshnb.projectgenerator.web.config.ProjectConfig
import freemarker.template.Configuration
import org.springframework.stereotype.Component

@Component
class RestfulBackendGenerator(private val backendParser: BackendParser,
                              private val ioUtil: IOUtil,
                              private val pathConfig: PathConfig,
                              private val projectConfig: ProjectConfig,
                              private val configuration: Configuration) :
    BaseGenerator(backendParser, ioUtil, projectConfig, pathConfig, configuration) {
    override fun generateProject(json: String) {
        super.generateProject(json)
        val controllerTemplate = configuration.getTemplate(BackendFreeMarkerFileConstant.RESTFUL_CONTROLLER_TEMPLATE)
        val project = backendParser.parseProject(json)
        project.controllers.forEach {
            ioUtil.writeTemplate(controllerTemplate, it,
                "${pathConfig.controllerDir(project.config)}/${it.name.capitalize()}Controller.java")
        }
    }
}
