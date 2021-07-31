package com.zshnb.projectgenerator.generator.generator

import com.zshnb.projectgenerator.generator.entity.Project

interface BaseGenerator {
    fun generateProject(project: Project): Project
}