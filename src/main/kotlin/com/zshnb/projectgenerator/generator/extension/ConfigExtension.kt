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

fun Config.rootDir() = "${PathConstant.javaSrcDirPath(this)}/${rootPackageName.replace('.', '/')}"
fun Config.entityDir() = "${PathConstant.javaSrcDirPath(this)}/${rootPackageName.replace('.', '/')}/$entityPackageName"
fun Config.serviceDir() = "${PathConstant.javaSrcDirPath(this)}/${rootPackageName.replace('.', '/')}/$servicePackageName"
fun Config.serviceImplDir() = "${PathConstant.javaSrcDirPath(this)}/${rootPackageName.replace('.', '/')}/$serviceImplPackageName"
fun Config.mapperDir() = "${PathConstant.javaSrcDirPath(this)}/${rootPackageName.replace('.', '/')}/$mapperPackageName"
fun Config.controllerDir() = "${PathConstant.javaSrcDirPath(this)}/${rootPackageName.replace('.', '/')}/$controllerPackageName"
fun Config.requestDir() = "${PathConstant.javaSrcDirPath(this)}/${rootPackageName.replace('.', '/')}/request"
fun Config.commonDir() = "${PathConstant.javaSrcDirPath(this)}/${rootPackageName.replace('.', '/')}/common"
fun Config.configDir() = "${PathConstant.javaSrcDirPath(this)}/${rootPackageName.replace('.', '/')}/config"
fun Config.dtoDir() = "${PathConstant.javaSrcDirPath(this)}/${rootPackageName.replace('.', '/')}/dto"
fun Config.xmlDir() = "${PathConstant.resourcesDirPath(this)}/xml"
