package com.zshnb.codegenerator.config

import com.google.gson.JsonDeserializationContext
import com.google.gson.JsonDeserializer
import com.google.gson.JsonElement
import com.google.gson.JsonPrimitive
import org.springframework.stereotype.Component
import java.lang.reflect.Type

@Component
class JsonInheritDeserializer<T> : JsonDeserializer<T> {
    override fun deserialize(json: JsonElement?, type: Type?, context: JsonDeserializationContext?): T {
        val jsonObject = json?.asJsonObject
        val primitive = jsonObject?.get("typeName") as JsonPrimitive
        val className = primitive.asString
        val clazz = Class.forName(className)
        return context?.deserialize(json, clazz)!!
    }
}