package com.zshnb.projectgenerator.generator.entity

/**
 * @param form 表单组件
 * @param entity 页面所代表的实体
 * */
class Page(val entity: Entity?,
           val form: FormComponent?,
           val table: TableComponent?) {
}

/**
 * 表单组件
 * */
class FormComponent(val formItems: List<FormItem>)

/**
 * 表格组建
 * */
class TableComponent(val fields: List<TableField>)

/**
 * 表格的列
 * @param field 列对应实体的属性域
 * @param title 列在表格页面上显示的描述
 * */
data class TableField(val title: String,
                      val formItemClassName: String,
                      val field: Field?,
                      val mappings: List<Mapping>?)

/**
 * 表格列值与显示内容的映射
 * @param source 值，存在数据库里的数据
 * @param target 内容，显示在网页上的数据
 * */
data class Mapping(val source: Any, val target: Any)

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

class ImageFormItem(field: Field?,
                    formItemClassName: String,
                    require: Boolean) : FormItem(field, formItemClassName, require)

data class Option(val title: String, val value: String)
