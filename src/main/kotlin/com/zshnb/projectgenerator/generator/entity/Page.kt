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

open class FormItem(val name: String) {}

class InputFormItem(name: String = "",
                    val comment: String = "",
                    val require: Boolean = false) : FormItem(name)

class DateTimeFormItem(name: String = "",
                       val comment: String = "",
                       val require: Boolean = false) : FormItem(name)

class SelectFormItem(name: String = "",
                     val comment: String = "",
                     val require: Boolean = false,
                     val options: List<Option> = emptyList()) : FormItem(name)

class RadioFormItem(name: String = "",
                    val comment: String = "",
                    val require: Boolean = false,
                    val options: List<Option> = emptyList()) : FormItem(name)

class TextAreaFormItem(name: String = "",
                       val comment: String = "",
                       val require: Boolean = false) : FormItem(name)

data class Option(val title: String, val value: String)
