package com.zshnb.projectgenerator.generator.entity

import com.squareup.moshi.Json
import com.zshnb.projectgenerator.generator.entity.ColumnType.*

/**
 * @param searchable 表是否有支持搜索的列
 * @param associates 关联表
 * @param enablePage 是否开启页面
 * @param permissions 表对应的页面上角色拥有的权限
 * */
data class Table(val name: String = "",
                 var columns: List<Column> = emptyList(),
                 val permissions: List<TablePermission> = emptyList(),
                 val searchable: Boolean = false,
                 val enablePage: Boolean = false,
                 val associate: Boolean = false)

/**
 * @param searchable 知否支持搜索
 * @param enableFormItem 是否开启表单项
 * */
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

/**
 * 表示表之间的一对多关联
 * @param targetTableName 被关联的一方表名
 * @param targetColumnName 被关联的一方表的列名
 * @param sourceColumnName 与一方表关联的多方表的列名
 * @param formItemColumnName 在添加修改页面，下拉框里option需要获取的一方表里的列名
 * */
data class Associate(val targetTableName: String,
                     val targetColumnName: String,
                     val sourceColumnName: String,
                     val associateResultColumns: List<AssociateResultColumn>,
                     val formItemColumnName: String)

/**
 * 描述被关联的多方表被筛选出来的列以及供前端显示用的描述
 * @param originColumnName 原本的列名
 * @param aliasColumnName 列别名，用在sql里
 * @param tableFieldTitle 在表格页面显示的描述
 * */
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
    @Json(name = "int")
    INT(1, "int"),

    @Json(name = "tinyint")
    TINYINT(2, "tinyint"),

    @Json(name = "varchar")
    VARCHAR(3, "varchar"),

    @Json(name = "datetime")
    DATETIME(4, "datetime"),

    @Json(name = "double")
    DOUBLE(5, "double"),

    @Json(name = "text")
    TEXT(6, "text"),

    @Json(name = "date")
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
