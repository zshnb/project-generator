package com.zshnb.projectgenerator.generator.generator

import com.zshnb.projectgenerator.generator.constant.*
import com.zshnb.projectgenerator.generator.entity.*
import com.zshnb.projectgenerator.generator.extension.*
import com.zshnb.projectgenerator.generator.parser.BackendParser
import com.zshnb.projectgenerator.web.config.ProjectConfig
import freemarker.template.Configuration
import org.apache.commons.io.FileUtils
import org.springframework.stereotype.Component
import java.io.*

@Component
open class BaseGenerator(private val backendParser: BackendParser,
                         private val pathConstant: PathConstant,
                         private val projectConfig: ProjectConfig,
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

        val initSqlWriter = BufferedWriter(FileWriter("${pathConstant.resourcesDirPath()}/init.sql"))
        sqlTemplate.process(project, initSqlWriter)
        initSqlWriter.close()

        project.entities.forEach {
            val entityWriter = BufferedWriter(FileWriter("${project.config.entityDir(pathConstant)}/${it.name.capitalize()}.java"))
            val listRequestWriter = BufferedWriter(FileWriter("${project.config.requestDir(pathConstant)}/List${it.name.capitalize()}Request.java"))
            entityTemplate.process(it, entityWriter)
            listRequestTemplate.process(mapOf("packageName" to project.config.requestPackagePath(),
                "name" to it.name, "commonPackageName" to project.config.commonPackagePath()), listRequestWriter)
            entityWriter.close()
            listRequestWriter.close()
        }

        project.services.forEach {
            val serviceWriter = BufferedWriter(FileWriter("${project.config.serviceDir(pathConstant)}/I${it.name.capitalize()}Service.java"))
            val serviceImplWriter = BufferedWriter(FileWriter("${project.config.serviceImplDir(pathConstant)}/${it.name.capitalize()}ServiceImpl.java"))
            serviceTemplate.process(it, serviceWriter)
            serviceImplTemplate.process(it, serviceImplWriter)
            serviceWriter.close()
            serviceImplWriter.close()
        }

        project.mappers.forEach {
            val writer = BufferedWriter(FileWriter("${project.config.mapperDir(pathConstant)}/${it.name.capitalize()}Mapper.java"))
            val xmlWriter = BufferedWriter(FileWriter("${project.config.xmlDir(pathConstant)}/${it.name.capitalize()}Mapper.xml"))
            mapperTemplate.process(it, writer)
            mapperXmlTemplate.process(it, xmlWriter)
            writer.close()
            xmlWriter.close()
        }

        val pageRequestWriter = BufferedWriter(FileWriter("${project.config.commonDir(pathConstant)}/PageRequest.java"))
        pageRequestTemplate.process(mapOf("packageName" to project.config.commonPackagePath()), pageRequestWriter)
        pageRequestWriter.close()

        val listResponseWriter = BufferedWriter(FileWriter("${project.config.commonDir(pathConstant)}/ListResponse.java"))
        listResponseTemplate.process(mapOf("packageName" to project.config.commonPackagePath()), listResponseWriter)
        listResponseWriter.close()

        val pomWriter = BufferedWriter(FileWriter("${projectConfig.tempDir}/pom.xml"))
        pomTemplate.process(project.config, pomWriter)
        pomWriter.close()

        val springBootMainWriter = BufferedWriter(FileWriter("${project.config.rootDir(pathConstant)}/SpringMainApplication.java"))
        springbootMainTemplate.process(mapOf("packageName" to project.config.rootPackageName,
            "mapperPackageName" to project.config.mapperPackagePath()), springBootMainWriter)
        springBootMainWriter.close()

        val responseWriter = BufferedWriter(FileWriter("${project.config.commonDir(pathConstant)}/Response.java"))
        responseTemplate.process(mapOf("packageName" to project.config.commonPackagePath()), responseWriter)
        responseWriter.close()

        val applicationWriter = BufferedWriter(FileWriter("${pathConstant.resourcesDirPath()}/application.yml"))
        applicationTemplate.process(project.config, applicationWriter)
        applicationWriter.close()

        val configWriter = BufferedWriter(FileWriter("${project.config.configDir(pathConstant)}/MybatisPlusConfig.java"))
        mybatisPlusConfigTemplate.process(mapOf("packageName" to project.config.configPackagePath()), configWriter)
        configWriter.close()

        val menuWriter = BufferedWriter(FileWriter("${project.config.dtoDir(pathConstant)}/MenuDto.java"))
        menuDtoTemplate.process(mapOf("packageName" to project.config.dtoPackagePath()), menuWriter)
        menuWriter.close()

        val loginRequestWriter = BufferedWriter(FileWriter("${project.config.requestDir(pathConstant)}/LoginRequest.java"))
        loginRequestTemplate.process(mapOf("packageName" to project.config.requestPackagePath()), loginRequestWriter)
        loginRequestWriter.close()

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

        val initDataWriter = BufferedWriter(FileWriter("${pathConstant.resourcesDirPath()}/initData.sql"))
        initDataTemplate.process(mapOf( "roles" to roles, "menus" to menus, "permissions" to permissions), initDataWriter)
        initDataWriter.close()
    }

    private fun getChildMenus(menu: Menu): List<Menu> {
        val menus = mutableListOf(menu)
        if (menu.child.isNotEmpty()) {
            menus.addAll(menu.child.map { getChildMenus(it) }.flatten())
        }
        return menus
    }

    open fun mkdirs(config: Config) {
        FileUtils.cleanDirectory(File(projectConfig.tempDir))
        val entityDir = File(config.entityDir(pathConstant))
        val serviceDir = File(config.serviceDir(pathConstant))
        val serviceImplDir = File(config.serviceImplDir(pathConstant))
        val mapperDir = File(config.mapperDir(pathConstant))
        val controllerDir = File(config.controllerDir(pathConstant))
        val mapperXmlDir = File(config.xmlDir(pathConstant))
        val requestDir = File(config.requestDir(pathConstant))
        val commonDir = File(config.commonDir(pathConstant))
        val configDir = File(config.configDir(pathConstant))
        val dtoDir = File(config.dtoDir(pathConstant))

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