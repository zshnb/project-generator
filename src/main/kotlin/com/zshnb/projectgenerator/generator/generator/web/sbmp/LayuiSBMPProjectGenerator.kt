package com.zshnb.projectgenerator.generator.generator.web.sbmp

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
import kotlin.random.Random

@Component
class LayuiSBMPProjectGenerator(private val backendParser: BackendParser,
                                private val configuration: Configuration,
                                private val projectConfig: ProjectConfig,
                                private val pathConfig: PathConfig,
                                private val frontendParser: FrontendParser,
                                private val ioUtil: IOUtil) :
    BaseSBMPProjectGenerator(backendParser, ioUtil, projectConfig, pathConfig, configuration) {
    override fun generateProject(project: Project): Project {
        val baseProject = super.generateProject(project)
        val webProject = baseProject.webProject!!
        val pageControllerTemplate = configuration.getTemplate(SBMPBackendFreeMarkerFileConstant.PAGE_CONTROLLER_TEMPLATE)
        val indexPageControllerTemplate =
            configuration.getTemplate(SBMPBackendFreeMarkerFileConstant.PAGE_INDEX_CONTROLLER_TEMPLATE)
        val indexPageTemplate = configuration.getTemplate(FrontendFreeMarkerFileConstant.LAYUI_INDEX_PAGE)
        val loginPageTemplate = configuration.getTemplate(FrontendFreeMarkerFileConstant.LAYUI_LOGIN_PAGES[Random.nextInt(3)])
        val registerPageTemplate = configuration.getTemplate(FrontendFreeMarkerFileConstant.LAYUI_REGISTER_PAGES[Random.nextInt(3)])
        val addPageTemplate = configuration.getTemplate(FrontendFreeMarkerFileConstant.LAYUI_ADD_PAGE)
        val editPageTemplate = configuration.getTemplate(FrontendFreeMarkerFileConstant.LAYUI_EDIT_PAGE)
        val detailPageTemplate = configuration.getTemplate(FrontendFreeMarkerFileConstant.LAYUI_DETAIL_PAGE)
        val tablePageTemplate = configuration.getTemplate(FrontendFreeMarkerFileConstant.LAYUI_TABLE_PAGE)
        val emptyPageTemplate = configuration.getTemplate(FrontendFreeMarkerFileConstant.LAYUI_EMPTY_PAGE)

        val pages = frontendParser.parsePages(webProject)
        val config = webProject.config
        createOtherDirs(pages.map { it.entity!!.name }, webProject.config)
        val resourceResolver = PathMatchingResourcePatternResolver()
        val resources = resourceResolver.getResources("/templates/layui/**")

        resources.filter {
            ReUtil.isMatch(".*?(css|images|js|lib).*?\\.[a-zA-Z0-9]*?$", it.url.path)
        }.map {
            val filePath = it.url.path.substring(it.url.path.indexOf("layui") + 5)
            it.url to File("${pathConfig.resourcesDirPath(config)}/static/$filePath")
        }.forEach {
            FileUtils.copyURLToFile(it.first, it.second)
        }

        val unBindMenus = webProject.roles.flatMap { it.menus }
            .filter { it.parentId == 0 && !it.bind }
            .distinctBy { it.name }
        unBindMenus.forEach {
            ioUtil.writeTemplate(emptyPageTemplate, mapOf("projectType" to "sbmp"),
                "${pathConfig.thymeleafPageDirPath(config)}${it.href}.html")
        }
        ioUtil.writeTemplate(indexPageControllerTemplate, mapOf(
            "packageName" to webProject.config.controllerPackagePath(),
            "dependencies" to listOf(webProject.config.entityPackagePath(), webProject.config.serviceImplPackagePath(),
                webProject.config.commonPackagePath(), webProject.config.requestPackagePath()),
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

        ioUtil.writeTemplate(indexPageTemplate, mapOf("config" to config,
            "projectType" to "sbmp",
            "theme" to Random.nextInt(10)
        ), "${pathConfig.thymeleafTemplateDirPath(config)}/index.html")
        ioUtil.writeTemplate(loginPageTemplate, mapOf("projectType" to "sbmp"),
            "${pathConfig.thymeleafTemplateDirPath(config)}/login.html")
        ioUtil.writeTemplate(registerPageTemplate, mapOf("projectType" to "sbmp"),
            "${pathConfig.thymeleafTemplateDirPath(config)}/register.html")
        pages.forEach {
            it.entity!!
            ioUtil.writeTemplate(addPageTemplate, mapOf(
                "page" to it,
                "projectType" to "sbmp"
            ), "${pathConfig.thymeleafPageDirPath(config)}/${it.entity.name}/add.html")
            ioUtil.writeTemplate(editPageTemplate, mapOf(
                "page" to it,
                "projectType" to "sbmp"
            ), "${pathConfig.thymeleafPageDirPath(config)}/${it.entity.name}/edit.html")
            ioUtil.writeTemplate(detailPageTemplate, mapOf(
                "page" to it,
                "projectType" to "sbmp"
            ), "${pathConfig.thymeleafPageDirPath(config)}/${it.entity.name}/detail.html")
            ioUtil.writeTemplate(tablePageTemplate, mapOf(
                "page" to it,
                "projectType" to "sbmp"
            ), "${pathConfig.thymeleafPageDirPath(config)}/${it.entity.name}/table.html")
        }
        return baseProject
    }

    override fun mkdirs(config: Config) {
        super.mkdirs(config)

        val pageDir = File(pathConfig.thymeleafPageDirPath(config))
        pageDir.mkdirs()

        val staticDir = File(pathConfig.layUIStaticDirPath(config))
        staticDir.mkdir()
    }

    private fun createOtherDirs(dirs: List<String>, config: Config) {
        dirs.forEach {
            File("${pathConfig.thymeleafPageDirPath(config)}/$it").mkdirs()
        }
    }
}
