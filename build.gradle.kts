buildscript {
    repositories {
        gradlePluginPortal()
        jcenter()
        google()
        mavenCentral()
    }
    dependencies {
        classpath("org.jetbrains.kotlin:kotlin-gradle-plugin:1.7.21")
        classpath("com.android.tools.build:gradle:7.3.1")
    }
}
group = "com.danielecampogiani.ktmp"
version = "1.0-SNAPSHOT"

repositories {
    mavenCentral()
}