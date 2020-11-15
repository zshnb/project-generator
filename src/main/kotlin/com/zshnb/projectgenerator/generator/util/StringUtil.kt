package com.zshnb.codegenerator.util

fun String.toCamelCase(): String =
    this.split('_').joinToString("") { it.capitalize() }.decapitalize()
