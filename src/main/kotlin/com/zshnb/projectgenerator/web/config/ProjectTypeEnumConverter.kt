package com.zshnb.projectgenerator.web.config

import com.zshnb.projectgenerator.generator.entity.ProjectType
import org.springframework.context.annotation.Configuration
import org.springframework.core.convert.converter.Converter
import java.lang.IllegalArgumentException

@Configuration
class ProjectTypeEnumConverter : Converter<Int, ProjectType> {
    private val codeToType = ProjectType.values().associateBy { it.code }

    override fun convert(p0: Int): ProjectType {
        return codeToType[p0] ?: throw IllegalArgumentException("$p0 has no enum")
    }
}