package com.zshnb.projectgenerator.generator.entity.swing

/**
 * 窗口
 * @param items 组件
 * */
data class Frame(val items: List<Item>)

open class Item(val className: String)

/**
 * 输入框组件
 * */
class TextFieldItem(className: String): Item(className)

/**
 * 密码框组件
 * */
class PasswordItem(className: String): Item(className)

/**
 * 单选框组件
 * */
class RadioItem(className: String): Item(className)

/**
 * 下拉框组件
 * */
class SelectItem(className: String): Item(className)