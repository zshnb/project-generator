package com.zshnb.projectgenerator.generator.generator

import com.zshnb.projectgenerator.generator.constant.*
import com.zshnb.projectgenerator.generator.entity.*
import com.zshnb.projectgenerator.generator.extension.*
import com.zshnb.projectgenerator.generator.parser.BackendParser
import com.zshnb.projectgenerator.generator.util.*
import freemarker.template.Configuration
import org.springframework.stereotype.Component
import java.io.*

@Component
open class BaseGenerator(private val backendParser: BackendParser,
                         private val ioUtil: IOUtil,
                         private val configuration: Configuration) {
    open fun generateProject(json: String) {
        val project = backendParser.parseProject(json)
        mkdirs(project.config)

        val entityTemplate = configuration.getTemplate(BackendFreeMarkerFileConstant.ENTITY_TEMPLATE)
        val serviceTemplate = configuration.getTemplate(BackendFreeMarkerFileConstant.SERVICE_TEMPLATE)
        val serviceImplTemplate = configuration.getTemplate(BackendFreeMarkerFileConstant.SERVICE_IMPL_TEMPLATE)
        val mapperTemplate = configuration.getTemplate(BackendFreeMarkerFileConstant.MAPPER_TEMPLATE)
        val mapperXmlTemplate = configuration.getTemplate(BackendFreeMarkerFileConstant.MAPPER_XML_TEMPLATE)
        val listRequestTemplate = configuration.getTemplate(BackendFreeMarkerFileConstant.LIST_REQUEST_TEMPLATE)
        val listResponseTemplate = configuration.getTemplate(BackendFreeMarkerFileConstant.LIST_RESPONSE_TEMPLATE)
        val pageRequestTemplate = configuration.getTemplate(BackendFreeMarkerFileConstant.PAGE_REQUEST_TEMPLATE)
        val pomTemplate = configuration.getTemplate(BackendFreeMarkerFileConstant.POM_TEMPLATE)
        val springbootMainTemplate =
            configuration.getTemplate(BackendFreeMarkerFileConstant.SPRING_BOOT_MAIN_APPLICATION_TEMPLATE)
        val responseTemplate = configuration.getTemplate(BackendFreeMarkerFileConstant.RESPONSE_TEMPLATE)
        val initTableTemplate = configuration.getTemplate(BackendFreeMarkerFileConstant.INIT_TABLE_TEMPLATE)
        val applicationTemplate = configuration.getTemplate(BackendFreeMarkerFileConstant.APPLICATION_TEMPLATE)
        val mybatisPlusConfigTemplate =
            configuration.getTemplate(BackendFreeMarkerFileConstant.MYBATIS_PLUS_CONFIG_TEMPLATE)
        val initDataTemplate = configuration.getTemplate(BackendFreeMarkerFileConstant.INIT_DATA_TEMPLATE)
        val menuDtoTemplate = configuration.getTemplate(BackendFreeMarkerFileConstant.MENU_DTO_TEMPLATE)
        val loginRequestTemplate = configuration.getTemplate(BackendFreeMarkerFileConstant.LOGIN_REQUEST_TEMPLATE)
        val staticResourceControllerTemplate =
            configuration.getTemplate(BackendFreeMarkerFileConstant.STATIC_RESOURCE_CONTROLLER_TEMPLATE)
        val localDateTimeMetaObjectHandlerTemplate =
            configuration.getTemplate(BackendFreeMarkerFileConstant.LOCAL_DATE_TIME_META_OBJECT_HANDLER_TEMPLATE)
        val uploadResponseTemplate = configuration.getTemplate(BackendFreeMarkerFileConstant.UPLOAD_RESPONSE_TEMPLATE)
        val dtoTemplate = configuration.getTemplate(BackendFreeMarkerFileConstant.DTO_TEMPLATE)

        ioUtil.writeTemplate(initTableTemplate,
            project,
            "${PathConstant.resourcesDirPath(project.config)}/initTable.sql")
        ioUtil.writeTemplate(staticResourceControllerTemplate, mapOf(
            "packageName" to project.config.controllerPackagePath(),
            "commonPackageName" to project.config.commonPackagePath()
        ), "${project.config.controllerDir()}/StaticResourceController.java")

        project.entities.forEach {
            ioUtil.writeTemplate(entityTemplate, it, "${project.config.entityDir()}/${it.name.capitalize()}.java")
            ioUtil.writeTemplate(listRequestTemplate, mapOf(
                "packageName" to project.config.requestPackagePath(),
                "entity" to it,
                "commonPackageName" to project.config.commonPackagePath()),
                "${project.config.requestDir()}/List${it.name.capitalize()}Request.java")
            if (it.table.associate) {
                val dto = Dto(it, project.config.dtoPackagePath())
                ioUtil.writeTemplate(dtoTemplate, dto, "${project.config.dtoDir()}/${it.name.capitalize()}Dto.java")
            }
        }

        project.services.forEach {
            ioUtil.writeTemplate(serviceTemplate,
                it,
                "${project.config.serviceDir()}/I${it.entity.name.capitalize()}Service.java")

            ioUtil.writeTemplate(serviceImplTemplate,
                it,
                "${project.config.serviceImplDir()}/${it.entity.name.capitalize()}ServiceImpl.java")
        }

        project.mappers.forEach {
            ioUtil.writeTemplate(mapperTemplate, it, "${project.config.mapperDir()}/${it.name.capitalize()}Mapper.java")

            ioUtil.writeTemplate(mapperXmlTemplate, it, "${project.config.xmlDir()}/${it.name.capitalize()}Mapper.xml")
        }

        ioUtil.writeTemplate(pageRequestTemplate, project.config.commonPackagePath().packageName(),
            "${project.config.commonDir()}/PageRequest.java")

        ioUtil.writeTemplate(listResponseTemplate, project.config.commonPackagePath().packageName(),
            "${project.config.commonDir()}/ListResponse.java")

        ioUtil.writeTemplate(pomTemplate, project.config, "${project.config.artifactId}/pom.xml")

        ioUtil.writeTemplate(springbootMainTemplate, mapOf(
            "packageName" to project.config.rootPackageName,
            "mapperPackageName" to project.config.mapperPackagePath()
        ), "${project.config.rootDir()}/SpringMainApplication.java")

        ioUtil.writeTemplate(responseTemplate, project.config.commonPackagePath().packageName(),
            "${project.config.commonDir()}/Response.java")

        ioUtil.writeTemplate(applicationTemplate,
            project.config,
            "${PathConstant.resourcesDirPath(project.config)}/application.yml")

        ioUtil.writeTemplate(mybatisPlusConfigTemplate, project.config.configPackagePath().packageName(),
            "${project.config.configDir()}/MybatisPlusConfig.java")

        ioUtil.writeTemplate(menuDtoTemplate, project.config.dtoPackagePath().packageName(),
            "${project.config.dtoDir()}/MenuDto.java")

        ioUtil.writeTemplate(loginRequestTemplate, project.config.requestPackagePath().packageName(),
            "${project.config.requestDir()}/LoginRequest.java")

        ioUtil.writeTemplate(
            localDateTimeMetaObjectHandlerTemplate, project.config.configPackagePath().packageName(),
            "${project.config.configDir()}/LocalDateTimeMetaObjectHandler.java")

        ioUtil.writeTemplate(uploadResponseTemplate, project.config.commonPackagePath().packageName(),
            "${project.config.commonDir()}/UploadResponse.java")

        var initMenuId = 1
        val roles = project.roles
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

        val permissions = project.entities.map { entity ->
            val model = entity.name
            entity.table.permissions.map {
                it.operations.map { op ->
                    Permission(op, it.role, model)
                }
            }
        }.flatten().flatten()

        ioUtil.writeTemplate(initDataTemplate, mapOf("roles" to roles, "menus" to menus, "permissions" to permissions),
            "${PathConstant.resourcesDirPath(project.config)}/initData.sql")
    }

    private fun getChildMenus(menu: Menu): List<Menu> {
        val menus = mutableListOf(menu)
        if (menu.child.isNotEmpty()) {
            menus.addAll(menu.child.map { getChildMenus(it) }.flatten())
        }
        return menus
    }

    open fun mkdirs(config: Config) {
        val entityDir = File(config.entityDir())
        val serviceDir = File(config.serviceDir())
        val serviceImplDir = File(config.serviceImplDir())
        val mapperDir = File(config.mapperDir())
        val controllerDir = File(config.controllerDir())
        val mapperXmlDir = File(config.xmlDir())
        val requestDir = File(config.requestDir())
        val commonDir = File(config.commonDir())
        val configDir = File(config.configDir())
        val dtoDir = File(config.dtoDir())

        entityDir.mkdirs()
        serviceDir.mkdirs()
        serviceImplDir.mkdirs()
        mapperDir.mkdirs()
        controllerDir.mkdirs()
        mapperXmlDir.mkdirs()
        requestDir.mkdirs()
        commonDir.mkdirs()
        configDir.mkdirs()
        dtoDir.mkdirs()
    }
}
