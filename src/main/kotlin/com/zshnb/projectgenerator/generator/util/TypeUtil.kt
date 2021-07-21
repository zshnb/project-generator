package com.zshnb.projectgenerator.generator.util

import com.zshnb.projectgenerator.generator.entity.*
import com.zshnb.projectgenerator.generator.entity.web.*
import com.zshnb.projectgenerator.generator.entity.web.FieldType.*
import com.zshnb.projectgenerator.generator.entity.web.FieldType.Boolean
import org.springframework.stereotype.Component

@Component
class TypeUtil {
    private val typeMap = mapOf(
        ColumnType.INT to INT,
        ColumnType.VARCHAR to STRING,
        ColumnType.TEXT to STRING,
        ColumnType.DOUBLE to DOUBLE,
        ColumnType.DATETIME to LOCAL_DATE_TIME,
        ColumnType.TINYINT to Boolean,
        ColumnType.LOCAL_DATE to LOCAL_DATE,
        ColumnType.DECIMAL to BIG_DECIMAL
    )

    fun convertColumnTypeToFieldType(columnType: ColumnType): FieldType = typeMap.getValue(columnType)
}
