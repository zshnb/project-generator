package com.zshnb.projectgenerator.web.serviceImpl

import com.zshnb.projectgenerator.generator.entity.web.ColumnType
import org.springframework.stereotype.Service

@Service
class ColumnServiceImpl {
    fun listColumnType(): List<String> = ColumnType.values().map { it.description }
}