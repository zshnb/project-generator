package com.zshnb.projectgenerator.generator.util

import com.zshnb.projectgenerator.generator.entity.*
import com.zshnb.projectgenerator.generator.entity.FieldType.*
import com.zshnb.projectgenerator.generator.entity.FieldType.Boolean
import org.springframework.stereotype.Component

@Component
class TypeUtil {
    private val typeMap = mapOf(
        ColumnType.INT to FieldType.INT,
        ColumnType.VARCHAR to FieldType.STRING,
        ColumnType.TEXT to FieldType.STRING,
        ColumnType.DOUBLE to DOUBLE,
        ColumnType.DATE_TIME to LOCAL_DATE_TIME,
        ColumnType.TINY_INT to Boolean
    )

    fun convertColumnTypeToFieldType(columnType: ColumnType): FieldType = typeMap.getValue(columnType)
}