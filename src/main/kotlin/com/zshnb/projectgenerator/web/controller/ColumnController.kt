package com.zshnb.projectgenerator.web.controller

import com.zshnb.projectgenerator.web.common.ListResponse
import com.zshnb.projectgenerator.web.serviceImpl.*
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/api/column")
class ColumnController {
    @Autowired
    private lateinit var columnService: ColumnService

    @GetMapping("/types")
    fun listColumnTypes(): ListResponse<String> = ListResponse.ok(columnService.listColumnType())
}