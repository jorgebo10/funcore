rootProject.name = "funcore"

plugins {
    id("com.gradle.enterprise") version "3.12.4"
}

dependencyResolutionManagement {
    versionCatalogs {
        create("libs") {
            from(files("libs.version.toml"))
        }
    }

    repositories {
        mavenCentral()
    }
}