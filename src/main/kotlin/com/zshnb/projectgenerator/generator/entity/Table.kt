package com.zshnb.projectgenerator.generator.entity

import com.zshnb.projectgenerator.generator.entity.ColumnType.INT

/**
 * @param searchable 表是否有支持搜索的列
 * */
data class Table(val name: String,
                 var columns: List<Column>,
                 val permissions: List<TablePermission> = emptyList(),
                 val searchable: Boolean = false,
                 val enablePage: Boolean = false)

data class Column(val name: String = "",
                  val type: ColumnType = INT,
                  val comment: String = "",
                  val length: Int = 0,
                  val primary: Boolean = false,
                  val searchable: Boolean = false,
                  val enableFormItem: Boolean = true) {
    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as Column

        if (name != other.name) return false
        if (type != other.type) return false
        if (comment != other.comment) return false
        if (length != other.length) return false
        if (primary != other.primary) return false
        if (searchable != other.searchable) return false
        if (enableFormItem != other.enableFormItem) return false

        return true
    }

    override fun hashCode(): Int {
        var result = name.hashCode()
        result = 31 * result + type.hashCode()
        result = 31 * result + comment.hashCode()
        result = 31 * result + length
        result = 31 * result + primary.hashCode()
        result = 31 * result + searchable.hashCode()
        result = 31 * result + enableFormItem.hashCode()
        return result
    }
}

/**
 * 角色在表格页面上的权限
 * @param role 角色名
 * @param operations 可以允许的权限集合
 * */
data class TablePermission(val role: String, val operations: List<String>)

enum class ColumnType(val code: Int, val description: String) {
    INT(1, "int"),
    TINYINT(2, "tinyint"),
    VARCHAR(3, "varchar"),
    DATETIME(4, "datetime"),
    DOUBLE(5, "double"),
    TEXT(6, "text")
    ;

    companion object {
        fun fromDescription(description: String): ColumnType {
            return when (description) {
                "int" -> INT
                "tinyint" -> TINYINT
                "varchar" -> VARCHAR
                "datetime" -> DATETIME
                "double" -> DOUBLE
                "text" -> TEXT
                else -> throw Exception()
            }
        }
    }
}