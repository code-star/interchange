plugins {
  base
  kotlin("jvm") version "1.3.50"
  kotlin("kapt") version "1.3.50"
}

allprojects {
  repositories {
    mavenLocal()
    jcenter()
    maven { url = uri("https://kotlin.bintray.com/ktor") }
    maven { url = uri("https://plugins.gradle.org/m2/") }
  }
}
