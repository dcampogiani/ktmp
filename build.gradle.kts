buildscript {
    repositories {
        gradlePluginPortal()
        jcenter()
        google()
        mavenCentral()
    }
    dependencies {
        classpath("org.jetbrains.kotlin:kotlin-gradle-plugin:1.8.0")
        classpath("com.android.tools.build:gradle:7.4.0")
    }
}
group = "com.danielecampogiani.ktmp"
version = "1.0-SNAPSHOT"

repositories {
    mavenCentral()
}