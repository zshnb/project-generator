package com.zshnb.projectgenerator.web.controller

import com.zshnb.projectgenerator.generator.entity.*
import com.zshnb.projectgenerator.web.common.ListResponse
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/api/page")
class PageController {
    private val formItemTypes = listOf(
        FormItemType("输入框", InputFormItem::class.qualifiedName!!),
        FormItemType("下拉选择框", SelectFormItem::class.qualifiedName!!),
        FormItemType("单选框", RadioFormItem::class.qualifiedName!!),
        FormItemType("文本域", TextAreaFormItem::class.qualifiedName!!)
    )
    @GetMapping("/form-items")
    fun listFormItems(): ListResponse<FormItemType> {
        return ListResponse.ok(formItemTypes)
    }

    data class FormItemType(val name: String, val className: String)
}