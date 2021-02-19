package com.zshnb.projectgenerator.generator.util

import freemarker.template.Template
import org.springframework.stereotype.Component
import java.io.*
import java.nio.charset.StandardCharsets

@Component
class IOUtil {
    fun writeTemplate(template: Template, obj: Any, fileName: String) {
        val writer = OutputStreamWriter(FileOutputStream(fileName), StandardCharsets.UTF_8)
        template.process(obj, writer)
        writer.close()
    }
}
