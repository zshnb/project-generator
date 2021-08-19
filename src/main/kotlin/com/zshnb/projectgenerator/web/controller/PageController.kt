package com.zshnb.projectgenerator.web.controller

import com.zshnb.projectgenerator.generator.entity.swing.*
import com.zshnb.projectgenerator.generator.entity.web.*
import com.zshnb.projectgenerator.web.common.*
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/api/page")
class PageController {
    private val formItemTypes = listOf(
        FormItemType("文本输入框", InputFormItem::class.qualifiedName!!),
        FormItemType("密码输入框", PasswordFormItem::class.qualifiedName!!),
        FormItemType("文本域", TextAreaFormItem::class.qualifiedName!!),
        FormItemType("下拉选择框", SelectFormItem::class.qualifiedName!!),
        FormItemType("单选框", RadioFormItem::class.qualifiedName!!),
        FormItemType("日期选择框", DateFormItem::class.qualifiedName!!),
        FormItemType("日期时间选择框", DateTimeFormItem::class.qualifiedName!!),
        FormItemType("上传文件", FileFormItem::class.qualifiedName!!),
        FormItemType("上传图片", ImageFormItem::class.qualifiedName!!),
    )

    private val frameItemTypes = listOf(
        FrameItemType("文本输入框", TextFieldFrameItem::class.qualifiedName!!),
        FrameItemType("密码输入框", PasswordFrameItem::class.qualifiedName!!),
        FrameItemType("单选框", RadioFrameItem::class.qualifiedName!!),
        FrameItemType("下拉选择框", SelectFrameItem::class.qualifiedName!!),
    )

    @GetMapping("/form-items")
    fun listFormItems(): ListResponse<FormItemType> {
        return ListResponse.ok(formItemTypes)
    }

    @GetMapping("/frame-items")
    fun listFrameItems(): ListResponse<FrameItemType> {
        return ListResponse.ok(frameItemTypes)
    }

    @GetMapping("/option-form-items")
    fun listNeedAddOptionFormItems(): ListResponse<String> {
        return ListResponse.ok(listOf(
            SelectFormItem::class.qualifiedName!!,
            RadioFormItem::class.qualifiedName!!))
    }

    @GetMapping("/option-frame-items")
    fun listNeedAddOptionFrameItems(): ListResponse<String> {
        return ListResponse.ok(listOf(SelectFrameItem::class.qualifiedName!!, RadioFrameItem::class.qualifiedName!!))
    }

    data class FormItemType(val name: String, val className: String)

    data class FrameItemType(val name: String, val className: String)
}