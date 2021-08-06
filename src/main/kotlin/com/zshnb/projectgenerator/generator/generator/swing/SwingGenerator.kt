package com.zshnb.projectgenerator.generator.generator.swing

import com.zshnb.projectgenerator.generator.config.PathConfig
import com.zshnb.projectgenerator.generator.constant.*
import com.zshnb.projectgenerator.generator.entity.Project
import com.zshnb.projectgenerator.generator.entity.web.*
import com.zshnb.projectgenerator.generator.extension.*
import com.zshnb.projectgenerator.generator.generator.BaseGenerator
import com.zshnb.projectgenerator.generator.parser.swing.FrameParser
import com.zshnb.projectgenerator.generator.parser.web.BackendParser
import com.zshnb.projectgenerator.generator.util.IOUtil
import com.zshnb.projectgenerator.web.config.ProjectConfig
import freemarker.template.Configuration
import org.springframework.stereotype.Component
import java.io.File

@Component
class SwingGenerator(private val configuration: Configuration,
                     private val projectConfig: ProjectConfig,
                     private val ioUtil: IOUtil,
                     private val pathConfig: PathConfig,
                     private val frameParser: FrameParser,
                     private val backendParser: BackendParser) : BaseGenerator {
    override fun generateProject(project: Project): Project {
        val swingProject = project.swingProject!!
        val config = swingProject.config
        mkdirs(config)
        val frameTemplate = configuration.getTemplate(SwingFreemarkerConstant.TABLE_FRAME_TEMPLATE)
        val pomTemplate = configuration.getTemplate(SwingFreemarkerConstant.POM_TEMPLATE)
        val entityTemplate = configuration.getTemplate(SwingFreemarkerConstant.ENTITY_TEMPLATE)
        val mapperTemplate = configuration.getTemplate(SwingFreemarkerConstant.MAPPER_TEMPLATE)
        val mapperXmlTemplate = configuration.getTemplate(SwingFreemarkerConstant.MAPPER_XML_TEMPLATE)

        ioUtil.writeTemplate(pomTemplate, config, "${projectConfig.projectDir}/${config.artifactId}/pom.xml")

        val frames = frameParser.parseFrames(swingProject)
        frames.forEach {
            ioUtil.writeTemplate(frameTemplate, mapOf(
                "frame" to it,
                "packageName" to config.framePackagePath()
            ), "${pathConfig.frameDir(config)}/${it.entity!!.name.capitalize()}Frame.java")
        }

        val tables = backendParser.parseTables(swingProject.tables)
        val entities = backendParser.parseEntities(tables)
        entities.forEach {
            ioUtil.writeTemplate(entityTemplate, mapOf(
                "entity" to it,
                "packageName" to config.entityPackagePath(),
                "config" to config),
                "${pathConfig.entityDir(config)}/${it.name.capitalize()}.java")
            ioUtil.writeTemplate(mapperTemplate, mapOf(
                "entity" to it,
                "packageName" to config.mapperPackagePath(),
                "entityPackageName" to config.entityPackagePath())
                , "${pathConfig.mapperDir(config)}/${it.name.capitalize()}Mapper.java")

            ioUtil.writeTemplate(mapperXmlTemplate, mapOf(
                "entity" to it,
                "packageName" to config.mapperPackagePath(),
                "entityPackageName" to config.entityPackagePath(),
                "config" to config)
                , "${pathConfig.xmlDir(config)}/${it.name.capitalize()}Mapper.xml")
        }

        return project
    }

    private fun mkdirs(config: Config) {
        val entityDir = File(pathConfig.entityDir(config))
        val mapperDir = File(pathConfig.mapperDir(config))
        val mapperXmlDir = File(pathConfig.xmlDir(config))
        val configDir = File(pathConfig.configDir(config))
        val frameDir = File(pathConfig.frameDir(config))
        val rootDir = File(pathConfig.rootDir(config))

        rootDir.mkdirs()
        entityDir.mkdir()
        mapperDir.mkdir()
        mapperXmlDir.mkdirs()
        configDir.mkdir()
        frameDir.mkdir()
    }
}
