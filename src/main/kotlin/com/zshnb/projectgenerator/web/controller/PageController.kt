package com.zshnb.projectgenerator.web.controller

import com.zshnb.projectgenerator.generator.entity.FormItem
import com.zshnb.projectgenerator.web.common.ListResponse
import org.reflections.Reflections
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/api/page")
class PageController {
    @GetMapping("/form-items")
    fun listFormItems(): ListResponse<String> {
        val kclass = FormItem::class.java
        val reflections = Reflections("com.zshnb.projectgenerator.generator.entity")
        val types = reflections.getSubTypesOf(kclass)
        return ListResponse.ok(types.map { it.name })
    }
}