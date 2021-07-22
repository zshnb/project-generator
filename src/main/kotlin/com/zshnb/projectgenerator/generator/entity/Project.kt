package com.zshnb.projectgenerator.generator.entity

import com.zshnb.projectgenerator.generator.entity.c.CProject
import com.zshnb.projectgenerator.generator.entity.web.WebProject

data class Project(val webProject: WebProject? = null,
                   val cProject: CProject? = null)
