/*
 * This file was generated by the Gradle 'init' task.
 *
 * The settings file is used to specify which projects to include in your build.
 *
 * Detailed information about configuring a multi-project build in Gradle can be found
 * in the user manual at https://docs.gradle.org/6.2.1/userguide/multi_project_builds.html
 */

rootProject.name = "rhs-assignment"
pluginManagement {
    plugins {
        `java-library`
        application
        id("com.github.johnrengelman.shadow") version "6.0.0"

    }
    repositories {
        mavenCentral()
        jcenter()
        gradlePluginPortal()
        maven {
            url = uri("https://plugins.gradle.org/m2/")
        }
//        maven {
//            url = uri("http://artifactory.paypalcorp.com/artifactory/public")
//        }
//        maven {
//            url = uri("http://artifactory.paypalcorp.com/artifactory/central")
//        }
//        maven {
//            url = uri("http://artifactory.paypalcorp.com/artifactory/maven-public")
//        }
//        maven {
//            url = uri("https://artifactory.paypalcorp.com/artifactory/gradle-plugins/m2")
//        }
    }
}
