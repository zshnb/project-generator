package com.zshnb.projectgenerator.generator.generator.swing

import com.zshnb.projectgenerator.generator.config.PathConfig
import com.zshnb.projectgenerator.generator.constant.SwingFreemarkerConstant
import com.zshnb.projectgenerator.generator.entity.Project
import com.zshnb.projectgenerator.generator.entity.web.Config
import com.zshnb.projectgenerator.generator.extension.framePackagePath
import com.zshnb.projectgenerator.generator.generator.BaseGenerator
import com.zshnb.projectgenerator.generator.parser.swing.FrameParser
import com.zshnb.projectgenerator.generator.parser.web.BackendParser
import com.zshnb.projectgenerator.generator.util.IOUtil
import freemarker.template.Configuration
import org.springframework.stereotype.Component
import java.io.File

@Component
class SwingGenerator(private val configuration: Configuration,
                     private val ioUtil: IOUtil,
                     private val pathConfig: PathConfig,
                     private val frameParser: FrameParser,
                     private val backendParser: BackendParser) : BaseGenerator {
    override fun generateProject(project: Project): Project {
        val swingProject = project.swingProject!!
        val config = swingProject.config
        mkdirs(config)
        val frameTemplate = configuration.getTemplate(SwingFreemarkerConstant.TABLE_FRAME_TEMPLATE)

        val frames = frameParser.parseFrames(swingProject)
        frames.forEach {
            ioUtil.writeTemplate(frameTemplate, mapOf(
                "frame" to it,
                "packageName" to config.framePackagePath()
            ), "${pathConfig.frameDir(config)}/${it.entity!!.name.capitalize()}.java")
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
