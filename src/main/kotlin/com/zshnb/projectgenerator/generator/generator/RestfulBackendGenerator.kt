package com.zshnb.projectgenerator.generator.generator

import com.zshnb.projectgenerator.generator.constant.BackendFreeMarkerFileConstant
import com.zshnb.projectgenerator.generator.extension.controllerDir
import com.zshnb.projectgenerator.generator.parser.BackendParser
import freemarker.template.Configuration
import org.springframework.stereotype.Component
import java.io.*

@Component
class RestfulBackendGenerator(private val backendParser: BackendParser,
                              private val configuration: Configuration): BaseGenerator(backendParser, configuration) {
    override fun generateProject(json: String) {
        super.generateProject(json)
        val controllerTemplate = configuration.getTemplate(BackendFreeMarkerFileConstant.RESTFUL_CONTROLLER_TEMPLATE)
        val project = backendParser.parseProject(json)
        project.controllers.forEach {
            val writer = BufferedWriter(FileWriter("${project.config.controllerDir()}/${it.name.capitalize()}Controller.java"))
            controllerTemplate.process(it, writer)
        }
    }
}