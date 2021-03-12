package com.zshnb.projectgenerator.generator.entity

data class Config(val artifactId: String,
                  val groupId: String,
                  val version: String ,
                  val rootPackageName: String,
                  val entityPackageName: String,
                  val servicePackageName: String,
                  val serviceImplPackageName: String,
                  val mapperPackageName: String,
                  val controllerPackageName: String,
                  val jdbcHost: String,
                  val jdbcPort: Int,
                  val jdbcUser: String,
                  val jdbcPassword: String,
                  val jdbcDatabase: String
)
