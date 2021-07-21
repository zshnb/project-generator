package com.zshnb.projectgenerator.generator.entity.c

data class Entity(val name: String,
                  val comment: String,
                  val fields: List<Field>)

data class Field(val name: String,
                 val type: FieldType,
                 val comment: String,
                 val searchable: Boolean)

enum class FieldType(val description: String) {
    INT("int"),
    STRING("char[255]"),
    CHAR("char")
}