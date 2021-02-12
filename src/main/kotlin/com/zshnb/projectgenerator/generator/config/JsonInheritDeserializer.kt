package com.zshnb.projectgenerator.generator.config

import com.google.gson.JsonDeserializationContext
import com.google.gson.JsonDeserializer
import com.google.gson.JsonElement
import com.google.gson.JsonPrimitive
import com.zshnb.projectgenerator.generator.entity.*
import org.springframework.stereotype.Component
import java.lang.reflect.Type

/***
 * Gson反序列化继承类
 * */
@Component
class JsonInheritDeserializer : JsonDeserializer<FormItem> {
    override fun deserialize(json: JsonElement?, type: Type?, context: JsonDeserializationContext?): FormItem? {
        val jsonObject = json?.asJsonObject
        val primitive = jsonObject?.get("formItemClassName") as JsonPrimitive
        val className = primitive.asString
        if (className.isNotEmpty()) {
            val clazz = Class.forName(className)
            return context?.deserialize(json, clazz)
        }
        return null
    }
}