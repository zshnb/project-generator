package com.zshnb.projectgenerator.generator.entity

import com.zshnb.projectgenerator.generator.entity.ColumnType.INT

/**
 * @param form 表单组件
 * @param table 表格组件
 * */
class Page(val name: String,
           val form: FormComponent,
           val table: TableComponent) {
}

class FormComponent(val formItems: List<FormItem>)

class TableComponent(val fields: List<TableField>, val permissions: List<TablePermission>)

data class TableField(val name: String, val title: String)

data class TablePermission(val role: String, val operations: List<String>)

open class FormItem(val name: String, val columnType: ColumnType) {}

class InputFormItem(name: String = "",
                    columnType: ColumnType = INT,
                    val comment: String = "",
                    val require: Boolean = false) : FormItem(name, columnType) {}

class SelectFormItem(name: String = "",
                     columnType: ColumnType = INT,
                     val comment: String = "",
                     val require: Boolean = false,
                     val options: List<Option> = emptyList()) : FormItem(name, columnType) {}

class RadioFormItem(name: String = "",
                    columnType: ColumnType = INT,
                    val comment: String = "",
                    val require: Boolean = false,
                    val options: List<Option> = emptyList()) : FormItem(name, columnType) {}

class TextAreaFormItem(name: String = "",
                       columnType: ColumnType = INT,
                       val comment: String = "",
                       val require: Boolean = false) : FormItem(name, columnType) {}

data class Option(val title: String, val value: String)
