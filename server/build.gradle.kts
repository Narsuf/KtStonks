import org.gradle.api.tasks.testing.Test
import org.gradle.testing.jacoco.tasks.JacocoReport

plugins {
    alias(libs.plugins.kotlinJvm)
    alias(libs.plugins.ktor)
    application
    kotlin("plugin.serialization") version "1.9.0"
    id("jacoco")
}

group = "org.n27.ktstonks"
version = "1.0.0"
application {
    mainClass.set("org.n27.ktstonks.ApplicationKt")

    val isDevelopment: Boolean = project.ext.has("development")
    applicationDefaultJvmArgs = listOf("-Dio.ktor.development=$isDevelopment")
}

dependencies {
    implementation(libs.logback)
    implementation(libs.ktor.serverCore)
    implementation(libs.ktor.serverNetty)
    implementation(libs.ktor.client.core)
    implementation(libs.ktor.client.cio)
    implementation(libs.ktor.client.content.negotiation)
    implementation(libs.ktor.serialization.kotlinx.json)
    implementation(libs.h2)
    implementation(libs.exposed.core)
    implementation(libs.exposed.dao)
    implementation(libs.exposed.jdbc)
    implementation(libs.koin.core)
    implementation(libs.koin.ktor)
    testImplementation(libs.ktor.serverTestHost)
    testImplementation(libs.kotlin.testJunit)
    testImplementation(libs.ktor.client.mock)
}

tasks.withType<Test> {
    jacoco { isEnabled = true }
}

tasks.register("jacocoJvmTestReport", JacocoReport::class) {
    dependsOn("test")

    reports {
        xml.required.set(true)
        html.required.set(true)
    }

    sourceDirectories.setFrom(files(
        "src/main/kotlin"
    ))

    classDirectories.setFrom(files("$buildDir/classes/kotlin/main"))
    executionData.setFrom(files("$buildDir/jacoco/test.exec"))
}

tasks.named("test") {
    finalizedBy("jacocoJvmTestReport")
}
