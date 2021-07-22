package com.zshnb.projectgenerator.generator.generator.c

import com.squareup.moshi.Moshi
import com.zshnb.projectgenerator.generator.constant.CFreemarkerFileConstant
import com.zshnb.projectgenerator.generator.entity.Project
import com.zshnb.projectgenerator.generator.entity.c.CProject
import com.zshnb.projectgenerator.generator.generator.BaseGenerate
import com.zshnb.projectgenerator.generator.util.IOUtil
import com.zshnb.projectgenerator.web.config.ProjectConfig
import freemarker.template.Configuration
import org.springframework.stereotype.Component

@Component
class CProjectGenerator(private val moshi: Moshi,
                        private val ioUtil: IOUtil,
                        private val projectConfig: ProjectConfig,
                        private val configuration: Configuration
) : BaseGenerate {
    override fun generateProject(json: String): Project {
        val adapter = moshi.adapter(CProject::class.java)
        val project = adapter.fromJson(json)!!
        val structTemplate = configuration.getTemplate(CFreemarkerFileConstant.STRUCT_TEMPLATE)
        ioUtil.writeTemplate(structTemplate, project, "${projectConfig.projectDir}/${project.name}.c")
        return Project(cProject = project)
    }
}