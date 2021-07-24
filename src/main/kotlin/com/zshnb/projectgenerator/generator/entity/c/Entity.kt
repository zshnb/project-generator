package com.zshnb.projectgenerator.generator.entity.c

import com.squareup.moshi.Json

data class Entity(val name: String,
                  val comment: String,
                  val fields: List<Field>,
                  val menus: List<Menu>,
                  val fileOperation: Boolean)

data class Field(val name: String,
                 val type: FieldType,
                 val comment: String,
                 val searchable: Boolean = false)

enum class FieldType(val description: String) {
    INT("int"),
    STRING("string"),
    CHAR("char")
}