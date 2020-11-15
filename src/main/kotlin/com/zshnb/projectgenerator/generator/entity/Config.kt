package com.zshnb.codegenerator.entity

data class Config(val artifactId: String = "",
                  val groupId: String = "",
                  val version: String = "1.0",
                  val rootPackageName: String = "",
                  val entityPackageName: String = "entity",
                  val servicePackageName: String = "service",
                  val serviceImplPackageName: String = "serviceImpl",
                  val mapperPackageName: String = "mapper",
                  val controllerPackageName: String = "controller",
                  val jdbcHost: String = "localhost",
                  val jdbcPort: Int = 3306,
                  val jdbcUser: String = "root",
                  val jdbcPassword: String = "",
                  val jdbcDatabase: String = ""
)