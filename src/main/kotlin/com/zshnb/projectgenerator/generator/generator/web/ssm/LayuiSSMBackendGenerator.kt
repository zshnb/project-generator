package com.zshnb.projectgenerator.generator.generator.web.ssm

import cn.hutool.core.util.ReUtil
import com.zshnb.projectgenerator.generator.config.PathConfig
import com.zshnb.projectgenerator.generator.constant.*
import com.zshnb.projectgenerator.generator.entity.Project
import com.zshnb.projectgenerator.generator.entity.web.*
import com.zshnb.projectgenerator.generator.extension.*
import com.zshnb.projectgenerator.generator.generator.BaseGenerator
import com.zshnb.projectgenerator.generator.parser.web.*
import com.zshnb.projectgenerator.generator.util.*
import freemarker.template.Configuration
import org.apache.commons.io.FileUtils
import org.springframework.core.io.support.*
import org.springframework.stereotype.Component
import java.io.*
import kotlin.random.Random

@Component
class LayuiSSMBackendGenerator(private val configuration: Configuration,
                               private val pathConfig: PathConfig,
                               private val frontendParser: FrontendParser,
                               private val ioUtil: IOUtil) : BaseGenerator {
    override fun generateProject(project: Project) {
        val webProject = project.webProject!!
        val webXmlTemplate = configuration.getTemplate(SSMBackendFreeMarkerFileConstant.WEB_XML)
        val indexPageTemplate = configuration.getTemplate(FrontendFreeMarkerFileConstant.LAYUI_INDEX_PAGE)
        val loginPageTemplate = configuration.getTemplate(FrontendFreeMarkerFileConstant.LAYUI_LOGIN_PAGES[Random.nextInt(3)])
        val registerPageTemplate = configuration.getTemplate(FrontendFreeMarkerFileConstant.LAYUI_REGISTER_PAGES[Random.nextInt(3)])
        val addPageTemplate = configuration.getTemplate(FrontendFreeMarkerFileConstant.LAYUI_ADD_PAGE)
        val editPageTemplate = configuration.getTemplate(FrontendFreeMarkerFileConstant.LAYUI_EDIT_PAGE)
        val detailPageTemplate = configuration.getTemplate(FrontendFreeMarkerFileConstant.LAYUI_DETAIL_PAGE)
        val tablePageTemplate = configuration.getTemplate(FrontendFreeMarkerFileConstant.LAYUI_TABLE_PAGE)
        val emptyPageTemplate = configuration.getTemplate(FrontendFreeMarkerFileConstant.LAYUI_EMPTY_PAGE)

        val config = webProject.config
        mkdirs(config)
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

        val unBindMenus = webProject.listUnbindMenus()
        unBindMenus.forEach {
            ioUtil.writeTemplate(emptyPageTemplate, mapOf("projectType" to "ssm"),
                "${pathConfig.jspPageDir(config)}/page/${it.href}.jsp")
        }

        ioUtil.writeTemplate(webXmlTemplate, emptyMap<Int, Int>(),
            "${pathConfig.webappDirPath(config)}/WEB-INF/web.xml")
        ioUtil.writeTemplate(indexPageTemplate, mapOf(
            "projectType" to "ssm",
            "theme" to Random.nextInt(10),
            "config" to config),
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
                "entityPackageName" to config.entityPackagePath(),
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
    }

    override fun mkdirs(config: Config) {
        val pageDir = File(pathConfig.jspPageDir(config))
        pageDir.mkdirs()
    }

    private fun createOtherDirs(dirs: List<String>, config: Config) {
        dirs.forEach {
            File("${pathConfig.jspPageDir(config)}/page/$it").mkdirs()
        }
    }
}
