package com.zshnb.projectgenerator.generator.entity

/**
 * @param form 表单组件
 * @param entity 页面所代表的实体
 * */
class Page(val entity: Entity?,
           val form: FormComponent) {
}

class FormComponent(val formItems: List<FormItem>)

/**
 * 表格的列
 * @param field 列对应实体的属性域
 * @param title 列在表格页面上显示的列描述
 * */
data class TableField(val title: String, val field: Field)

open class FormItem(val field: Field?,
                    val formItemClassName: String,
                    val require: Boolean)

class InputFormItem(field: Field?,
                    formItemClassName: String,
                    require: Boolean) : FormItem(field, formItemClassName, require)

class PasswordFormItem(field: Field?,
                       formItemClassName: String,
                       require: Boolean) : FormItem(field, formItemClassName, require)

class DateTimeFormItem(field: Field?,
                       formItemClassName: String,
                       require: Boolean) : FormItem(field, formItemClassName, require)

class DateFormItem(field: Field?,
                   formItemClassName: String,
                   require: Boolean) : FormItem(field, formItemClassName, require)

class TextAreaFormItem(field: Field?,
                       formItemClassName: String,
                       require: Boolean) : FormItem(field, formItemClassName, require)

class SelectFormItem(field: Field?,
                     val options: List<Option> = emptyList(),
                     formItemClassName: String,
                     require: Boolean) : FormItem(field, formItemClassName, require)

class RadioFormItem(field: Field?,
                    val options: List<Option> = emptyList(),
                    formItemClassName: String,
                    require: Boolean) : FormItem(field, formItemClassName, require)

class FileFormItem(field: Field?,
                   formItemClassName: String,
                   require: Boolean) : FormItem(field, formItemClassName, require)

data class Option(val title: String, val value: String)
