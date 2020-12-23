package com.zshnb.projectgenerator.generator.generator

import com.zshnb.projectgenerator.generator.constant.*
import com.zshnb.projectgenerator.generator.entity.*
import com.zshnb.projectgenerator.generator.extension.*
import com.zshnb.projectgenerator.generator.parser.BackendParser
import com.zshnb.projectgenerator.generator.util.IOUtil
import freemarker.template.Configuration
import org.springframework.stereotype.Component
import java.io.*
import java.nio.charset.StandardCharsets

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
            configuration.getTemplate(BackendFreeMarkerFileConstant.SPRING_BOOT_MAIN_APPLICATION)
        val responseTemplate = configuration.getTemplate(BackendFreeMarkerFileConstant.RESPONSE_TEMPLATE)
        val sqlTemplate = configuration.getTemplate(BackendFreeMarkerFileConstant.SQL_TEMPLATE)
        val applicationTemplate = configuration.getTemplate(BackendFreeMarkerFileConstant.APPLICATION_TEMPLATE)
        val mybatisPlusConfigTemplate =
            configuration.getTemplate(BackendFreeMarkerFileConstant.MYBATIS_PLUS_CONFIG_TEMPLATE)
        val initDataTemplate = configuration.getTemplate(BackendFreeMarkerFileConstant.INIT_DATA_TEMPLATE)
        val menuDtoTemplate = configuration.getTemplate(BackendFreeMarkerFileConstant.MENU_DTO_TEMPLATE)
        val loginRequestTemplate = configuration.getTemplate(BackendFreeMarkerFileConstant.LOGIN_REQUEST_TEMPLATE)

        ioUtil.writeTemplate(sqlTemplate, project, "${PathConstant.resourcesDirPath(project.config)}/sql.sql")

        project.entities.forEach {
            ioUtil.writeTemplate(entityTemplate, it,
                "${project.config.entityDir(project.config)}/${it.name.capitalize()}.java")

            ioUtil.writeTemplate(listRequestTemplate, mapOf(
                "packageName" to project.config.requestPackagePath(),
                "name" to it.name, "commonPackageName" to project.config.commonPackagePath()),
                "${project.config.requestDir(project.config)}/List${it.name.capitalize()}Request.java")
        }

        project.services.forEach {
            ioUtil.writeTemplate(serviceTemplate, it,
                "${project.config.serviceDir(project.config)}/I${it.name.capitalize()}Service.java")

            ioUtil.writeTemplate(serviceImplTemplate, it,
                "${project.config.serviceImplDir(project.config)}/${it.name.capitalize()}ServiceImpl.java")
        }

        project.mappers.forEach {
            ioUtil.writeTemplate(mapperTemplate, it,
                "${project.config.mapperDir(project.config)}/${it.name.capitalize()}Mapper.java")

            ioUtil.writeTemplate(mapperXmlTemplate, it,
                "${project.config.xmlDir(project.config)}/${it.name.capitalize()}Mapper.xml")
        }

        ioUtil.writeTemplate(pageRequestTemplate, mapOf("packageName" to project.config.commonPackagePath()),
            "${project.config.commonDir(project.config)}/PageRequest.java")

        ioUtil.writeTemplate(listResponseTemplate, mapOf("packageName" to project.config.commonPackagePath()),
            "${project.config.commonDir(project.config)}/ListResponse.java")

        ioUtil.writeTemplate(pomTemplate, project.config,
            "${project.config.artifactId}/pom.xml")

        ioUtil.writeTemplate(springbootMainTemplate, mapOf(
            "packageName" to project.config.rootPackageName,
            "mapperPackageName" to project.config.mapperPackagePath()
        ), "${project.config.rootDir(project.config)}/SpringMainApplication.java")

        ioUtil.writeTemplate(responseTemplate, mapOf("packageName" to project.config.commonPackagePath()),
            "${project.config.commonDir(project.config)}/Response.java")

        ioUtil.writeTemplate(applicationTemplate, project.config,
            "${PathConstant.resourcesDirPath(project.config)}/application.yml")

        ioUtil.writeTemplate(mybatisPlusConfigTemplate, mapOf("packageName" to project.config.configPackagePath()),
            "${project.config.configDir(project.config)}/MybatisPlusConfig.java")

        ioUtil.writeTemplate(menuDtoTemplate, mapOf("packageName" to project.config.dtoPackagePath()),
            "${project.config.dtoDir(project.config)}/MenuDto.java")

        ioUtil.writeTemplate(loginRequestTemplate, mapOf("packageName" to project.config.requestPackagePath()),
            "${project.config.requestDir(project.config)}/LoginRequest.java")

        val roles = project.roles
        val menus = roles.map { it.menus }.flatten()
            .map { getChildMenus(it) }
            .flatten()

        val permissions = project.pages.map { page ->
            val model = page.name
            page.table.permissions.map {
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
        val entityDir = File(config.entityDir(config))
        val serviceDir = File(config.serviceDir(config))
        val serviceImplDir = File(config.serviceImplDir(config))
        val mapperDir = File(config.mapperDir(config))
        val controllerDir = File(config.controllerDir(config))
        val mapperXmlDir = File(config.xmlDir(config))
        val requestDir = File(config.requestDir(config))
        val commonDir = File(config.commonDir(config))
        val configDir = File(config.configDir(config))
        val dtoDir = File(config.dtoDir(config))

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