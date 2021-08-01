package com.zshnb.projectgenerator.generator.entity.swing

import com.zshnb.projectgenerator.generator.entity.web.*

/**
 * 窗口
 * @param items 组件
 * */
data class Frame(val entity: Entity?,
                 val items: List<Item>)

open class Item(val className: String,
                val field: Field?)

/**
 * 输入框组件
 * */
class TextFieldItem(className: String,
                    field: Field?) : Item(className, field)

/**
 * 密码框组件
 * */
class PasswordItem(className: String,
                   field: Field?) : Item(className, field)

/**
 * 单选框组件
 * */
class RadioItem(className: String,
                val options: List<Option>,
                field: Field?) : Item(className, field)

/**
 * 下拉框组件
 * */
class SelectItem(className: String,
                 val options: List<Option>,
                 field: Field?) : Item(className, field)