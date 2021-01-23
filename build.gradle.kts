import org.jetbrains.compose.compose
import org.jetbrains.compose.desktop.application.dsl.TargetFormat

import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
    kotlin("jvm") version "1.4.21"
    id("org.jetbrains.kotlin.plugin.allopen") version "1.4.21"
    id("org.jetbrains.compose") version "0.3.0-build139"
    idea
    application
}

group = "com.tbcsim"

repositories {
    jcenter()
    mavenCentral()
    maven("https://maven.pkg.jetbrains.space/public/p/compose/dev")
}

dependencies {
    implementation("org.jetbrains.lets-plot:lets-plot-jfx:1.5.6")
    api("org.jetbrains.lets-plot:lets-plot-common:1.5.6")
    api("org.jetbrains.lets-plot-kotlin:lets-plot-kotlin-api:1.2.0")

    implementation("de.m3y.kformat:kformat:0.7")
    implementation("com.squareup:kotlinpoet:1.7.2")

    implementation("com.fasterxml.jackson.core:jackson-annotations:2.12.+")
    implementation("com.fasterxml.jackson.core:jackson-core:2.12.+")
    implementation("com.fasterxml.jackson.core:jackson-databind:2.12.+")
    implementation("com.fasterxml.jackson.module:jackson-module-kotlin:2.12.+")

    implementation("io.github.microutils:kotlin-logging:1.12.0")
    implementation("org.jetbrains.kotlin:kotlin-reflect:1.4.21")
    implementation("org.jetbrains.kotlinx:kotlinx-collections-immutable:0.3.3")
    implementation("org.slf4j:slf4j-simple:1.7.29")

    implementation(compose.desktop.currentOs)
}

tasks.test {
    useJUnit()
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
