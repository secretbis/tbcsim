import org.jetbrains.kotlin.gradle.dsl.KotlinJsCompile

plugins {
    kotlin("multiplatform") version "2.2.21"
    kotlin("plugin.serialization") version "2.2.21"
    idea
}

group = "com.tbcsim"

repositories {
    mavenCentral()
}

kotlin {
    jvm()
    js {
        browser()
        generateTypeScriptDefinitions()
        binaries.executable()
        useEsModules()
    }

    sourceSets {
        val commonMain by getting {
            dependencies {
                implementation("de.m3y.kformat:kformat:0.14")
                implementation("net.mamoe.yamlkt:yamlkt:0.13.0")
                implementation("io.github.oshai:kotlin-logging:7.0.13")
                implementation("org.jetbrains.kotlinx:kotlinx-collections-immutable:0.4.0")
                implementation("org.jetbrains.kotlinx:kotlinx-coroutines-core:1.10.2")
                implementation("org.jetbrains.kotlinx:kotlinx-serialization-json:1.9.0")
            }
        }

        val jvmMain by getting {
            dependencies {
                implementation("com.fasterxml.jackson.core:jackson-annotations:2.20")
                implementation("com.fasterxml.jackson.core:jackson-core:2.20.1")
                implementation("com.fasterxml.jackson.core:jackson-databind:2.20.1")
                implementation("com.fasterxml.jackson.module:jackson-module-kotlin:2.20.1")
                implementation("net.pearx.kasechange:kasechange-jvm:1.4.1")
                implementation("com.github.ajalt.clikt:clikt:5.0.3")
                implementation("com.squareup:kotlinpoet:2.2.0")
                implementation("io.github.oshai:kotlin-logging-jvm:7.0.13")
                implementation("org.slf4j:slf4j-simple:2.0.17")
            }
        }

        val jvmTest by getting {
            dependencies {
                implementation("io.kotest:kotest-assertions-core:6.0.7")
                implementation("io.kotest:kotest-property:6.0.7")
                implementation("io.kotest:kotest-runner-junit5:6.0.7")
            }
        }

        val jsMain by getting {
            dependencies {
                implementation("io.github.oshai:kotlin-logging-js:7.0.13")
            }
        }
    }

    compilerOptions {
        optIn.add("kotlin.js.ExperimentalJsExport")
        freeCompilerArgs = listOf(
            "-Xes-long-as-bigint"
        )
    }
}

// TODO: This would be nice, but the output drops the package info and jams it all into one export namespace
//tasks.withType<KotlinJsCompile>().configureEach {
//    compilerOptions {
//        target = "es2015"
//    }
//}

// This all gets bundled separately with the UI anyway, we don't need an optimized build here
tasks.named("jsBrowserProductionWebpack") {
    enabled = false
}

tasks.withType<Test>().configureEach {
    useJUnitPlatform()
}
