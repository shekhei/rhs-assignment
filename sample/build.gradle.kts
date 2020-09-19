
plugins {
    `java-library`
    kotlin("jvm")
//    kotlin("plugin.allopen")
    id("org.jetbrains.kotlin.plugin.allopen")
}


allOpen {
    annotation("io.github.shekhei.sample.Open")
    // annotations("com.another.Annotation", "com.third.Annotation")
}
apply(plugin = "io.github.shekhei.rhs-assignment-gradle-plugin")