package com.zshnb.projectgenerator.generator.entity

import com.squareup.moshi.Json
import com.zshnb.projectgenerator.generator.entity.ColumnType.*

/**
 * @param searchable 表是否有支持搜索的列
 * */
data class Table(val name: String = "",
                 var columns: List<Column> = emptyList(),
                 val permissions: List<TablePermission> = emptyList(),
                 val searchable: Boolean = false,
                 val enablePage: Boolean = false,
                 val associate: Associate? = null)

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

        return true
    }

    override fun hashCode(): Int {
        return name.hashCode()
    }
}

data class Associate(val targetTableName: String,
                     val targetColumnName: String,
                     val sourceColumnName: String,
                     val associateResultColumns: List<AssociateResultColumn>,
                     val formItemName: String)

data class AssociateResultColumn(val originColumnName: String,
                                 val aliasColumnName: String,
                                 val tableFieldTitle: String)

/**
 * 角色在表格页面上的权限
 * @param role 角色名
 * @param operations 可以允许的权限集合
 * */
data class TablePermission(val role: String = "", val operations: List<String> = emptyList())

enum class ColumnType(val code: Int, val description: String) {
    @Json(name = "int") INT(1, "int"),
    @Json(name = "tinyint") TINYINT(2, "tinyint"),
    @Json(name = "varchar") VARCHAR(3, "varchar"),
    @Json(name = "datetime") DATETIME(4, "datetime"),
    @Json(name = "double") DOUBLE(5, "double"),
    @Json(name = "text") TEXT(6, "text"),
    @Json(name = "date") LOCAL_DATE(7, "date")
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
                "date" -> LOCAL_DATE
                else -> throw Exception()
            }
        }
    }
}