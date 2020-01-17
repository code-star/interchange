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
    kotlin("jvm")
    kotlin("kapt")
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

    withType<Test> {
        useJUnitPlatform()
    }
}

dependencies {
    implementation("org.jetbrains.kotlin:kotlin-stdlib-jdk8:$kotlin_version")
    implementation("ch.qos.logback:logback-classic:$logback_version")
    implementation("io.ktor:ktor-server-netty:$ktor_version") { exclude("io.netty", "netty-codec-http2")}
    implementation("io.ktor:ktor-server-core:$ktor_version")
    implementation("io.ktor:ktor-auth:$ktor_version")
    implementation("io.ktor:ktor-gson:$ktor_version")
    implementation("io.ktor:ktor-locations:$ktor_version")
    implementation("io.ktor:ktor-metrics:$ktor_version")
    implementation("io.ktor:ktor-server-host-common:$ktor_version")
    implementation("io.ktor:ktor-websockets:$ktor_version")
    implementation("org.jetbrains.exposed:exposed:0.17.4")
    implementation("software.amazon.kinesis:amazon-kinesis-client:2.2.5"){ exclude("com.amazonaws", "aws-java-sdk-kinesisvideo") }
    implementation("io.netty:netty-codec-http2:4.1.35.Final")
    testImplementation("io.ktor:ktor-server-tests:$ktor_version")
    testImplementation("io.kotlintest:kotlintest-runner-junit5:3.3.0")
}

kotlin.sourceSets["main"].kotlin.srcDirs("src")
kotlin.sourceSets["test"].kotlin.srcDirs("test")

project.the<SourceSetContainer>()["main"].resources.srcDirs("resources")
project.the<SourceSetContainer>()["test"].resources.srcDirs("testresources")
