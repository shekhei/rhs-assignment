val intellijLocalPath: String? by project
plugins {
    `java`
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
//    implementation(kotlin("compiler-embeddable"))
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
    updateSinceUntilBuild = false
    setPlugins("com.intellij.gradle", "org.jetbrains.plugins.gradle", "java", "org.jetbrains.kotlin:1.4.10-release-IJ2020.2-1")
    isDownloadSources = true
    pluginName = "RhsAssignment"
}


tasks.withType<org.jetbrains.intellij.tasks.RunIdeTask> {
    var jvmArgs = this.jvmArgs ?: listOf()
    jvmArgs += listOf("-agentlib:jdwp=transport=dt_socket,server=n,suspend=y,address=55884")
    this.jvmArgs = jvmArgs
}

tasks.withType<org.jetbrains.kotlin.gradle.tasks.KotlinCompile> {
    kotlinOptions.jvmTarget = "1.8"
    kotlinOptions.freeCompilerArgs += listOf("-Xjvm-default=enable")
}

