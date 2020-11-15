package com.zshnb.projectgenerator.generator.config

import com.google.gson.*
import com.zshnb.projectgenerator.generator.entity.*
import org.springframework.context.annotation.*

@Configuration
open class GsonConfig(
    private val columnTypeDeserializer: ColumnTypeDeserializer,
    private val projectTypeDeserializer: ProjectTypeDeserializer,
    private val jsonInheritDeserializer: JsonInheritDeserializer<FormItem>
) {
    @Bean
    open fun gson(): Gson {
        val builder = GsonBuilder()
        builder.registerTypeAdapter(ColumnType::class.java, columnTypeDeserializer)
        builder.registerTypeAdapter(ProjectType::class.java, projectTypeDeserializer)
        builder.registerTypeAdapter(FormItem::class.java, jsonInheritDeserializer)
        return builder.create()
    }
}