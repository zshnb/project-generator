package com.zshnb.projectgenerator.generator.config

import com.squareup.moshi.*
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import com.zshnb.projectgenerator.generator.entity.FormItem
import org.springframework.context.annotation.*

@Configuration
class MoshiConfig {
    @Bean
    fun moshi(formItemAdapter: FormItemAdapter): Moshi =
        Moshi.Builder()
            .addLast(KotlinJsonAdapterFactory())
            .add(FormItem::class.java, formItemAdapter)
            .build()
}