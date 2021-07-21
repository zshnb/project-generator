package com.zshnb.projectgenerator.generator.generator

import cn.hutool.core.util.ReUtil
import com.zshnb.projectgenerator.generator.config.PathConfig
import com.zshnb.projectgenerator.generator.constant.*
import com.zshnb.projectgenerator.generator.entity.*
import com.zshnb.projectgenerator.generator.extension.*
import com.zshnb.projectgenerator.generator.parser.*
import com.zshnb.projectgenerator.generator.util.*
import com.zshnb.projectgenerator.web.config.ProjectConfig
import freemarker.template.Configuration
import org.apache.commons.io.FileUtils
import org.springframework.core.io.support.*
import org.springframework.stereotype.Component
import java.io.*

@Component
class LayuiGenerator(private val backendParser: BackendParser,
                     private val configuration: Configuration,
                     private val projectConfig: ProjectConfig,
                     private val pathConfig: PathConfig,
                     private val frontendParser: FrontendParser,
                     private val ioUtil: IOUtil) :
    BaseGenerator(backendParser, ioUtil, projectConfig, pathConfig, configuration) {
    override fun generateProject(json: String): Project {
        val project = super.generateProject(json)
        val controllerTemplate = configuration.getTemplate(BackendFreeMarkerFileConstant.PAGE_CONTROLLER_TEMPLATE)
        val indexControllerTemplate =
            configuration.getTemplate(BackendFreeMarkerFileConstant.PAGE_INDEX_CONTROLLER_TEMPLATE)
        val addPageTemplate = configuration.getTemplate(FrontendFreeMarkerFileConstant.LAY_UI_ADD_PAGE)
        val editPageTemplate = configuration.getTemplate(FrontendFreeMarkerFileConstant.LAY_UI_EDIT_PAGE)
        val detailPageTemplate = configuration.getTemplate(FrontendFreeMarkerFileConstant.LAY_UI_DETAIL_PAGE)
        val tablePageTemplate = configuration.getTemplate(FrontendFreeMarkerFileConstant.LAY_UI_TABLE_PAGE)
        val emptyPageTemplate = configuration.getTemplate(FrontendFreeMarkerFileConstant.LAY_UI_EMPTY_PAGE)

        val pages = frontendParser.parsePages(project)
        val config = project.config
        createOtherDirs(pages.map { it.entity!!.name }, project.config)
        val resourceResolver = PathMatchingResourcePatternResolver()
        val resources = resourceResolver.getResources("/templates/layui/**")

        resources.forEach {
            val url = it.url
            val filePath = url.path.substring(url.path.indexOf("layui") + 5)
            val destination = if (ReUtil.isMatch(".*?(css|images|js|lib).*?\\.[a-zA-Z0-9]*?$", url.path)) {
                File("${pathConfig.resourcesDirPath(config)}/static/$filePath")
            } else {
                if (ReUtil.isMatch(".*?(\\.html)$", it.filename!!)) {
                    File("${pathConfig.resourcesDirPath(config)}/templates/$filePath")
                } else {
                    File("")
                }
            }
            if (destination.name.isNotEmpty()) {
                FileUtils.copyURLToFile(url, destination)
            }
        }
        val unBindMenus = project.roles.flatMap { it.menus }
            .filter { it.parentId == 0 && !it.bind }
            .distinctBy { it.name }
        unBindMenus.forEach {
            ioUtil.writeTemplate(emptyPageTemplate, it,
                "${pathConfig.layUIPageDirPath(config)}${it.href}.html")
        }
        ioUtil.writeTemplate(indexControllerTemplate, mapOf(
            "packageName" to project.config.controllerPackagePath(),
            "dependencies" to listOf(project.config.entityPackagePath(), project.config.serviceImplPackagePath(),
                project.config.commonPackagePath(), project.config.requestPackagePath()),
            "unBindMenus" to unBindMenus),
            "${pathConfig.controllerDir(config)}/IndexController.java")
        val entities = backendParser.parseEntities(project.tables)
        entities.forEach { entity ->
            val operations = entity.table.permissions.asSequence().map { it.operations }
                .flatten()
                .distinctBy { it.value }
                .filter { it.custom }
                .toList()
            ioUtil.writeTemplate(controllerTemplate, mapOf(
                "entity" to entity,
                "operations" to operations,
                "packageName" to config.controllerPackagePath(),
                "dependencies" to listOf(config.entityPackagePath(), config.dtoPackagePath(), config.serviceImplPackagePath(),
                    config.commonPackagePath(), config.requestPackagePath())),
                "${pathConfig.controllerDir(config)}/${entity.name.capitalize()}Controller.java")
        }

        pages.forEach {
            it.entity!!
            ioUtil.writeTemplate(addPageTemplate, it,
                "${pathConfig.layUIPageDirPath(config)}/${it.entity.name}/add.html")
            ioUtil.writeTemplate(editPageTemplate, it,
                "${pathConfig.layUIPageDirPath(config)}/${it.entity.name}/edit.html")
            ioUtil.writeTemplate(detailPageTemplate, it,
                "${pathConfig.layUIPageDirPath(config)}/${it.entity.name}/detail.html")
            ioUtil.writeTemplate(tablePageTemplate, it,
                "${pathConfig.layUIPageDirPath(config)}/${it.entity.name}/table.html")
        }
        return project
    }

    override fun mkdirs(config: Config) {
        super.mkdirs(config)

        val pageDir = File(pathConfig.layUIPageDirPath(config))
        pageDir.mkdirs()

        val staticDir = File(pathConfig.layUIStaticDirPath(config))
        staticDir.mkdir()
    }

    private fun createOtherDirs(dirs: List<String>, config: Config) {
        dirs.forEach {
            File("${pathConfig.layUIPageDirPath(config)}/$it").mkdirs()
        }
    }
}
