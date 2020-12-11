package com.zshnb.projectgenerator.generator.entity

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

open class FormItem {}

class InputFormItem(val name: String = "",
                    val comment: String = "",
                    val require: Boolean = false) : FormItem() {}

class SelectFormItem(
    val name: String = "",
    val comment: String = "",
    val require: Boolean = false,
    val options: List<Option> = emptyList()
) : FormItem() {}

class RadioFormItem(
    val name: String = "",
    val comment: String = "",
    val require: Boolean = false,
    val options: List<Option> = emptyList()
) : FormItem() {}

class TextAreaFormItem(
    val name: String = "",
    val comment: String = "",
    val require: Boolean = false
) : FormItem() {}

data class Option(val title: String, val value: String)
