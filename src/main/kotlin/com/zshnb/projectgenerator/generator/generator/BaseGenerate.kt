package com.zshnb.projectgenerator.generator.generator

import com.zshnb.projectgenerator.generator.entity.Project

interface BaseGenerate {
    fun generateProject(project: Project): Project
}