package com.zshnb.projectgenerator.generator.constant

import java.time.LocalDateTime

class TypeConstant {
    companion object {
        val typeMap = mapOf(
            "int" to Int::class.simpleName!!,
            "double" to Double::class.simpleName!!,
            "varchar" to String::class.simpleName!!,
            "char" to String::class.simpleName!!,
            "datetime" to LocalDateTime::class.simpleName!!,
            "bigint" to Long::class.simpleName!!
        )
    }
}
