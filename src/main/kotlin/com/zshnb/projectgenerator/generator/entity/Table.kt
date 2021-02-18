package com.zshnb.projectgenerator.generator.entity

import com.zshnb.projectgenerator.generator.entity.ColumnType.*

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
                  val enableFormItem: Boolean = true,
                  val associate: Associate? = null) {
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

data class Associate(val targetTable: Table,
                     val targetColumn: Column,
                     val associateResultColumns: List<AssociateResultColumn>,
                     val formItemName: String)

data class AssociateResultColumn(val sourceColumnName: String,
                                 val aliasColumnName: String,
                                 val tableFieldTitle: String)

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
    TEXT(6, "text"),
    LOCAL_DATE(7, "date")
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