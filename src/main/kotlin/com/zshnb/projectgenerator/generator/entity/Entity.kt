package com.zshnb.projectgenerator.generator.entity

class Entity(val packageName: String,
             val name: String,
             val fields: List<Field>) {
}

data class Field(val name: String,
                 val type: String,
                 val comment: String? = "",
                 val primary: Boolean? = false)

enum class FieldType(val code: Int, val description: String) {
    INT(1, "Integer"),
    STRING(2, "String"),
    LOCAL_DATE_TIME(3, "LocalDateTime"),
    DOUBLE(4, "Double"),
    Boolean(5, "Boolean")
}
