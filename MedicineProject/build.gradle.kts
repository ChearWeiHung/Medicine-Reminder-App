// Top-level build file where you can add configuration options common to all sub-projects/modules.
// Top-level build file where you can add configuration options common to all sub-projects/modules.
plugins {
    alias(libs.plugins.android.application) apply false
    alias(libs.plugins.kotlin.android) apply false
    alias(libs.plugins.google.gms.google.services) apply false
}
buildscript {
    repositories {
        google()          // Google repository for Android libraries
        mavenCentral()    // Maven Central repository for other libraries
    }
    dependencies {
        classpath ("com.android.tools.build:gradle:8.0.2" )// Example classpath for Android Gradle plugin
        val nav_version = "2.8.3"
        classpath("androidx.navigation:navigation-safe-args-gradle-plugin:$nav_version")
        classpath("com.google.gms:google-services:4.4.2")

    }
}