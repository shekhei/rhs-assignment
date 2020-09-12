val kotlinVersion = "1.4.0"
buildscript {
//    val arrow_meta = "1.3.61-SNAPSHOT"
//
//    repositories {
////        google()
////        jcenter()
////        gradlePluginPortal()
//        maven {
//            url = uri("https://oss.jfrog.org/artifactory/oss-snapshot-local/")
//        }
//        maven {
//            url = uri("https://plugins.gradle.org/m2/")
//        }
//    }
//    dependencies {
//        classpath("io.arrow-kt:gradle-plugin:$arrow_meta")
////        classpath("io.arrow-kt:compiler-plugin:$arrow_meta")
//
//        // NOTE: Do not place your application dependencies here; they belong
//        // in the individual module build.gradle files
//    }
}
plugins {
    val kotlinVersion = "1.4.0"
    `java-library`
    kotlin("jvm") version kotlinVersion
    kotlin("kapt") version kotlinVersion
//    id("org.jetbrains.intellij") version "0.4.22"
//    id("io.arrow-kt.arrow")
//    id("com.github.johnrengelman.shadow")
//    id("io.arrow-kt.arrow") version "1.3.60-55-b57f44f"
}
//apply(plugin = "io.arrow-kt.arrow")

repositories {
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

val ARROW_VERSION = "0.10.4-SNAPSHOT"

dependencies {
    implementation(kotlin("stdlib-jdk8"))
    compileOnly(kotlin("compiler-embeddable"))
//    compileOnly("io.arrow-kt", "compiler-plugin", "1.3.70-SNAPSHOT")

//    testImplementation("io.arrow-kt", "testing-plugin", "1.3.61-SNAPSHOT")
//    testImplementation(kotlin("stdlib"))
    testImplementation("org.junit.jupiter:junit-jupiter:5.6.0")
    testImplementation("com.github.tschuchortdev:kotlin-compile-testing:1.2.10")
//    testImplementation("io.arrow-kt:meta-test:1.3.61-SNAPSHOT")
    testImplementation(kotlin("test"))
    testImplementation(kotlin("test-junit5"))
//    testImplementation("io.arrow-kt", "compiler-plugin", "1.3.61-SNAPSHOT")
//    testImplementation("io.arrow-kt", "arrow-meta-prelude", "1.3.61-SNAPSHOT")
    testImplementation("io.github.classgraph:classgraph:4.8.47")
    kapt("com.google.auto.service:auto-service:1.0-rc7")
    compileOnly("com.google.auto.service:auto-service-annotations:1.0-rc7")
    testImplementation(kotlin("compiler-embeddable"))
    testImplementation("io.mockk:mockk:1.10.0")
//    testImplementation ("io.arrow-kt:arrow-core-data:$ARROW_VERSION")
//    kapt("io.arrow-kt:arrow-annotations-processor:0.7.1")
//    implementation("io.arrow-kt:arrow-annotations:0.7.1") {
//        exclude( group= "org.jetbrains.kotlin", module= "kotlin-stdlib")
//    }
//    testImplementation("io.arrow-kt:arrow-core-data:$ARROW_VERSION") {
//        exclude (group= "org.jetbrains.kotlin", module= "kotlin-stdlib")
//    }
//    testImplementation("io.arrow-kt:arrow-optics:$ARROW_VERSION") {
//        exclude (group= "org.jetbrains.kotlin", module= "kotlin-stdlib")
//    }
}

tasks.withType<org.jetbrains.kotlin.gradle.tasks.KotlinCompile> {
    kotlinOptions {
        jvmTarget = "1.8"
    }
}

// Create a new JAR with: Arrow Meta + new plugin
//tasks.withType<com.github.jengelman.gradle.plugins.shadow.tasks.ShadowJar> {
//    configurations = listOf(project.configurations.compileOnly.get())
//    dependencies {
//        exclude("org.jetbrains.kotlin:kotlin-stdlib")
//        exclude("org.jetbrains.kotlin:kotlin-compiler-embeddable")
//    }
//}

tasks.test {
    System.setProperty("user.language", "en")
    System.setProperty("user.region", "US")
    useJUnitPlatform()
}