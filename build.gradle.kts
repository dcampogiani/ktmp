buildscript {
    repositories {
        gradlePluginPortal()
        jcenter()
        google()
        mavenCentral()
    }
    dependencies {
        classpath("org.jetbrains.kotlin:kotlin-gradle-plugin:1.7.22")
        classpath("com.android.tools.build:gradle:8.1.3")
    }
}
group = "com.danielecampogiani.ktmp"
version = "1.0-SNAPSHOT"

repositories {
    mavenCentral()
}