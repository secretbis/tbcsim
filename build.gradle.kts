import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

import org.jetbrains.kotlin.gradle.plugin.KotlinCompilation
import org.jetbrains.kotlin.gradle.plugin.KotlinJsCompilerType
import org.jetbrains.kotlin.gradle.targets.js.ir.JsIrBinary
import org.jetbrains.kotlin.gradle.targets.js.npm.npmProject

plugins {
    kotlin("multiplatform") version "1.4.21"
    kotlin("plugin.serialization") version "1.4.21"

    idea
    application
}

group = "com.tbcsim"

repositories {
    mavenCentral()
    jcenter()
}

kotlin {
    // Build individual JS files instead of a big one
    // Fixed by commit (Kotlin 1.5.20-dev): https://github.com/JetBrains/kotlin/commit/3f10914f05b860a74a30656b046f896644795c57
    // Found at: https://github.com/fluidsonic/kjs-chunks/blob/main/build.gradle.kts
    js(KotlinJsCompilerType.IR) {
        compilations.named(KotlinCompilation.MAIN_COMPILATION_NAME) {
            val compilation = this
            val compileKotlinTask = compileKotlinTask
            val npmModuleIndex = compilation.npmProject.dir.resolve(compilation.npmProject.main)
            val npmModuleDir = npmModuleIndex.parentFile

            compileKotlinTask
                .doFirst {
                    // Disable klib creation.
                    // Instead simply create separate JS files per module & dependency (in /build/classes/main/)

                    kotlinOptions {
                        freeCompilerArgs = freeCompilerArgs
                            .filter { it != "-Xir-produce-klib-dir" }
                            .plus(listOf("-Xir-per-module", "-Xir-produce-js"))
                    }
                }
                .finalizedBy(tasks.create<Copy>("copy${compileKotlinTaskName.removePrefix("compile")}ToRootProject") {
                    // Copy the separate JS files to the expected location (from /build/classes/main/ to /build/js/packages/kjs-chunks/kotlin/)

                    from(compileKotlinTask) {
                        exclude { it.file == compileKotlinTask.outputFile }
                    }
                    from(compileKotlinTask.outputFile) {
                        rename { npmModuleIndex.name }
                    }
                    into(npmModuleDir)
                })
        }

        browser {
            webpackTask {
                output.libraryTarget = "commonjs"
            }
        }

        binaries.executable().forEach { binary ->
            // Looks like we don't need this task anymore?

            binary as JsIrBinary
            binary.linkTask.configure {
                isEnabled = false
            }
        }
    }

    jvm {
        withJava()
    }

    sourceSets {
        val commonMain by getting {
            dependencies {
                implementation("de.m3y.kformat:kformat:0.7")

                implementation("net.mamoe.yamlkt:yamlkt:0.8.0")

                implementation("io.github.microutils:kotlin-logging:2.0.4")

                implementation("org.jetbrains.kotlinx:kotlinx-collections-immutable:0.3.3")
                implementation("org.jetbrains.kotlinx:kotlinx-coroutines-core:1.4.2")
                implementation("org.jetbrains.kotlinx:kotlinx-serialization-json:1.0.1")
            }
        }

        val jvmMain by getting {
            dependencies {
                implementation("com.fasterxml.jackson.core:jackson-annotations:2.12.1")
                implementation("com.fasterxml.jackson.core:jackson-core:2.12.1")
                implementation("com.fasterxml.jackson.core:jackson-databind:2.12.1")
                implementation("com.fasterxml.jackson.module:jackson-module-kotlin:2.12.1")

                implementation("net.pearx.kasechange:kasechange-jvm:1.3.0")

                implementation("com.github.ajalt.clikt:clikt:3.1.0")

                implementation("com.squareup:kotlinpoet:1.7.2")

                implementation("io.github.microutils:kotlin-logging-jvm:2.0.4")
                implementation("org.slf4j:slf4j-simple:1.7.29")
            }
        }

        val jvmTest by getting {
            dependencies {
                implementation("io.kotest:kotest-assertions-core:4.3.2")
                implementation("io.kotest:kotest-property:4.3.2")
                implementation("io.kotest:kotest-runner-junit5:4.3.2")
            }
        }

        val jsMain by getting {
            dependencies {
                implementation("io.github.microutils:kotlin-logging-js:2.0.4")
            }
        }
    }
}

tasks.test {
    useJUnitPlatform()
}

tasks.withType<KotlinCompile> {
    kotlinOptions.jvmTarget = "11"
    kotlinOptions.freeCompilerArgs += listOf(
        "-Xuse-experimental=kotlin.js.ExperimentalJsExport",
        "-Xuse-experimental=kotlin.js.JsExport"
    )
}

distributions {
    main {
        contents {
            from("$buildDir/libs") {
                rename("${rootProject.name}-jvm", rootProject.name)
                into("lib")
            }
        }
    }
}

application {
    mainClassName = "MainKt"
}
