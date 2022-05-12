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
class SwingProjectGenerator(private val configuration: Configuration,
                            private val projectConfig: ProjectConfig,
                            private val ioUtil: IOUtil,
                            private val pathConfig: PathConfig,
                            private val frameParser: FrameParser,
                            private val backendParser: BackendParser) : BaseGenerator {
    override fun generateProject(project: Project) {
        val swingProject = project.swingProject!!
        val config = swingProject.config
        mkdirs(config)
        createOtherDirs(swingProject.tables.filter { it.enablePage }.map { it.name }, config)
        val frameTemplate = configuration.getTemplate(SwingFreemarkerConstant.TABLE_FRAME_TEMPLATE)
        val pomTemplate = configuration.getTemplate(SwingFreemarkerConstant.POM_TEMPLATE)
        val entityTemplate = configuration.getTemplate(SwingFreemarkerConstant.ENTITY_TEMPLATE)
        val mapperTemplate = configuration.getTemplate(SwingFreemarkerConstant.MAPPER_TEMPLATE)
        val mapperXmlTemplate = configuration.getTemplate(SwingFreemarkerConstant.MAPPER_XML_TEMPLATE)
        val initTableTemplate = configuration.getTemplate(SBMPBackendFreeMarkerFileConstant.INIT_TABLE_TEMPLATE)
        val initDataTemplate = configuration.getTemplate(SBMPBackendFreeMarkerFileConstant.INIT_DATA_TEMPLATE)
        val globalSqlSessionFactoryTemplate = configuration.getTemplate(SwingFreemarkerConstant.GLOBAL_SQL_SESSION_FACTORY_TEMPLATE)
        val globalSessionTemplate = configuration.getTemplate(SwingFreemarkerConstant.GLOBAL_SESSION_TEMPLATE)
        val mybatisConfigTemplate = configuration.getTemplate(SwingFreemarkerConstant.MYBATIS_CONFIG_TEMPLATE)
        val loginFrameTemplate = configuration.getTemplate(SwingFreemarkerConstant.LOGIN_FRAME_TEMPLATE)
        val registerFrameTemplate = configuration.getTemplate(SwingFreemarkerConstant.REGISTER_FRAME_TEMPLATE)
        val mainFrameTemplate = configuration.getTemplate(SwingFreemarkerConstant.MAIN_FRAME_TEMPLATE)
        val dtoTemplate = configuration.getTemplate(SwingFreemarkerConstant.DTO_TEMPLATE)
        val listRequestTemplate = configuration.getTemplate(SwingFreemarkerConstant.LIST_REQUEST_TEMPLATE)
        val detailFrameTemplate = configuration.getTemplate(SwingFreemarkerConstant.DETAIL_FRAME_TEMPLATE)

        ioUtil.writeTemplate(loginFrameTemplate, mapOf(
            "configPackageName" to config.configPackagePath(),
            "entityPackageName" to config.entityPackagePath(),
            "mapperPackageName" to config.mapperPackagePath(),
            "framePackageName" to config.framePackagePath()
        ), "${pathConfig.frameDir(config)}/LoginFrame.java")
        ioUtil.writeTemplate(registerFrameTemplate, mapOf(
            "configPackageName" to config.configPackagePath(),
            "entityPackageName" to config.entityPackagePath(),
            "mapperPackageName" to config.mapperPackagePath(),
            "framePackageName" to config.framePackagePath()
        ), "${pathConfig.frameDir(config)}/RegisterFrame.java")
        ioUtil.writeTemplate(pomTemplate, config, "${projectConfig.projectDir}/${config.artifactId}/pom.xml")
        ioUtil.writeTemplate(globalSqlSessionFactoryTemplate, mapOf("configPackageName" to config.configPackagePath()),
            "${pathConfig.configDir(config)}/GlobalSqlSessionFactory.java")
        ioUtil.writeTemplate(globalSessionTemplate, mapOf(
            "configPackageName" to config.configPackagePath(),
            "entityPackageName" to config.entityPackagePath()
        ), "${pathConfig.configDir(config)}/GlobalSession.java")

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
                "dtoPackageName" to config.dtoPackagePath(),
                "requestPackageName" to config.requestPackagePath(),
                "entityPackageName" to config.entityPackagePath())
                , "${pathConfig.mapperDir(config)}/${it.name.capitalize()}Mapper.java")

            ioUtil.writeTemplate(mapperXmlTemplate, mapOf(
                "entity" to it,
                "packageName" to config.mapperPackagePath(),
                "dtoPackageName" to config.dtoPackagePath(),
                "entityPackageName" to config.entityPackagePath(),
                "config" to config)
                , "${pathConfig.xmlDir(config)}/${it.name.capitalize()}Mapper.xml")
            ioUtil.writeTemplate(listRequestTemplate, mapOf(
                "packageName" to config.requestPackagePath(),
                "entity" to it),
                "${pathConfig.requestDir(config)}/List${it.name.capitalize()}Request.java")
            if (it.table.associate) {
                val dtoDir = File(pathConfig.dtoDir(config))
                if (!dtoDir.exists()) {
                    dtoDir.mkdir()
                }
                ioUtil.writeTemplate(dtoTemplate,
                    mapOf("entity" to it, "packageName" to config.dtoPackagePath())
                    , "${pathConfig.dtoDir(config)}/${it.name.capitalize()}Dto.java")
            }
        }

        val frames = frameParser.parseFrames(swingProject.frames, entities)
        frames.forEach {
            val operations = it.entity!!.table.permissions.asSequence().map { it.operations }
                .flatten()
                .distinctBy { it.value }
                .toList()
            ioUtil.writeTemplate(frameTemplate, mapOf(
                "frame" to it,
                "operations" to operations,
                "packageName" to "${config.framePackagePath()}.${it.entity.name}",
                "entityPackageName" to config.entityPackagePath(),
                "dtoPackageName" to config.dtoPackagePath(),
                "requestPackageName" to config.requestPackagePath(),
                "configPackageName" to config.configPackagePath(),
                "mapperPackageName" to config.mapperPackagePath()
            ), "${pathConfig.frameDir(config)}/${it.entity.table.name}/${it.entity.name.capitalize()}Frame.java")
            ioUtil.writeTemplate(detailFrameTemplate, mapOf(
                "frame" to it,
                "packageName" to "${config.framePackagePath()}.${it.entity.name}",
                "entityPackageName" to config.entityPackagePath(),
                "dtoPackageName" to config.dtoPackagePath(),
                "configPackageName" to config.configPackagePath(),
                "mapperPackageName" to config.mapperPackagePath()
            ), "${pathConfig.frameDir(config)}/${it.entity.table.name}/${it.entity.name.capitalize()}DetailFrame.java")
        }

        ioUtil.writeTemplate(initTableTemplate, mapOf(
            "tables" to tables,
            "config" to config
        ), "${pathConfig.resourcesDirPath(config)}/initTable.sql")
        ioUtil.writeTemplate(mybatisConfigTemplate, mapOf(
            "config" to config,
            "entities" to entities), "${pathConfig.resourcesDirPath(config)}/mybatis-config.xml")

        var initMenuId = 1
        val roles = swingProject.roles
        val menus = roles.map { it.menus }
            .flatten()
            .map {
                Menu(initMenuId++, it.name, it.href, it.role, it.bind)
            }.toList()

        val permissions = backendParser.parsePermissions(entities)
        ioUtil.writeTemplate(initDataTemplate, mapOf(
            "roles" to roles,
            "menus" to menus,
            "permissions" to permissions,
            "config" to config),
            "${pathConfig.resourcesDirPath(config)}/initData.sql")
        ioUtil.writeTemplate(mainFrameTemplate, mapOf(
            "menus" to menus.distinctBy { it.href },
            "mapperPackageName" to config.mapperPackagePath(),
            "entityPackageName" to config.entityPackagePath(),
            "framePackageName" to config.framePackagePath(),
            "configPackageName" to config.configPackagePath(),
            "dependencies" to entities.filter { it.table.enablePage }.map { "${config.framePackagePath()}.${it.name}" }
            ),
            "${pathConfig.frameDir(config)}/MainFrame.java")

    }

    override fun mkdirs(config: Config) {
        val entityDir = File(pathConfig.entityDir(config))
        val mapperDir = File(pathConfig.mapperDir(config))
        val mapperXmlDir = File(pathConfig.xmlDir(config))
        val configDir = File(pathConfig.configDir(config))
        val frameDir = File(pathConfig.frameDir(config))
        val rootDir = File(pathConfig.rootDir(config))
        val requestDir = File(pathConfig.requestDir(config))

        rootDir.mkdirs()
        entityDir.mkdir()
        mapperDir.mkdir()
        mapperXmlDir.mkdirs()
        configDir.mkdir()
        frameDir.mkdir()
        requestDir.mkdir()
    }

    private fun createOtherDirs(dirs: List<String>, config: Config) {
        dirs.forEach {
            File("${pathConfig.frameDir(config)}/$it").mkdirs()
        }
    }
}
