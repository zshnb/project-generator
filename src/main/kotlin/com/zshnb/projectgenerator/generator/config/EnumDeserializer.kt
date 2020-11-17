package com.zshnb.projectgenerator.generator.config

import com.google.gson.*
import com.zshnb.projectgenerator.generator.entity.*
import org.springframework.stereotype.Component
import java.lang.reflect.Type

@Component
class ColumnTypeDeserializer : JsonDeserializer<ColumnType> {
    override fun deserialize(json: JsonElement?, type: Type?, context: JsonDeserializationContext?): ColumnType {
        return ColumnType.fromDescription(json!!.asString)
    }
}

@Component
class ProjectTypeDeserializer: JsonDeserializer<ProjectType> {
    override fun deserialize(json: JsonElement?, type: Type?, context: JsonDeserializationContext?): ProjectType {
        return ProjectType.fromCode(json!!.asInt)
    }
}
