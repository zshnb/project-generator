package com.zshnb.projectgenerator.generator.generator

import cn.hutool.core.util.ReUtil
import com.zshnb.projectgenerator.generator.constant.*
import com.zshnb.projectgenerator.generator.entity.*
import com.zshnb.projectgenerator.generator.extension.*
import com.zshnb.projectgenerator.generator.parser.*
import com.zshnb.projectgenerator.generator.util.*
import freemarker.template.Configuration
import org.apache.commons.io.FileUtils
import org.springframework.core.io.support.*
import org.springframework.stereotype.Component
import java.io.*

@Component
class LayuiGenerator(private val backendParser: BackendParser,
                     private val configuration: Configuration,
                     private val frontendParser: FrontendParser,
                     private val ioUtil: IOUtil) : BaseGenerator(backendParser, ioUtil, configuration) {
    override fun generateProject(json: String) {
        super.generateProject(json)
        val controllerTemplate = configuration.getTemplate(BackendFreeMarkerFileConstant.LAYUI_CONTROLLER_TEMPLATE)
        val indexControllerTemplate =
            configuration.getTemplate(BackendFreeMarkerFileConstant.LAYUI_INDEX_CONTROLLER_TEMPLATE)
        val addPageTemplate = configuration.getTemplate(FrontendFreeMarkerFileConstant.LAY_UI_ADD_PAGE)
        val editPageTemplate = configuration.getTemplate(FrontendFreeMarkerFileConstant.LAY_UI_EDIT_PAGE)
        val detailPageTemplate = configuration.getTemplate(FrontendFreeMarkerFileConstant.LAY_UI_DETAIL_PAGE)
        val tablePageTemplate = configuration.getTemplate(FrontendFreeMarkerFileConstant.LAY_UI_TABLE_PAGE)
        val emptyPageTemplate = configuration.getTemplate(FrontendFreeMarkerFileConstant.LAY_UI_EMPTY_PAGE)

        val project = backendParser.parseProject(json)
        val pages = frontendParser.parsePages(project)
        createOtherDirs(pages.map { it.entity!!.name }, project.config)
        val resourceResolver = PathMatchingResourcePatternResolver()
        val resources = resourceResolver.getResources("/templates/layui/**")

        resources.forEach {
            val url = it.url
            val destination = if (ReUtil.isMatch(".*?(css|images|js|lib).*?\\.[a-zA-Z0-9]*?$", it.url.path)) {
                val filePath = url.path.substring(url.path.indexOf("layui") + 5)
                File("${PathConstant.resourcesDirPath(project.config)}/static/$filePath")
            } else {
                val filePath = url.path.substring(url.path.indexOf("layui") + 5)
                if (ReUtil.isMatch(".*?(\\.html)$", it.filename!!)) {
                    File("${PathConstant.resourcesDirPath(project.config)}/templates/$filePath")
                } else {
                    File("")
                }
            }
            if (destination.name.isNotEmpty()) {
                FileUtils.copyURLToFile(url, destination)
            }
        }
        // fixme 有bug，前端没有覆盖修改的菜单项
        val unBindMenus = project.roles.flatMap { it.menus }
            .filter { it.parentId == 0 && !it.bind }
        unBindMenus.forEach {
            ioUtil.writeTemplate(emptyPageTemplate, it,
                "${PathConstant.layUIPageDirPath(project.config)}${it.href}.html")
        }
        ioUtil.writeTemplate(indexControllerTemplate, mapOf(
            "packageName" to project.config.controllerPackagePath(),
            "dependencies" to listOf(project.config.entityPackagePath(), project.config.serviceImplPackagePath(),
                project.config.commonPackagePath(), project.config.requestPackagePath()),
            "unBindMenus" to unBindMenus
        ), "${project.config.controllerDir()}/IndexController.java")

        project.controllers.forEach {
            ioUtil.writeTemplate(controllerTemplate, it,
                "${project.config.controllerDir()}/${it.name.capitalize()}Controller.java")
        }

        pages.forEach {
            it.entity!!
            ioUtil.writeTemplate(addPageTemplate, it,
                "${PathConstant.layUIPageDirPath(project.config)}/${it.entity.name}/add.html")
            ioUtil.writeTemplate(editPageTemplate, it,
                "${PathConstant.layUIPageDirPath(project.config)}/${it.entity.name}/edit.html")
            ioUtil.writeTemplate(detailPageTemplate, it,
                "${PathConstant.layUIPageDirPath(project.config)}/${it.entity.name}/detail.html")
            ioUtil.writeTemplate(tablePageTemplate, it,
                "${PathConstant.layUIPageDirPath(project.config)}/${it.entity.name}/table.html")
        }
    }

    override fun mkdirs(config: Config) {
        super.mkdirs(config)

        val pageDir = File(PathConstant.layUIPageDirPath(config))
        pageDir.mkdirs()

        val staticDir = File(PathConstant.layUIStaticDirPath(config))
        staticDir.mkdir()
    }

    private fun createOtherDirs(dirs: List<String>, config: Config) {
        dirs.forEach {
            File("${PathConstant.layUIPageDirPath(config)}/$it").mkdirs()
        }
    }
}
