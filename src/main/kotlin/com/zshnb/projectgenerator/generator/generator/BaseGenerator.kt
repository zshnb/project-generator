package com.zshnb.projectgenerator.generator.generator

import com.zshnb.projectgenerator.generator.entity.Project
import com.zshnb.projectgenerator.generator.entity.web.Config

interface BaseGenerator {
    fun generateProject(project: Project)

    fun mkdirs(config: Config) {}
}