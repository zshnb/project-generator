package com.zshnb.projectgenerator.generator.entity

class Entity(val packageName: String,
             val name: String,
             val table: Table,
             val fields: List<Field>)

data class Field(val name: String,
                 val column: Column,
                 val type: String)

data class Dto(val entity: Entity, val packageName: String)

enum class FieldType(val code: Int, val description: String) {
    INT(1, "Integer"),
    STRING(2, "String"),
    LOCAL_DATE_TIME(3, "LocalDateTime"),
    DOUBLE(4, "Double"),
    Boolean(5, "Boolean"),
    LOCAL_DATE(6, "LocalDate"),
    BIG_DECIMAL(7, "BigDecimal")
}
