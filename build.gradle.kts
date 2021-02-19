import org.jetbrains.kotlin.gradle.tasks.KotlinCompile
import org.jetbrains.kotlin.kapt3.base.Kapt.kapt

plugins {
	id("org.springframework.boot") version "2.4.0"
	id("io.spring.dependency-management") version "1.0.10.RELEASE"
	kotlin("jvm") version "1.4.10"
	kotlin("plugin.spring") version "1.4.10"
	kotlin("kapt") version "1.4.30"
}

group = "com.zshnb"
version = "0.0.1"
java.sourceCompatibility = JavaVersion.VERSION_1_8

repositories {
	mavenCentral()
}

dependencies {
	implementation("org.springframework.boot:spring-boot-starter-freemarker")
	implementation("org.springframework.boot:spring-boot-starter-web")
	implementation("com.fasterxml.jackson.module:jackson-module-kotlin")
	implementation("org.jetbrains.kotlin:kotlin-reflect")
    implementation("com.baomidou:mybatis-plus-boot-starter:3.4.0")
	implementation("org.jetbrains.kotlin:kotlin-stdlib-jdk8")
	implementation("com.google.code.gson:gson:2.8.6")
	implementation("com.squareup.moshi:moshi-kotlin:1.11.0")
	implementation("commons-io:commons-io:2.6")
	implementation("cn.hutool:hutool-all:5.5.0")
	testImplementation("org.springframework.boot:spring-boot-starter-test")
	kapt("com.squareup.moshi:moshi-kotlin-codegen:1.11.0")
}

tasks.withType<Test> {
	useJUnitPlatform()
}

tasks.withType<KotlinCompile> {
	kotlinOptions {
		freeCompilerArgs = listOf("-Xjsr305=strict")
		jvmTarget = "1.8"
	}
}
