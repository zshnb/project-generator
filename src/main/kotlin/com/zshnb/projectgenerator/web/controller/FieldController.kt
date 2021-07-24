package com.zshnb.projectgenerator.web.controller

import com.zshnb.projectgenerator.generator.entity.c.FieldType
import com.zshnb.projectgenerator.web.common.ListResponse
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/api/field")
class FieldController {
    @GetMapping("/types")
    fun listTypes(): ListResponse<String> =
        ListResponse.ok(listOf(FieldType.CHAR.description, FieldType.INT.description, FieldType.STRING.description))
}
