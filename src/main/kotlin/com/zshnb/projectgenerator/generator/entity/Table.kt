package com.zshnb.projectgenerator.generator.entity

import com.squareup.moshi.Json
import com.zshnb.projectgenerator.generator.entity.ColumnType.INT

/**
 * @param searchable 是否有支持搜索的列
 * @param associate 是否存在关联列
 * @param enablePage 是否开启页面
 * @param permissions 表对应的页面上角色拥有的权限
 * */
data class Table(val name: String,
                 val comment: String,
                 val columns: List<Column> = emptyList(),
                 val permissions: List<Permission> = emptyList(),
                 val searchable: Boolean = false,
                 val enablePage: Boolean = false,
                 val associate: Boolean = false,
                 val bindRoles: List<String> = emptyList(),
                 val bindUser: String? = null)

/**
 * @param searchable 是否支持搜索
 * @param enableFormItem 是否开启表单项
 * @param associate 关联列
 * @param repeatable 是否允许重复
 * @param nullable 列可空
 * */
data class Column(val name: String,
                  val type: ColumnType,
                  val comment: String,
                  val length: String = "0",
                  val primary: Boolean = false,
                  val nullable: Boolean = true,
                  val searchable: Boolean = false,
                  val enableFormItem: Boolean = true,
                  val enableTableField: Boolean = true,
                  val associate: Associate? = null,
                  val repeatable: Boolean = true)

/**
 * 表示表之间的一对多关联
 * @param targetTableName 被关联的一方表名
 * @param targetColumnName 被关联的一方表的列名
 * @param formItemColumnName 在添加修改页面，下拉框里option需要获取的一方表里的列名
 * */
data class Associate(val targetTableName: String,
                     val targetColumnName: String,
                     val associateResultColumns: List<AssociateResultColumn>,
                     val formItemColumnName: String)

/**
 * 描述被关联的多方表被筛选出来的列以及供前端显示用的描述
 * @param originColumnName 原本的列名
 * @param aliasColumnName 列别名，用在sql里
 * @param tableFieldTitle 在表格页面显示的描述
 * */
data class AssociateResultColumn(var originColumnName: String = "",
                                 var aliasColumnName: String = "",
                                 var columnType: ColumnType = INT,
                                 var fieldType: FieldType = FieldType.INT,
                                 var tableFieldTitle: String = "")

enum class ColumnType(val code: Int, val description: String) {
    @Json(name = "int")
    INT(1, "int"),

    @Json(name = "tinyint")
    TINYINT(2, "tinyint"),

    @Json(name = "double")
    DOUBLE(5, "double"),

    @Json(name = "decimal")
    DECIMAL(8, "decimal"),

    @Json(name = "varchar")
    VARCHAR(3, "varchar"),

    @Json(name = "text")
    TEXT(6, "text"),

    @Json(name = "datetime")
    DATETIME(4, "datetime"),

    @Json(name = "date")
    LOCAL_DATE(7, "date")
    ;
}

enum class Database {
    @Json(name = "mysql")
    MYSQL,
    @Json(name = "sqlserver")
    SQLSERVER
}
