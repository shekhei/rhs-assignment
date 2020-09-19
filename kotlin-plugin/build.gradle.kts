plugins {
    `java-library`
    kotlin("jvm")
    kotlin("kapt")
}

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
    testImplementation("net.java.dev.jna:jna:5.6.0")
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