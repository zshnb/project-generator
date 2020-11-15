package com.zshnb.codegenerator.generator

import com.zshnb.codegenerator.constant.*
import com.zshnb.codegenerator.entity.*
import com.zshnb.codegenerator.extension.*
import com.zshnb.codegenerator.parser.BackendParser
import freemarker.template.Configuration
import org.apache.commons.io.FileUtils
import org.springframework.stereotype.Component
import java.io.*

@Component
open class BaseGenerator(private val backendParser: BackendParser,
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
        val springbootMainTemplate = configuration.getTemplate(BackendFreeMarkerFileConstant.SPRING_BOOT_MAIN_APPLICATION)
        val responseTemplate = configuration.getTemplate(BackendFreeMarkerFileConstant.RESPONSE_TEMPLATE)
        val sqlTemplate = configuration.getTemplate(BackendFreeMarkerFileConstant.SQL_TEMPLATE)
        val applicationTemplate = configuration.getTemplate(BackendFreeMarkerFileConstant.APPLICATION_TEMPLATE)
        val mybatisPlusConfigTemplate = configuration.getTemplate(BackendFreeMarkerFileConstant.MYBATIS_PLUS_CONFIG_TEMPLATE)
        val initDataTemplate = configuration.getTemplate(BackendFreeMarkerFileConstant.INIT_DATA_TEMPLATE)
        val menuDtoTemplate = configuration.getTemplate(BackendFreeMarkerFileConstant.MENU_DTO_TEMPLATE)
        val loginRequestTemplate = configuration.getTemplate(BackendFreeMarkerFileConstant.LOGIN_REQUEST_TEMPLATE)

        val initSqlWriter = BufferedWriter(FileWriter("${PathConstant.resourcesDirPath}/init.sql"))
        sqlTemplate.process(project, initSqlWriter)

        project.entities.forEach {
            val writer = BufferedWriter(FileWriter("${project.config.entityDir()}/${it.name.capitalize()}.java"))
            val listRequestWriter = BufferedWriter(FileWriter("${project.config.requestDir()}/List${it.name.capitalize()}Request.java"))
            entityTemplate.process(it, writer)
            listRequestTemplate.process(mapOf("packageName" to project.config.requestPackagePath(),
                "name" to it.name, "commonPackageName" to project.config.commonPackagePath()), listRequestWriter)
        }

        project.services.forEach {
            val serviceWriter = BufferedWriter(FileWriter("${project.config.serviceDir()}/I${it.name.capitalize()}Service.java"))
            val serviceImplWriter = BufferedWriter(FileWriter("${project.config.serviceImplDir()}/${it.name.capitalize()}ServiceImpl.java"))
            serviceTemplate.process(it, serviceWriter)
            serviceImplTemplate.process(it, serviceImplWriter)
        }

        project.mappers.forEach {
            val writer = BufferedWriter(FileWriter("${project.config.mapperDir()}/${it.name.capitalize()}Mapper.java"))
            val xmlWriter = BufferedWriter(FileWriter("${project.config.xmlDir()}/${it.name.capitalize()}Mapper.xml"))
            mapperTemplate.process(it, writer)
            mapperXmlTemplate.process(it, xmlWriter)
        }

        val pageRequestWriter = BufferedWriter(FileWriter("${project.config.commonDir()}/PageRequest.java"))
        pageRequestTemplate.process(mapOf("packageName" to project.config.commonPackagePath()), pageRequestWriter)

        val listResponseWriter = BufferedWriter(FileWriter("${project.config.commonDir()}/ListResponse.java"))
        listResponseTemplate.process(mapOf("packageName" to project.config.commonPackagePath()), listResponseWriter)

        val pomWriter = BufferedWriter(FileWriter("temp/pom.xml"))
        pomTemplate.process(project.config, pomWriter)

        val springBootMainWriter = BufferedWriter(FileWriter("${project.config.rootDir()}/SpringMainApplication.java"))
        springbootMainTemplate.process(mapOf("packageName" to project.config.rootPackageName,
            "mapperPackageName" to project.config.mapperPackagePath()), springBootMainWriter)

        val responseWriter = BufferedWriter(FileWriter("${project.config.commonDir()}/Response.java"))
        responseTemplate.process(mapOf("packageName" to project.config.commonPackagePath()), responseWriter)

        val applicationWriter = BufferedWriter(FileWriter("${PathConstant.resourcesDirPath}/application.yml"))
        applicationTemplate.process(project.config, applicationWriter)

        val configWriter = BufferedWriter(FileWriter("${project.config.configDir()}/MybatisPlusConfig.java"))
        mybatisPlusConfigTemplate.process(mapOf("packageName" to project.config.configPackagePath()), configWriter)

        val menuWriter = BufferedWriter(FileWriter("${project.config.dtoDir()}/MenuDto.java"))
        menuDtoTemplate.process(mapOf("packageName" to project.config.dtoPackagePath()), menuWriter)

        val loginRequestWriter = BufferedWriter(FileWriter("${project.config.requestDir()}/LoginRequest.java"))
        loginRequestTemplate.process(mapOf("packageName" to project.config.requestPackagePath()), loginRequestWriter)

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

        val initDataWriter = BufferedWriter(FileWriter("${PathConstant.resourcesDirPath}/initData.sql"))
        initDataTemplate.process(mapOf("roles" to roles, "menus" to menus, "permissions" to permissions, "permission" to Permission("aa", "aa", "aa")), initDataWriter)
    }

    private fun getChildMenus(menu: Menu): List<Menu> {
        val menus = mutableListOf(menu)
        if (menu.child.isNotEmpty()) {
            menus.addAll(menu.child.map { getChildMenus(it) }.flatten())
        }
        return menus
    }

    open fun mkdirs(config: Config) {
        FileUtils.deleteDirectory(File("./generator/temp"))
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