plugins {
    val kotlinVersion = "1.4.0"
    `java-library`
    kotlin("jvm") version kotlinVersion
    kotlin("kapt") version kotlinVersion
    kotlin("plugin.allopen") version kotlinVersion apply false
}

buildscript {
    repositories {
        mavenCentral()
    }
    dependencies {
        classpath("io.github.shekhei:rhs-assignment-gradle-plugin")
    }
}

allprojects {
    repositories {
        mavenLocal()
        mavenCentral()
        jcenter()
        maven { url = uri("https://oss.jfrog.org/artifactory/oss-snapshot-local/") }

        maven { url = uri("https://kotlin.bintray.com/kotlinx") }
        maven {
            url = uri("https://dl.bintray.com/arrow-kt/arrow-kt/")
            content {
                includeGroup("io.arrow-kt")
            }
        }
        maven { url = uri("https://jitpack.io") }
    }

    // Install into local Maven repo with `./gradlew :kotlin-plugin:install :gradle-plugin:install`
    group = "io.github.shekhei"
    version = "0.0.1-SNAPSHOT"
}

subprojects {
    if ( this.name != "sample") {
        apply(plugin = "maven")
    }
    configurations.all {
        resolutionStrategy {
            dependencySubstitution {
                // The Kotlin plugin will try to add the trackable-compiler-plugin to dependencies with a version
                // We have it local to this project, so we want it to just substitute that with the local project
                // Not needed for external consumers
                substitute(
                        module("io.github.shekhei:rhs-assignment-compiler-plugin")
                ).with(project(":rhs-assignment-compiler-plugin"))
            }
        }
    }
}