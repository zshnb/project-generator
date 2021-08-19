package com.zshnb.projectgenerator.generator.config

import com.squareup.moshi.*
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import com.zshnb.projectgenerator.generator.entity.swing.FrameItem
import com.zshnb.projectgenerator.generator.entity.web.*
import org.springframework.context.annotation.*

@Configuration
class MoshiConfig {
    @Bean
    fun moshi(formItemAdapter: FormItemAdapter,
              associateResultColumnAdapter: AssociateResultColumnAdapter,
              frameItemAdapter: SwingFrameItemAdapter): Moshi =
        Moshi.Builder()
            .addLast(KotlinJsonAdapterFactory())
            .add(FormItem::class.java, formItemAdapter)
            .add(FrameItem::class.java, frameItemAdapter)
            .add(AssociateResultColumn::class.java, associateResultColumnAdapter).build()
}
