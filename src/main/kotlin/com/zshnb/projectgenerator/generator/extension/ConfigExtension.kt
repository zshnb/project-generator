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

fun Config.rootDir() = "${PathConstant.srcDirPath}/${rootPackageName.replace('.', '/')}"
fun Config.entityDir() = "${PathConstant.srcDirPath}/${rootPackageName.replace('.', '/')}/$entityPackageName"
fun Config.serviceDir() = "${PathConstant.srcDirPath}/${rootPackageName.replace('.', '/')}/$servicePackageName"
fun Config.serviceImplDir() = "${PathConstant.srcDirPath}/${rootPackageName.replace('.', '/')}/$serviceImplPackageName"
fun Config.mapperDir() = "${PathConstant.srcDirPath}/${rootPackageName.replace('.', '/')}/$mapperPackageName"
fun Config.controllerDir() = "${PathConstant.srcDirPath}/${rootPackageName.replace('.', '/')}/$controllerPackageName"
fun Config.requestDir() = "${PathConstant.srcDirPath}/${rootPackageName.replace('.', '/')}/request"
fun Config.commonDir() = "${PathConstant.srcDirPath}/${rootPackageName.replace('.', '/')}/common"
fun Config.configDir() = "${PathConstant.srcDirPath}/${rootPackageName.replace('.', '/')}/config"
fun Config.dtoDir() = "${PathConstant.srcDirPath}/${rootPackageName.replace('.', '/')}/dto"
fun Config.xmlDir() = "${PathConstant.resourcesDirPath}/xml"
