package com.zshnb.projectgenerator.web.config

import com.zshnb.projectgenerator.generator.entity.web.WebProjectType
import org.springframework.context.annotation.Configuration
import org.springframework.core.convert.converter.Converter
import java.lang.IllegalArgumentException

@Configuration
class ProjectTypeEnumConverter : Converter<Int, WebProjectType> {
    private val codeToType = WebProjectType.values().associateBy { it.code }

    override fun convert(p0: Int): WebProjectType {
        return codeToType[p0] ?: throw IllegalArgumentException("$p0 has no enum")
    }
}