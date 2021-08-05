package com.zshnb.projectgenerator.generator.generator.ssm

import cn.hutool.core.util.ReUtil
import com.zshnb.projectgenerator.generator.config.PathConfig
import com.zshnb.projectgenerator.generator.constant.*
import com.zshnb.projectgenerator.generator.entity.Project
import com.zshnb.projectgenerator.generator.entity.web.*
import com.zshnb.projectgenerator.generator.extension.*
import com.zshnb.projectgenerator.generator.parser.web.*
import com.zshnb.projectgenerator.generator.util.*
import com.zshnb.projectgenerator.web.config.ProjectConfig
import freemarker.template.Configuration
import org.apache.commons.io.FileUtils
import org.springframework.core.io.support.*
import org.springframework.stereotype.Component
import java.io.*

@Component
class LayuiSSMProjectGenerator(private val backendParser: BackendParser,
                               private val configuration: Configuration,
                               private val projectConfig: ProjectConfig,
                               private val pathConfig: PathConfig,
                               private val frontendParser: FrontendParser,
                               private val ioUtil: IOUtil) :
    BaseSSMProjectGenerator(backendParser, ioUtil, projectConfig, pathConfig, configuration) {
    override fun generateProject(project: Project): Project {
        val baseProject = super.generateProject(project)
        val webProject = baseProject.webProject!!
        val pageControllerTemplate = configuration.getTemplate(SSMBackendFreeMarkerFileConstant.PAGE_CONTROLLER_TEMPLATE)
        val indexPageControllerTemplate =
            configuration.getTemplate(SSMBackendFreeMarkerFileConstant.PAGE_INDEX_CONTROLLER_TEMPLATE)
        val webXmlTemplate = configuration.getTemplate(SSMBackendFreeMarkerFileConstant.WEB_XML)
        val indexPageTemplate = configuration.getTemplate(FrontendFreeMarkerFileConstant.LAY_UI_INDEX_PAGE)
        val loginPageTemplate = configuration.getTemplate(FrontendFreeMarkerFileConstant.LAY_UI_LOGIN_PAGE)
        val registerPageTemplate = configuration.getTemplate(FrontendFreeMarkerFileConstant.LAY_UI_REGISTER_PAGE)
        val addPageTemplate = configuration.getTemplate(FrontendFreeMarkerFileConstant.LAY_UI_ADD_PAGE)
        val editPageTemplate = configuration.getTemplate(FrontendFreeMarkerFileConstant.LAY_UI_EDIT_PAGE)
        val detailPageTemplate = configuration.getTemplate(FrontendFreeMarkerFileConstant.LAY_UI_DETAIL_PAGE)
        val tablePageTemplate = configuration.getTemplate(FrontendFreeMarkerFileConstant.LAY_UI_TABLE_PAGE)
        val emptyPageTemplate = configuration.getTemplate(FrontendFreeMarkerFileConstant.LAY_UI_EMPTY_PAGE)

        val config = webProject.config
        val resourceResolver = PathMatchingResourcePatternResolver()
        val resources = resourceResolver.getResources("/templates/layui/**")

        resources.filter {
            ReUtil.isMatch(".*?(css|images|js|lib).*?\\.[a-zA-Z0-9]*?$", it.url.path)
        }.map {
            val filePath = it.url.path.substring(it.url.path.indexOf("layui") + 5)
            it.url to File("${pathConfig.webappDirPath(config)}/static/$filePath")
        }.forEach {
            FileUtils.copyURLToFile(it.first, it.second)
        }

        val unBindMenus = webProject.roles.flatMap { it.menus }
            .filter { it.parentId == 0 && !it.bind }
            .distinctBy { it.name }
        unBindMenus.forEach {
            ioUtil.writeTemplate(emptyPageTemplate, mapOf("projectType" to "ssm"),
                "${pathConfig.jspPageDir(config)}${it.href}.jsp")
        }
        ioUtil.writeTemplate(indexPageControllerTemplate, mapOf(
            "packageName" to config.controllerPackagePath(),
            "dependencies" to listOf(config.entityPackagePath(), config.serviceImplPackagePath(),
                config.commonPackagePath(), config.requestPackagePath()),
            "unBindMenus" to unBindMenus),
            "${pathConfig.controllerDir(config)}/IndexController.java")

        val entities = backendParser.parseEntities(webProject.tables)
        entities.forEach { entity ->
            val operations = entity.table.permissions.asSequence().map { it.operations }
                .flatten()
                .distinctBy { it.value }
                .filter { it.custom }
                .toList()
            ioUtil.writeTemplate(pageControllerTemplate, mapOf(
                "entity" to entity,
                "operations" to operations,
                "packageName" to config.controllerPackagePath(),
                "dependencies" to listOf(config.entityPackagePath(), config.dtoPackagePath(), config.serviceImplPackagePath(),
                    config.commonPackagePath(), config.requestPackagePath())),
                "${pathConfig.controllerDir(config)}/${entity.name.capitalize()}Controller.java")
        }

        ioUtil.writeTemplate(webXmlTemplate, emptyMap<Int, Int>(),
            "${pathConfig.webappDirPath(config)}/WEB-INF/web.xml")
        ioUtil.writeTemplate(indexPageTemplate, mapOf("projectType" to "ssm", "config" to config),
            "${pathConfig.jspPageDir(config)}/index.jsp")
        ioUtil.writeTemplate(loginPageTemplate, mapOf("projectType" to "ssm"),
            "${pathConfig.jspPageDir(config)}/login.jsp")
        ioUtil.writeTemplate(registerPageTemplate, mapOf("projectType" to "ssm"),
            "${pathConfig.jspPageDir(config)}/register.jsp")

        val pages = frontendParser.parsePages(webProject)
        createOtherDirs(pages.map { it.entity!!.name }, webProject.config)
        pages.forEach {
            it.entity!!
            ioUtil.writeTemplate(addPageTemplate, mapOf(
                "page" to it,
                "projectType" to "ssm"
            ), "${pathConfig.jspPageDir(config)}/page/${it.entity.name}/add.jsp")
            ioUtil.writeTemplate(editPageTemplate, mapOf(
                "page" to it,
                "projectType" to "ssm"
            ), "${pathConfig.jspPageDir(config)}/page/${it.entity.name}/edit.jsp")
            ioUtil.writeTemplate(detailPageTemplate, mapOf(
                "page" to it,
                "projectType" to "ssm"
            ), "${pathConfig.jspPageDir(config)}/page/${it.entity.name}/detail.jsp")
            ioUtil.writeTemplate(tablePageTemplate, mapOf(
                "page" to it,
                "projectType" to "ssm"
            ), "${pathConfig.jspPageDir(config)}/page/${it.entity.name}/table.jsp")
        }
        return baseProject
    }

    override fun mkdirs(config: Config) {
        super.mkdirs(config)
        val pageDir = File(pathConfig.jspPageDir(config))
        pageDir.mkdirs()
    }

    private fun createOtherDirs(dirs: List<String>, config: Config) {
        dirs.forEach {
            File("${pathConfig.jspPageDir(config)}/page/$it").mkdirs()
        }
    }
}
