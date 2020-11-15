package com.zshnb.projectgenerator.web.serviceImpl

import com.zshnb.projectgenerator.generator.entity.ColumnType
import org.springframework.stereotype.Service

@Service
class ColumnService {
    fun listColumnType(): List<String> = ColumnType.values().map { it.description }
}