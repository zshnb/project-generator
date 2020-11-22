package com.zshnb.projectgenerator.generator.generator

import cn.hutool.core.util.ReUtil
import com.zshnb.projectgenerator.generator.constant.*
import com.zshnb.projectgenerator.generator.entity.Config
import com.zshnb.projectgenerator.generator.extension.*
import com.zshnb.projectgenerator.generator.parser.BackendParser
import com.zshnb.projectgenerator.web.config.ProjectConfig
import freemarker.template.Configuration
import org.apache.commons.io.FileUtils
import org.springframework.core.io.support.*
import org.springframework.stereotype.Component
import java.io.*

@Component
class LayuiGenerator(
    private val backendParser: BackendParser,
    private val pathConstant: PathConstant,
    private val configuration: Configuration,
    projectConfig: ProjectConfig,
) : BaseGenerator(backendParser, pathConstant, projectConfig, configuration) {
    override fun generateProject(json: String) {
        super.generateProject(json)
        val controllerTemplate = configuration.getTemplate(BackendFreeMarkerFileConstant.LAYUI_CONTROLLER_TEMPLATE)
        val indexControllerTemplate =
            configuration.getTemplate(BackendFreeMarkerFileConstant.LAYUI_INDEX_CONTROLLER_TEMPLATE)
        val addPageTemplate = configuration.getTemplate(FrontendFreeMarkerFileConstant.LAY_UI_ADD_PAGE)
        val editPageTemplate = configuration.getTemplate(FrontendFreeMarkerFileConstant.LAY_UI_EDIT_PAGE)
        val detailPageTemplate = configuration.getTemplate(FrontendFreeMarkerFileConstant.LAY_UI_DETAIL_PAGE)
        val tablePageTemplate = configuration.getTemplate(FrontendFreeMarkerFileConstant.LAY_UI_TABLE_PAGE)

        val project = backendParser.parseProject(json)
        createOtherDirs(project.pages.map { it.name })
        val resourceResolver = PathMatchingResourcePatternResolver()
        val resources = resourceResolver.getResources("/templates/layui/**")
        resources.filter { ReUtil.isMatch(".*?\\.[a-zA-Z]*?", it.filename!!) }
            .forEach {
                val url = it.url
                val filePath = url.path.substring(url.path.indexOf("layui") + 5)
                if (!filePath.contains("page")) {
                    val destination = File("${pathConstant.resourcesDirPath()}/templates/$filePath")
                    FileUtils.copyURLToFile(url, destination)
                }
            }

        val indexControllerWriter =
            BufferedWriter(FileWriter("${project.config.controllerDir(pathConstant)}/IndexController.java"))
        indexControllerTemplate.process(mapOf(
            "packageName" to project.config.controllerPackagePath(),
            "dependencies" to listOf(project.config.entityPackagePath(), project.config.serviceImplPackagePath(),
                project.config.commonPackagePath(), project.config.requestPackagePath())), indexControllerWriter)
        indexControllerWriter.close()

        project.controllers.forEach {
            val writer = BufferedWriter(FileWriter("${project.config.controllerDir(pathConstant)}/${it.name.capitalize()}Controller.java"))
            controllerTemplate.process(it, writer)
            writer.close()
        }

        project.pages.forEach {
            val addPageWriter = BufferedWriter(FileWriter("${pathConstant.layUIPageDirPath()}/${it.name}/add.html"))
            addPageTemplate.process(it, addPageWriter)
            addPageWriter.close()

            val editPageWriter = BufferedWriter(FileWriter("${pathConstant.layUIPageDirPath()}/${it.name}/edit.html"))
            editPageTemplate.process(it, editPageWriter)
            editPageWriter.close()

            val detailPageWriter =
                BufferedWriter(FileWriter("${pathConstant.layUIPageDirPath()}/${it.name}/detail.html"))
            detailPageTemplate.process(it, detailPageWriter)
            detailPageWriter.close()

            val tablePageWriter = BufferedWriter(FileWriter("${pathConstant.layUIPageDirPath()}/${it.name}/table.html"))
            tablePageTemplate.process(it, tablePageWriter)
            tablePageWriter.close()
        }
    }

    override fun mkdirs(config: Config) {
        super.mkdirs(config)
        val templateDir = File(pathConstant.resourcesDirPath())
        templateDir.mkdirs()

        val pageDir = File(pathConstant.layUIPageDirPath())
        pageDir.mkdirs()
    }

    private fun createOtherDirs(dirs: List<String>) {
        dirs.forEach {
            File("${pathConstant.layUIPageDirPath()}/$it").mkdirs()
        }
    }
}