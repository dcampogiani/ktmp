buildscript {
    repositories {
        gradlePluginPortal()
        jcenter()
        google()
        mavenCentral()
    }
    dependencies {
        classpath("org.jetbrains.kotlin:kotlin-gradle-plugin:1.6.10")
        classpath("com.android.tools.build:gradle:7.1.2")
    }
}
group = "com.danielecampogiani.ktmp"
version = "1.0-SNAPSHOT"

repositories {
    mavenCentral()
}