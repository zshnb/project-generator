package com.zshnb.projectgenerator.generator.generator.c

import com.zshnb.projectgenerator.generator.constant.CFreemarkerFileConstant
import com.zshnb.projectgenerator.generator.entity.Project
import com.zshnb.projectgenerator.generator.generator.BaseGenerator
import com.zshnb.projectgenerator.generator.util.IOUtil
import com.zshnb.projectgenerator.web.config.ProjectConfig
import freemarker.template.Configuration
import org.springframework.stereotype.Component

@Component
class CProjectGenerator(private val ioUtil: IOUtil,
                        private val projectConfig: ProjectConfig,
                        private val configuration: Configuration
) : BaseGenerator {
    override fun generateProject(project: Project): Project {
        val cProject = project.cProject!!
        val structTemplate = configuration.getTemplate(CFreemarkerFileConstant.STRUCT_TEMPLATE)
        ioUtil.writeTemplate(structTemplate, cProject, "${projectConfig.projectDir}/${cProject.name}.c")
        return project
    }
}
