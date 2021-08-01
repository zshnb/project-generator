package com.zshnb.projectgenerator.generator.generator.swing

import com.zshnb.projectgenerator.generator.entity.Project
import com.zshnb.projectgenerator.generator.generator.BaseGenerator
import freemarker.template.Configuration
import org.springframework.stereotype.Component

@Component
class SwingGenerator(private val configuration: Configuration): BaseGenerator {
    override fun generateProject(project: Project): Project {

    }
}
