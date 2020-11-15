package com.zshnb.web.serviceImpl

import com.zshnb.codegenerator.entity.ColumnType
import org.springframework.stereotype.Service

@Service
class ColumnService {
    fun listColumnType(): List<String> = ColumnType.values().map { it.description }
}