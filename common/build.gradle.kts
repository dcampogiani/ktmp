buildscript {
    repositories {
        jcenter()
    }
    dependencies {
        classpath(kotlin("gradle-plugin", version = "1.3.72"))
        classpath(kotlin("serialization", version = "1.3.72"))
    }
}

plugins {
    kotlin("multiplatform")
    kotlin("plugin.serialization") version "1.3.70"
}

kotlin {
    targets {
        //select iOS target platform depending on the Xcode environment variables
        val iOSTarget: (String, org.jetbrains.kotlin.gradle.plugin.mpp.KotlinNativeTarget.() -> Unit) -> org.jetbrains.kotlin.gradle.plugin.mpp.KotlinNativeTarget =
            if (System.getenv("SDK_NAME")?.startsWith("iphoneos") == true)
                ::iosArm64
            else
                ::iosX64

        iOSTarget("ios") {
            binaries {
                framework {
                    baseName = "common"
                    freeCompilerArgs = listOf("-Xobjc-generics")
                }
            }
        }

        jvm("android")
    }

    sourceSets {
        val commonMain by getting {
            dependencies {
                // Kotlin
                implementation("org.jetbrains.kotlin:kotlin-stdlib-common:1.3.72")

                // Coroutines
                implementation("org.jetbrains.kotlinx:kotlinx-coroutines-core-common:1.3.7")

                // Ktor
                implementation("io.ktor:ktor-client-core:1.3.2")
                implementation("io.ktor:ktor-client-json:1.3.2")
                implementation("io.ktor:ktor-client-logging:1.3.2")
                implementation("io.ktor:ktor-client-serialization:1.3.2")
                implementation("io.ktor:ktor-serialization:1.3.2")

                // Serialize
                implementation("org.jetbrains.kotlinx:kotlinx-serialization-runtime-common:0.20.0")
            }
        }

        val androidMain by getting {
            dependencies {
                // Kotlin
                implementation("org.jetbrains.kotlin:kotlin-stdlib:1.3.72")

                // Coroutines
                implementation("org.jetbrains.kotlinx:kotlinx-coroutines-android:1.3.7")
                implementation("org.jetbrains.kotlinx:kotlinx-coroutines-core:1.3.7")

                // Ktor
                implementation("io.ktor:ktor-client-android:1.3.2")
                implementation("io.ktor:ktor-client-core-jvm:1.3.2")
                implementation("io.ktor:ktor-client-json-jvm:1.3.2")
                implementation("io.ktor:ktor-client-logging-jvm:1.3.2")
                implementation("io.ktor:ktor-client-serialization-jvm:1.3.2")

                // Serialize
                implementation("org.jetbrains.kotlinx:kotlinx-serialization-runtime:0.20.0")
            }
        }

        val iosMain by getting {
            dependencies {
                // Coroutines
                implementation("org.jetbrains.kotlinx:kotlinx-coroutines-core-native:1.3.7")

                // Ktor
                implementation("io.ktor:ktor-client-ios:1.3.2")
                implementation("io.ktor:ktor-client-core-native:1.3.2")
                implementation("io.ktor:ktor-client-json-native:1.3.2")
                implementation("io.ktor:ktor-client-logging-native:1.3.2")
                implementation("io.ktor:ktor-client-serialization-native:1.3.2")

                // Serialize
                implementation("org.jetbrains.kotlinx:kotlinx-serialization-runtime-native:0.20.0")

            }
        }
    }
}

val packForXcode by tasks.creating(Sync::class) {
    val targetDir = File(buildDir, "xcode-frameworks")

    /// selecting the right configuration for the iOS
    /// framework depending on the environment
    /// variables set by Xcode build
    val mode = System.getenv("CONFIGURATION") ?: "DEBUG"
    val framework = kotlin.targets
        .getByName<org.jetbrains.kotlin.gradle.plugin.mpp.KotlinNativeTarget>("ios")
        .binaries.getFramework(mode)
    inputs.property("mode", mode)
    dependsOn(framework.linkTask)

    from({ framework.outputDirectory })
    into(targetDir)

    /// generate a helpful ./gradlew wrapper with embedded Java path
    doLast {
        val gradlew = File(targetDir, "gradlew")
        gradlew.writeText("#!/bin/bash\n"
                + "export 'JAVA_HOME=${System.getProperty("java.home")}'\n"
                + "cd '${rootProject.rootDir}'\n"
                + "./gradlew \$@\n")
        gradlew.setExecutable(true)
    }
}

tasks.getByName("build").dependsOn(packForXcode)
