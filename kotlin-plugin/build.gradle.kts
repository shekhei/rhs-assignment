plugins {
    `java-library`
    kotlin("jvm")
    kotlin("kapt")
}

dependencies {
    implementation(kotlin("stdlib-jdk8"))
    compileOnly(kotlin("compiler-embeddable"))
    testImplementation("org.junit.jupiter:junit-jupiter:5.6.0")
    testImplementation("com.github.tschuchortdev:kotlin-compile-testing:1.2.10")
    testImplementation(kotlin("test"))
    testImplementation(kotlin("test-junit5"))
    testImplementation("io.github.classgraph:classgraph:4.8.47")
    kapt("com.google.auto.service:auto-service:1.0-rc7")
    compileOnly("com.google.auto.service:auto-service-annotations:1.0-rc7")
    testImplementation(kotlin("compiler-embeddable"))
    testImplementation("io.mockk:mockk:1.10.0")
    testImplementation("net.java.dev.jna:jna:5.6.0")
}

tasks.test {
    System.setProperty("user.language", "en")
    System.setProperty("user.region", "US")
    useJUnitPlatform()
}