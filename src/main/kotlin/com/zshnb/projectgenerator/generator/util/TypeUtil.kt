package com.zshnb.codegenerator.util

import com.zshnb.codegenerator.entity.*
import com.zshnb.codegenerator.entity.FieldType.*
import com.zshnb.codegenerator.entity.FieldType.Boolean
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