package com.zshnb.projectgenerator.generator.entity.swing

import com.zshnb.projectgenerator.generator.entity.web.*

/**
 * 窗口
 * @param frameItems 组件
 * */
data class Frame(val entity: Entity?,
                 val frameItems: List<FrameItem>)

open class FrameItem(val className: String,
                     val field: Field?)

/**
 * 输入框组件
 * */
class TextFieldFrameItem(field: Field?,
                         className: String) : FrameItem(className, field)

/**
 * 密码框组件
 * */
class PasswordFrameItem(field: Field?,
                        className: String) : FrameItem(className, field)

/**
 * 单选框组件
 * */
class RadioFrameItem(field: Field?,
                     className: String,
                     val options: List<Option>) : FrameItem(className, field)

/**
 * 下拉框组件
 * */
class SelectFrameItem(field: Field?,
                      className: String,
                      val options: List<Option>) : FrameItem(className, field)