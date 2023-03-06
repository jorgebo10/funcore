import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

@Suppress("DSL_SCOPE_VIOLATION") plugins {
    application
    alias(libs.plugins.kotest.multiplatform)
    alias(libs.plugins.kotlin.jvm)
    alias(libs.plugins.kover)
    alias(libs.plugins.kotlinx.serialization)
    alias(libs.plugins.sqldelight)
    alias(libs.plugins.ktor)
    alias(libs.plugins.spotless)
    alias(libs.plugins.detekt)
}


group = "org.example"
version = "1.0-SNAPSHOT"

tasks {
    withType<KotlinCompile>().configureEach {
        kotlinOptions {
            jvmTarget = "${JavaVersion.VERSION_17}"
            freeCompilerArgs = freeCompilerArgs + "-Xcontext-receivers"
        }
    }

    test {
        useJUnitPlatform()
        extensions.configure(kotlinx.kover.api.KoverTaskExtension::class) {
            includes.add("io.github.jbo.*")
        }
    }
}


repositories {
    google()
    mavenCentral()
}

dependencies {
    implementation(libs.bundles.arrow)
    implementation(libs.bundles.ktor.server)
    implementation(libs.logback.classic)
    implementation(libs.sqldelight.jdbc)
    implementation(libs.hikari)
    implementation(libs.bundles.cohort)

    testImplementation(libs.bundles.ktor.client)
    testImplementation(libs.ktor.server.tests)
    testImplementation(libs.bundles.kotest)
}

java {
    sourceCompatibility = JavaVersion.VERSION_17
    targetCompatibility = JavaVersion.VERSION_17
}

spotless {
    kotlin {
        targetExclude("**/build/**")
        ktlint()
    }
    json {
        target("src/**/*.json")               // you have to set the target manually
        jackson()                             // has its own section below
    }
    yaml {
        target("src/**/*.yaml")               // you have to set the target manually
        jackson()                             // has its own section below
    }
}

sqldelight {
    databases {
        create("SqlDelight") {
            packageName.set("io.github.nomisrev.sqldelight")
            dialect(libs.sqldelight.mysql.dialect)
        }
    }
}

application {
    mainClass.set("io.github.jbo.MainKt")
}

allprojects {
    extra.set("dokka.outputDirectory", rootDir.resolve("docs"))
}