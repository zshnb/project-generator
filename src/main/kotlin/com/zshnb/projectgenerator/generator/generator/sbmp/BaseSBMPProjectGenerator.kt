package com.zshnb.projectgenerator.generator.generator.sbmp

import com.zshnb.projectgenerator.generator.config.PathConfig
import com.zshnb.projectgenerator.generator.constant.*
import com.zshnb.projectgenerator.generator.entity.Project
import com.zshnb.projectgenerator.generator.entity.web.*
import com.zshnb.projectgenerator.generator.extension.*
import com.zshnb.projectgenerator.generator.generator.BaseGenerator
import com.zshnb.projectgenerator.generator.parser.web.BackendParser
import com.zshnb.projectgenerator.generator.util.*
import com.zshnb.projectgenerator.web.config.ProjectConfig
import freemarker.template.Configuration
import org.springframework.stereotype.Component
import java.io.*

@Component
open class BaseSBMPProjectGenerator(private val backendParser: BackendParser,
                                    private val ioUtil: IOUtil,
                                    private val projectConfig: ProjectConfig,
                                    private val pathConfig: PathConfig,
                                    private val configuration: Configuration) : BaseGenerator{
    override fun generateProject(project: Project): Project {
        val webProject = backendParser.parseProject(project.webProject!!)
        val config = webProject.config
        mkdirs(config)

        val entityTemplate = configuration.getTemplate(SBMPBackendFreeMarkerFileConstant.ENTITY_TEMPLATE)
        val serviceTemplate = configuration.getTemplate(SBMPBackendFreeMarkerFileConstant.SERVICE_TEMPLATE)
        val serviceImplTemplate = configuration.getTemplate(SBMPBackendFreeMarkerFileConstant.SERVICE_IMPL_TEMPLATE)
        val mapperTemplate = configuration.getTemplate(SBMPBackendFreeMarkerFileConstant.MAPPER_TEMPLATE)
        val mapperXmlTemplate = configuration.getTemplate(SBMPBackendFreeMarkerFileConstant.MAPPER_XML_TEMPLATE)
        val listRequestTemplate = configuration.getTemplate(SBMPBackendFreeMarkerFileConstant.LIST_REQUEST_TEMPLATE)
        val listResponseTemplate = configuration.getTemplate(SBMPBackendFreeMarkerFileConstant.LIST_RESPONSE_TEMPLATE)
        val pageRequestTemplate = configuration.getTemplate(SBMPBackendFreeMarkerFileConstant.PAGE_REQUEST_TEMPLATE)
        val pomTemplate = configuration.getTemplate(SBMPBackendFreeMarkerFileConstant.POM_TEMPLATE)
        val springbootMainTemplate =
            configuration.getTemplate(SBMPBackendFreeMarkerFileConstant.SPRING_BOOT_MAIN_APPLICATION_TEMPLATE)
        val responseTemplate = configuration.getTemplate(SBMPBackendFreeMarkerFileConstant.RESPONSE_TEMPLATE)
        val initTableTemplate = configuration.getTemplate(SBMPBackendFreeMarkerFileConstant.INIT_TABLE_TEMPLATE)
        val applicationTemplate = configuration.getTemplate(SBMPBackendFreeMarkerFileConstant.APPLICATION_TEMPLATE)
        val mybatisPlusConfigTemplate =
            configuration.getTemplate(SBMPBackendFreeMarkerFileConstant.MYBATIS_PLUS_CONFIG_TEMPLATE)
        val initDataTemplate = configuration.getTemplate(SBMPBackendFreeMarkerFileConstant.INIT_DATA_TEMPLATE)
        val menuDtoTemplate = configuration.getTemplate(SBMPBackendFreeMarkerFileConstant.MENU_DTO_TEMPLATE)
        val loginRequestTemplate = configuration.getTemplate(SBMPBackendFreeMarkerFileConstant.LOGIN_REQUEST_TEMPLATE)
        val staticResourceControllerTemplate =
            configuration.getTemplate(SBMPBackendFreeMarkerFileConstant.STATIC_RESOURCE_CONTROLLER_TEMPLATE)
        val localDateTimeMetaObjectHandlerTemplate =
            configuration.getTemplate(SBMPBackendFreeMarkerFileConstant.LOCAL_DATE_TIME_META_OBJECT_HANDLER_TEMPLATE)
        val uploadResponseTemplate = configuration.getTemplate(SBMPBackendFreeMarkerFileConstant.UPLOAD_RESPONSE_TEMPLATE)
        val dtoTemplate = configuration.getTemplate(SBMPBackendFreeMarkerFileConstant.DTO_TEMPLATE)
        val invalidArgumentException = configuration.getTemplate(SBMPBackendFreeMarkerFileConstant.INVALID_ARGUMENT_EXCEPTION_TEMPLATE)
        val globalExceptionController = configuration.getTemplate(SBMPBackendFreeMarkerFileConstant.GLOBAL_EXCEPTION_CONTROLLER_TEMPLATE)

        ioUtil.writeTemplate(initTableTemplate,
            webProject,
            "${pathConfig.resourcesDirPath(config)}/initTable.sql")
        ioUtil.writeTemplate(staticResourceControllerTemplate, mapOf(
            "packageName" to config.controllerPackagePath(),
            "commonPackageName" to config.commonPackagePath()
        ), "${pathConfig.controllerDir(config)}/StaticResourceController.java")

        val entities = backendParser.parseEntities(webProject.tables)
        entities.forEach {
            val operations = it.table.permissions.asSequence().map { it.operations }
                .flatten()
                .distinctBy { it.value }
                .filter { it.custom && it.type == OperationType.AJAX }
                .toList()
            ioUtil.writeTemplate(entityTemplate, mapOf(
                "entity" to it,
                "packageName" to config.entityPackagePath(),
                "config" to config),
                "${pathConfig.entityDir(config)}/${it.name.capitalize()}.java")
            ioUtil.writeTemplate(listRequestTemplate, mapOf(
                "packageName" to config.requestPackagePath(),
                "entity" to it,
                "commonPackageName" to config.commonPackagePath()),
                "${pathConfig.requestDir(config)}/List${it.name.capitalize()}Request.java")
            ioUtil.writeTemplate(mapperTemplate, mapOf(
                "entity" to it,
                "packageName" to config.mapperPackagePath(),
                "entityPackageName" to config.entityPackagePath(),
                "requestPackageName" to config.requestPackagePath(),
                "dtoPackageName" to config.dtoPackagePath(),
                "config" to config)
                , "${pathConfig.mapperDir(config)}/${it.name.capitalize()}Mapper.java")

            ioUtil.writeTemplate(mapperXmlTemplate, mapOf(
                "entity" to it,
                "packageName" to config.mapperPackagePath(),
                "dtoPackageName" to config.dtoPackagePath(),
                "config" to config)
                , "${pathConfig.xmlDir(config)}/${it.name.capitalize()}Mapper.xml")

            ioUtil.writeTemplate(serviceTemplate, mapOf(
                "entity" to it,
                "operations" to operations,
                "packageName" to config.servicePackagePath(),
                "dependencies" to listOf(
                    config.entityPackagePath(), config.commonPackagePath(), config.requestPackagePath(), config.dtoPackagePath()
                )),
                "${pathConfig.serviceDir(config)}/I${it.name.capitalize()}Service.java")

            ioUtil.writeTemplate(serviceImplTemplate, mapOf(
                "entity" to it,
                "operations" to operations,
                "packageName" to config.serviceImplPackagePath(),
                "servicePackageName" to config.servicePackagePath(),
                "tables" to webProject.tables,
                "dependencies" to listOf(
                    config.entityPackagePath(), config.commonPackagePath(), config.requestPackagePath(), config.dtoPackagePath(),
                    config.exceptionPackagePath(), config.mapperPackagePath()
                ),
                "config" to config),
                "${pathConfig.serviceImplDir(config)}/${it.name.capitalize()}ServiceImpl.java")
            if (it.table.associate) {
                ioUtil.writeTemplate(dtoTemplate,
                    mapOf("entity" to it, "packageName" to config.dtoPackagePath())
                    , "${pathConfig.dtoDir(config)}/${it.name.capitalize()}Dto.java")
            }
        }

        ioUtil.writeTemplate(pageRequestTemplate, config.commonPackagePath().packageName(),
            "${pathConfig.commonDir(config)}/PageRequest.java")

        ioUtil.writeTemplate(listResponseTemplate, config.commonPackagePath().packageName(),
            "${pathConfig.commonDir(config)}/ListResponse.java")

        ioUtil.writeTemplate(pomTemplate, config, "${projectConfig.projectDir}/${config.artifactId}/pom.xml")

        ioUtil.writeTemplate(springbootMainTemplate, mapOf(
            "packageName" to config.rootPackageName,
            "mapperPackageName" to config.mapperPackagePath()
        ), "${pathConfig.rootDir(config)}/SpringMainApplication.java")

        ioUtil.writeTemplate(responseTemplate, config.commonPackagePath().packageName(),
            "${pathConfig.commonDir(config)}/Response.java")

        ioUtil.writeTemplate(applicationTemplate, config,
            "${pathConfig.resourcesDirPath(config)}/application.yml")

        ioUtil.writeTemplate(mybatisPlusConfigTemplate,
            mapOf("config" to config, "packageName" to config.configPackagePath()),
            "${pathConfig.configDir(config)}/MybatisPlusConfig.java")

        ioUtil.writeTemplate(menuDtoTemplate, config.dtoPackagePath().packageName(),
            "${pathConfig.dtoDir(config)}/MenuDto.java")

        ioUtil.writeTemplate(loginRequestTemplate, config.requestPackagePath().packageName(),
            "${pathConfig.requestDir(config)}/LoginRequest.java")

        ioUtil.writeTemplate(
            localDateTimeMetaObjectHandlerTemplate, config.configPackagePath().packageName(),
            "${pathConfig.configDir(config)}/LocalDateTimeMetaObjectHandler.java")

        ioUtil.writeTemplate(uploadResponseTemplate, config.commonPackagePath().packageName(),
            "${pathConfig.commonDir(config)}/UploadResponse.java")

        ioUtil.writeTemplate(invalidArgumentException, config.exceptionPackagePath().packageName(),
            "${pathConfig.exceptionDir(config)}/InvalidArgumentException.java")

        ioUtil.writeTemplate(globalExceptionController, mapOf(
            "packageName" to config.commonPackagePath(),
            "dependencies" to listOf(config.exceptionPackagePath(), config.commonPackagePath())),
            "${pathConfig.commonDir(config)}/GlobalExceptionController.java")

        var initMenuId = 1
        val roles = webProject.roles
        val menus = roles.asSequence().map { it.menus }
            .flatten()
            .map { getChildMenus(it) }
            .flatten()
            .map {
                Menu(initMenuId++, it.parentId, it.name, it.icon, it.href, it.role, it.bind, it.child).apply {
                    child.forEach { childMenu ->
                        childMenu.parentId = id
                        childMenu.role = role
                    }
                }
            }.toList()

        val permissions = backendParser.parsePermissions(entities)
        ioUtil.writeTemplate(initDataTemplate,
            mapOf("roles" to roles, "menus" to menus, "permissions" to permissions, "config" to config),
            "${pathConfig.resourcesDirPath(config)}/initData.sql")

        return Project(webProject = webProject)
    }

    private fun getChildMenus(menu: Menu): List<Menu> {
        val menus = mutableListOf(menu)
        if (menu.child.isNotEmpty()) {
            menus.addAll(menu.child.map { getChildMenus(it) }.flatten())
        }
        return menus
    }

    open fun mkdirs(config: Config) {
        val entityDir = File(pathConfig.entityDir(config))
        val serviceDir = File(pathConfig.serviceDir(config))
        val serviceImplDir = File(pathConfig.serviceImplDir(config))
        val mapperDir = File(pathConfig.mapperDir(config))
        val controllerDir = File(pathConfig.controllerDir(config))
        val mapperXmlDir = File(pathConfig.xmlDir(config))
        val requestDir = File(pathConfig.requestDir(config))
        val commonDir = File(pathConfig.commonDir(config))
        val configDir = File(pathConfig.configDir(config))
        val dtoDir = File(pathConfig.dtoDir(config))
        val exceptionDir = File(pathConfig.exceptionDir(config))
        val rootDir = File(pathConfig.rootDir(config))

        rootDir.mkdirs()
        entityDir.mkdir()
        serviceDir.mkdir()
        serviceImplDir.mkdir()
        mapperDir.mkdir()
        controllerDir.mkdir()
        mapperXmlDir.mkdirs()
        requestDir.mkdir()
        commonDir.mkdir()
        configDir.mkdir()
        dtoDir.mkdir()
        exceptionDir.mkdir()
    }
}
