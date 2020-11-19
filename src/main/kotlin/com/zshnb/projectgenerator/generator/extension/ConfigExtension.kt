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

fun Config.rootDir(pathConstant: PathConstant) = "${pathConstant.srcDirPath()}/${rootPackageName.replace('.', '/')}"
fun Config.entityDir(pathConstant: PathConstant) = "${pathConstant.srcDirPath()}/${rootPackageName.replace('.', '/')}/$entityPackageName"
fun Config.serviceDir(pathConstant: PathConstant) = "${pathConstant.srcDirPath()}/${rootPackageName.replace('.', '/')}/$servicePackageName"
fun Config.serviceImplDir(pathConstant: PathConstant) = "${pathConstant.srcDirPath()}/${rootPackageName.replace('.', '/')}/$serviceImplPackageName"
fun Config.mapperDir(pathConstant: PathConstant) = "${pathConstant.srcDirPath()}/${rootPackageName.replace('.', '/')}/$mapperPackageName"
fun Config.controllerDir(pathConstant: PathConstant) = "${pathConstant.srcDirPath()}/${rootPackageName.replace('.', '/')}/$controllerPackageName"
fun Config.requestDir(pathConstant: PathConstant) = "${pathConstant.srcDirPath()}/${rootPackageName.replace('.', '/')}/request"
fun Config.commonDir(pathConstant: PathConstant) = "${pathConstant.srcDirPath()}/${rootPackageName.replace('.', '/')}/common"
fun Config.configDir(pathConstant: PathConstant) = "${pathConstant.srcDirPath()}/${rootPackageName.replace('.', '/')}/config"
fun Config.dtoDir(pathConstant: PathConstant) = "${pathConstant.srcDirPath()}/${rootPackageName.replace('.', '/')}/dto"
fun Config.xmlDir(pathConstant: PathConstant) = "${pathConstant.resourcesDirPath()}/xml"
