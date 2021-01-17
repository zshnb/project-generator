package com.zshnb.projectgenerator.generator.entity

class Service(val packageName: String,
              val entity: Entity,
              val implPackageName: String,
              val dependencies: List<String>) {
}