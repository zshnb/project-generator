package com.zshnb.projectgenerator.generator.extension

/**
* 把包名字符串和 "packageName"一起放进map，在ftl文件中使用
* */
fun String.packageName(): Map<String, String> = mapOf("packageName" to this)
