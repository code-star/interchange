val serialization_version: String by project

plugins {
    kotlin("jvm")
}

group = "nl.codestar"
version = "0.0.1-SNAPSHOT"

buildscript {
    dependencies {
        classpath("org.jetbrains.kotlin:kotlin-gradle-plugin:1.3.50")
        classpath("org.jetbrains.kotlin:kotlin-serialization:1.3.50")
    }
}

apply {
    plugin("kotlinx-serialization")
}

dependencies {
    implementation("org.jetbrains.kotlinx:kotlinx-serialization-runtime:$serialization_version")
    testImplementation("io.kotlintest:kotlintest-runner-junit5:3.4.2")
}

tasks {
    compileKotlin {
        kotlinOptions.jvmTarget = "1.8"
    }
    compileTestKotlin {
        kotlinOptions.jvmTarget = "1.8"
    }
}
