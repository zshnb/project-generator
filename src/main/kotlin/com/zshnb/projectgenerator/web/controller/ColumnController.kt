package com.zshnb.projectgenerator.web.controller

import com.zshnb.projectgenerator.generator.entity.c.FieldType
import com.zshnb.projectgenerator.web.common.ListResponse
import com.zshnb.projectgenerator.web.serviceImpl.*
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/api")
class ColumnController {
    @Autowired
    private lateinit var columnServiceImpl: ColumnServiceImpl

    @GetMapping("/column/types")
    fun listColumnTypes(): ListResponse<String> = ListResponse.ok(columnServiceImpl.listColumnType())

    @GetMapping("/field/types")
    fun listCFieldTypes(): ListResponse<String> = ListResponse.ok(FieldType.values().map { it.description })
}