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

fun Config.rootDir(config: Config) = "${PathConstant.javaSrcDirPath(config)}/${rootPackageName.replace('.', '/')}"
fun Config.entityDir(config: Config) = "${PathConstant.javaSrcDirPath(config)}/${rootPackageName.replace('.', '/')}/$entityPackageName"
fun Config.serviceDir(config: Config) = "${PathConstant.javaSrcDirPath(config)}/${rootPackageName.replace('.', '/')}/$servicePackageName"
fun Config.serviceImplDir(config: Config) = "${PathConstant.javaSrcDirPath(config)}/${rootPackageName.replace('.', '/')}/$serviceImplPackageName"
fun Config.mapperDir(config: Config) = "${PathConstant.javaSrcDirPath(config)}/${rootPackageName.replace('.', '/')}/$mapperPackageName"
fun Config.controllerDir(config: Config) = "${PathConstant.javaSrcDirPath(config)}/${rootPackageName.replace('.', '/')}/$controllerPackageName"
fun Config.requestDir(config: Config) = "${PathConstant.javaSrcDirPath(config)}/${rootPackageName.replace('.', '/')}/request"
fun Config.commonDir(config: Config) = "${PathConstant.javaSrcDirPath(config)}/${rootPackageName.replace('.', '/')}/common"
fun Config.configDir(config: Config) = "${PathConstant.javaSrcDirPath(config)}/${rootPackageName.replace('.', '/')}/config"
fun Config.dtoDir(config: Config) = "${PathConstant.javaSrcDirPath(config)}/${rootPackageName.replace('.', '/')}/dto"
fun Config.xmlDir(config: Config) = "${PathConstant.resourcesDirPath(config)}/xml"
