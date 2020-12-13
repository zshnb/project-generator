package com.zshnb.projectgenerator.generator.util

import com.zshnb.projectgenerator.generator.entity.*
import com.zshnb.projectgenerator.generator.entity.FieldType.*
import com.zshnb.projectgenerator.generator.entity.FieldType.Boolean
import org.springframework.stereotype.Component

@Component
class TypeUtil {
    private val typeMap = mapOf(
        ColumnType.INT to INT,
        ColumnType.VARCHAR to STRING,
        ColumnType.TEXT to STRING,
        ColumnType.DOUBLE to DOUBLE,
        ColumnType.DATETIME to LOCAL_DATE_TIME,
        ColumnType.TINYINT to Boolean
    )

    fun convertColumnTypeToFieldType(columnType: ColumnType): FieldType = typeMap.getValue(columnType)
}