package com.zshnb.projectgenerator.generator.util

fun String.toCamelCase(): String =
    this.split('_').joinToString("") { it.capitalize() }.decapitalize()
