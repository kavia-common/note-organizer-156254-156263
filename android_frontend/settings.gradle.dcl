pluginManagement {
    repositories {
        google()
        gradlePluginPortal()
    }
}

plugins {
    id("org.gradle.experimental.android-ecosystem").version("0.1.43")
}

rootProject.name = "example-android-app"

include("app")
include("list")
include("utilities")

defaults {
    androidApplication {
        jdkVersion = 17
        compileSdk = 34
        minSdk = 30

        versionCode = 1
        versionName = "0.1"
        applicationId = "org.gradle.experimental.android.app"

        testing {
            dependencies {
                implementation("org.junit.jupiter:junit-jupiter:5.10.2")
                // Add JUnit 4 for Android unit test discovery by default
                implementation("junit:junit:4.13.2")
                runtimeOnly("org.junit.platform:junit-platform-launcher")
            }
        }
    }

    androidLibrary {
        jdkVersion = 17
        compileSdk = 34
        minSdk = 30

        testing {
            dependencies {
                implementation("org.junit.jupiter:junit-jupiter:5.10.2")
                // Add JUnit 4 for library unit tests as well
                implementation("junit:junit:4.13.2")
                runtimeOnly("org.junit.platform:junit-platform-launcher")
            }
        }
    }
}
