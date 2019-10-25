import org.jetbrains.kotlin.gradle.tasks.KotlinCompile
import com.github.jengelman.gradle.plugins.shadow.tasks.ShadowJar

val logback_version: String by project
val ktor_version: String by project
val kotlin_version: String by project


buildscript {
    dependencies {
        classpath("com.github.jengelman.gradle.plugins:shadow:5.1.0")
    }
}

apply(plugin = "com.github.johnrengelman.shadow")

plugins {
    application
    kotlin("jvm") version "1.3.50"
    kotlin("kapt") version "1.3.50"
}

group = "nl.codestar"
version = "0.0.1-SNAPSHOT"

application {
    mainClassName = "io.ktor.server.netty.EngineMain"
}

repositories {
    mavenLocal()
    jcenter()
    maven { url = uri("https://kotlin.bintray.com/ktor") }
    maven { url = uri("https://plugins.gradle.org/m2/") }
}

tasks {
    withType<KotlinCompile> {
        kotlinOptions.jvmTarget = "1.8"
    }
}

dependencies {
    compile("org.jetbrains.kotlin:kotlin-stdlib-jdk8:$kotlin_version")
    compile("ch.qos.logback:logback-classic:$logback_version")
    compile("io.ktor:ktor-server-netty:$ktor_version")
    compile("io.ktor:ktor-server-core:$ktor_version")
    compile("io.ktor:ktor-auth:$ktor_version")
    compile("io.ktor:ktor-gson:$ktor_version")
    compile("io.ktor:ktor-locations:$ktor_version")
    compile("io.ktor:ktor-metrics:$ktor_version")
    compile("io.ktor:ktor-server-host-common:$ktor_version")
    compile("io.ktor:ktor-websockets:$ktor_version")
    compile("org.jetbrains.exposed:exposed:0.17.4")
    testCompile("io.ktor:ktor-server-tests:$ktor_version")
}

kotlin.sourceSets["main"].kotlin.srcDirs("src")
kotlin.sourceSets["test"].kotlin.srcDirs("test")

sourceSets["main"].resources.srcDirs("resources")
sourceSets["test"].resources.srcDirs("testresources")

tasks.withType<ShadowJar>  {
    archiveBaseName.set("${project.name}-all")
}
