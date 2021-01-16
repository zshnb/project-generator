package com.zshnb.projectgenerator.generator.entity

/**
 * @param form 表单组件
 * @param table 表格组件
 * */
class Page(val entity: Entity = Entity(),
           val form: FormComponent) {
}

class FormComponent(val formItems: List<FormItem>)

//class TableComponent(val permissions: List<TablePermission>)

/**
 * 表格的列
 * @param field 列对应实体的属性域
 * @param title 列在表格页面上显示的列描述
 * */
data class TableField(val title: String, val field: Field)

/**
 * 角色在表格页面上的权限
 * @param role 角色名
 * @param operations 可以允许的权限集合
 * */
data class TablePermission(val role: String, val operations: List<String>)

open class FormItem(val field: Field?, val formItemClassName: String, val require: Boolean)

class NullFormItem(field: Field? = null,
                   formItemClassName: String = "",
                   require: Boolean = false) : FormItem(field, formItemClassName, require)

class InputFormItem(field: Field,
                    formItemClassName: String,
                    require: Boolean) : FormItem(field, formItemClassName, require)

class DateTimeFormItem(field: Field,
                       formItemClassName: String,
                       require: Boolean) : FormItem(field, formItemClassName, require)

class TextAreaFormItem(field: Field,
                       formItemClassName: String,
                       require: Boolean) : FormItem(field, formItemClassName, require)

class SelectFormItem(field: Field,
                     val options: List<Option> = emptyList(),
                     formItemClassName: String,
                     require: Boolean) : FormItem(field, formItemClassName, require)

class RadioFormItem(field: Field,
                    val options: List<Option> = emptyList(),
                    formItemClassName: String,
                    require: Boolean) : FormItem(field, formItemClassName, require)

data class Option(val title: String, val value: String)
