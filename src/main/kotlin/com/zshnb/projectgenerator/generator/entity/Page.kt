package com.zshnb.codegenerator.entity

class Page(val name: String,
           val form: FormComponent,
           val table: TableComponent) {
}

class FormComponent(val formItems: List<FormItem>)

class TableComponent(val fields: List<TableField>, val permissions: List<TablePermission>)

data class TableField(val name: String, val title: String)

data class TablePermission(val role: String, val operations: List<String>)

open class FormItem {
    var typeName: String = ""

    init {
        typeName = javaClass.simpleName
    }
}

class InputFormItem(val name: String = "",
                    val comment: String = "",
                    val require: Boolean = false) : FormItem() {
}

class SelectFormItem(
    val name: String = "",
    val comment: String = "",
    val require: Boolean = false,
    val options: List<Option> = emptyList()
)

class RadioFormItem(
    val name: String = "",
    val comment: String = "",
    val require: Boolean = false,
    val options: List<Option> = emptyList()
)

class TextAreaFormItem(
    val name: String = "",
    val comment: String = "",
    val require: Boolean = false
)

data class Option(val title: String, val value: String)
