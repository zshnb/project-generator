package com.zshnb.projectgenerator.generator.config

import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import org.springframework.context.annotation.*

@Configuration
class MoshiConfig {
    @Bean
    fun moshi(): Moshi =
        Moshi.Builder()
            .addLast(KotlinJsonAdapterFactory())
            .build()
}