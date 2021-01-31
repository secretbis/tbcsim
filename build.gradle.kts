//import org.jetbrains.compose.compose

import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
    kotlin("jvm") version "1.4.21"
    kotlin("plugin.serialization") version "1.4.21"

    idea
    application
}

group = "com.tbcsim"

repositories {
    jcenter()
    mavenCentral()
}

dependencies {
    implementation("de.m3y.kformat:kformat:0.7")
    implementation("com.squareup:kotlinpoet:1.7.2")
    implementation("com.fleshgrinder.kotlin:case-format:0.1.0")
    implementation("com.github.ajalt.clikt:clikt:3.1.0")
    implementation("com.charleskorn.kaml:kaml:0.26.0")

    implementation("com.fasterxml.jackson.core:jackson-annotations:2.12.+")
    implementation("com.fasterxml.jackson.core:jackson-core:2.12.+")
    implementation("com.fasterxml.jackson.core:jackson-databind:2.12.+")
    implementation("com.fasterxml.jackson.module:jackson-module-kotlin:2.12.+")

    implementation("io.github.microutils:kotlin-logging:1.12.0")
    implementation("org.jetbrains.kotlin:kotlin-reflect:1.4.21")
    implementation("org.jetbrains.kotlinx:kotlinx-collections-immutable:0.3.3")
    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-core:1.4.2")
    implementation("org.jetbrains.kotlinx:kotlinx-serialization-json:1.0.1")
    implementation("org.slf4j:slf4j-simple:1.7.29")

    testImplementation("io.kotest:kotest-assertions-core:4.3.2")
    testImplementation("io.kotest:kotest-property:4.3.2")
    testImplementation("io.kotest:kotest-runner-junit5:4.3.2")
}

tasks.test {
    useJUnitPlatform()
}

tasks.withType<KotlinCompile>() {
    kotlinOptions.jvmTarget = "11"
}

application {
    mainClassName = "MainKt"
}

//compose.desktop {
//    application {
//        mainClass = "MainKt"
//
//        nativeDistributions {
//            packageName = "TBCSim"
//            version = version
//            description = "A simulator for TBC damage dealers"
//            targetFormats(TargetFormat.Dmg, TargetFormat.Msi, TargetFormat.Deb)
//        }
//    }
//}
