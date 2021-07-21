package com.zshnb.projectgenerator.generator.entity.web

/**
 * @param form 表单组件
 * @param entity 页面所代表的实体
 * */
class Page(val entity: Entity?,
           val form: FormComponent,
           val table: TableComponent) {
}

/**
 * 表单组件
 * */
class FormComponent(val items: List<FormItem>)

/**
 * 表格组件
 * @param fields 列
 * @param operations 操作列所有动作按钮
 * */
class TableComponent(val fields: List<TableField>, val operations: List<Operation>?)

/**
 * 表格的列
 * @param field 列对应实体的属性域
 * @param title 列在表格页面上显示的描述
 * @param formItemClassName 与[FormItem]的一致
 * */
data class TableField(val title: String,
                      val formItemClassName: String?,
                      val field: Field?,
                      val mappings: List<Mapping>?)

/**
 * 表格列值与显示内容的映射
 * @param source 值，存在数据库里的数据
 * @param target 内容，显示在网页上的数据
 * */
data class Mapping(val source: Any, val target: Any)

/**
 * 表单项
 * @param field 列对应实体的属性域
 * @param label 标签
 * @param formItemClassName 具体表单项类
 * @param require 必填项
 * @param
 * */
open class FormItem(val label: String,
                    val formItemClassName: String,
                    val require: Boolean,
                    val field: Field?)

class InputFormItem(field: Field?,
                    formItemClassName: String,
                    require: Boolean,
                    label: String) : FormItem(label, formItemClassName, require, field)

class PasswordFormItem(field: Field?,
                       formItemClassName: String,
                       require: Boolean,
                       label: String) : FormItem(label, formItemClassName, require, field)

class DateTimeFormItem(field: Field?,
                       formItemClassName: String,
                       require: Boolean,
                       label: String) : FormItem(label, formItemClassName, require, field)

class DateFormItem(field: Field?,
                   formItemClassName: String,
                   require: Boolean,
                   label: String) : FormItem(label, formItemClassName, require, field)

class TextAreaFormItem(field: Field?,
                       formItemClassName: String,
                       require: Boolean,
                       label: String) : FormItem(label, formItemClassName, require, field)

class SelectFormItem(field: Field?,
                     val options: List<Option> = emptyList(),
                     formItemClassName: String,
                     require: Boolean,
                     label: String) : FormItem(label, formItemClassName, require, field)

class RadioFormItem(field: Field?,
                    val options: List<Option> = emptyList(),
                    formItemClassName: String,
                    require: Boolean,
                    label: String) : FormItem(label, formItemClassName, require, field)

class FileFormItem(field: Field?,
                   formItemClassName: String,
                   require: Boolean,
                   label: String) : FormItem(label, formItemClassName, require, field)

class ImageFormItem(field: Field?,
                    formItemClassName: String,
                    require: Boolean,
                    label: String) : FormItem(label, formItemClassName, require, field)

data class Option(val title: String, val value: String)
