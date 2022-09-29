import org.jetbrains.kotlin.gradle.plugin.mpp.KotlinNativeTarget

plugins {
    kotlin("multiplatform")
    kotlin("plugin.serialization") version "1.7.10"
}

group = "com.danielecampogiani.ktmp"
version = "1.0-SNAPSHOT"

val ktorVer = "1.6.8"
val coroutinesVer = "1.3.8"
val serializationVer = "1.3.3"

repositories {
    gradlePluginPortal()
    google()
    jcenter()
    mavenCentral()
}

kotlin {
    jvm("android")
    ios {
        binaries {
            framework {
                baseName = "common"
            }
        }
    }

    sourceSets {
        val commonMain by getting {
            dependencies {
                // Coroutines
                implementation("org.jetbrains.kotlinx:kotlinx-coroutines-core:$coroutinesVer")

                // Ktor
                implementation("io.ktor:ktor-client-core:$ktorVer")
                implementation("io.ktor:ktor-client-json:$ktorVer")
                implementation("io.ktor:ktor-client-logging:$ktorVer")
                implementation("io.ktor:ktor-client-serialization:$ktorVer")

                // Serialize
                implementation("org.jetbrains.kotlinx:kotlinx-serialization-json:$serializationVer")
            }
        }

        val commonTest by getting {
            dependencies {
                implementation(kotlin("test-common"))
                implementation(kotlin("test-annotations-common"))
                implementation("io.mockk:mockk:1.13.2")

            }
        }

        val androidMain by getting {
            dependencies {
                // Coroutines
                implementation("org.jetbrains.kotlinx:kotlinx-coroutines-android:$coroutinesVer")

                // Ktor
                implementation("io.ktor:ktor-client-android:$ktorVer")
                implementation("io.ktor:ktor-client-core-jvm:$ktorVer")
                implementation("io.ktor:ktor-client-json-jvm:$ktorVer")
                implementation("io.ktor:ktor-client-logging-jvm:$ktorVer")
                implementation("io.ktor:ktor-client-serialization-jvm:$ktorVer")
            }
        }

        val androidTest by getting {
            dependencies {
                implementation(kotlin("test-junit"))
                implementation("junit:junit:4.13.2")
            }
        }

        val iosMain by getting {
            dependencies {
                implementation("org.jetbrains.kotlinx:kotlinx-coroutines-core-native:$coroutinesVer")

                implementation("io.ktor:ktor-client-ios:$ktorVer")
            }
        }
        val iosTest by getting
    }
}
