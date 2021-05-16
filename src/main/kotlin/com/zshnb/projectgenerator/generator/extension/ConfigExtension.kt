package com.zshnb.projectgenerator.generator.extension

import com.zshnb.projectgenerator.generator.constant.PathConstant
import com.zshnb.projectgenerator.generator.entity.Config

fun Config.entityPackagePath() = "$rootPackageName.$entityPackageName"
fun Config.servicePackagePath() = "$rootPackageName.$servicePackageName"
fun Config.serviceImplPackagePath() = "$rootPackageName.$serviceImplPackageName"
fun Config.mapperPackagePath() = "$rootPackageName.$mapperPackageName"
fun Config.commonPackagePath() = "$rootPackageName.common"
fun Config.controllerPackagePath() = "$rootPackageName.$controllerPackageName"
fun Config.requestPackagePath() = "$rootPackageName.request"
fun Config.configPackagePath() = "$rootPackageName.config"
fun Config.dtoPackagePath() = "$rootPackageName.dto"
fun Config.exceptionPackagePath() = "$rootPackageName.exception"

fun Config.rootDir() = "${PathConstant.javaSrcDirPath(this)}/${rootPackageName.replace('.', '/')}"
fun Config.entityDir() = "${this.rootDir()}/$entityPackageName"
fun Config.serviceDir() = "${this.rootDir()}/$servicePackageName"
fun Config.serviceImplDir() = "${this.rootDir()}/$serviceImplPackageName"
fun Config.mapperDir() = "${this.rootDir()}/$mapperPackageName"
fun Config.controllerDir() = "${this.rootDir()}/$controllerPackageName"
fun Config.requestDir() = "${this.rootDir()}/request"
fun Config.commonDir() = "${this.rootDir()}/common"
fun Config.configDir() = "${this.rootDir()}/config"
fun Config.exceptionDir() = "${this.rootDir()}/exception"
fun Config.dtoDir() = "${this.rootDir()}/dto"
fun Config.xmlDir() = "${PathConstant.resourcesDirPath(this)}/xml"
