package com.zshnb.projectgenerator.generator.entity

data class Table(val name: String, var columns: List<Column>) {
}

data class Column(val name: String,
                  val type: ColumnType,
                  val comment: String = "",
                  val length: Int = 0,
                  val primary: Boolean = false) {
    override fun equals(other: Any?): Boolean {
        other as Column
        return name == other.name && type == other.type
    }

    override fun hashCode(): Int {
        var result = name.hashCode()
        result = 31 * result + type.hashCode()
        return result
    }
}

enum class ColumnType(val code: Int, val description: String) {
    INT(1, "int"),
    TINY_INT(2, "tinyint"),
    VARCHAR(3, "varchar"),
    DATE_TIME(4, "datetime"),
    DOUBLE(5, "double"),
    TEXT(6, "text")
    ;

    companion object {
        fun fromDescription(description: String): ColumnType {
            return when(description) {
                "int" -> INT
                "tinyint" -> TINY_INT
                "varchar" -> VARCHAR
                "datetime" -> DATE_TIME
                "double" -> DOUBLE
                "text" -> TEXT
                else -> throw Exception()
            }
        }
    }
}