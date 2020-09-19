val intellijLocalPath: String? by project
plugins {
    `java-gradle-plugin`
    kotlin("jvm") version "1.4.0"
    kotlin("kapt") version "1.4.0"
    `maven`
}

group = "io.github.shekhei"
version = "0.0.1-SNAPSHOT"

repositories {
    mavenCentral()
    jcenter()
    google()
    gradlePluginPortal()
    maven {
        url = uri("https://dl.bintray.com/jetbrains/intellij-third-party-dependencies/")
    }
}

gradlePlugin {
    plugins {
        create("simplePlugin") {
            id = "io.github.shekhei.rhs-assignment-gradle-plugin" // users will do `apply plugin: "debuglog.plugin"`
            implementationClass = "io.github.shekhei.RhsAssignmentGradlePlugin"
        }
    }
}

dependencies {
    implementation(kotlin("stdlib"))
    implementation(gradleApi())
    // contains classes like Subplugin, SubpluginOption, etc
    implementation(kotlin("gradle-plugin-api"))
    implementation(kotlin("gradle-plugin"))
    kapt("com.google.auto.service:auto-service:1.0-rc7")
    compileOnly("com.google.auto.service:auto-service-annotations:1.0-rc7")
}
