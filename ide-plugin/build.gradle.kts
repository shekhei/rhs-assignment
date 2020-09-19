val intellijLocalPath: String? by project
plugins {
    `java-library`
    kotlin("jvm")
    kotlin("kapt")
//    `maven`
    id("org.jetbrains.intellij")
}

repositories {
    mavenLocal()
    mavenCentral()
    jcenter()
    google()
    gradlePluginPortal()
    maven {
        url = uri("https://dl.bintray.com/jetbrains/intellij-third-party-dependencies/")
    }
}

dependencies {
    implementation(kotlin("stdlib"))
//    implementation("org.jetbrains.intellij.plugins:structure-intellij-classes:3.99")
//    implementation(kotlin("gradle-plugin"))
    implementation(project(":rhs-assignment-compiler-plugin"))
    kapt("com.google.auto.service:auto-service:1.0-rc7")
    compileOnly("com.google.auto.service:auto-service-annotations:1.0-rc7")
}

intellij {
    if (intellijLocalPath != null) {
        println("using local path")
        localPath = intellijLocalPath
    } else {
        version = "2020.2"
    }
    setPlugins("gradle", "org.jetbrains.kotlin:1.4.10-release-IJ2020.2-1")
    isDownloadSources = true
    pluginName = "RhsAssignment"
}