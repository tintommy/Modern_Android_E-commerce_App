buildscript {
    dependencies {
        classpath("com.google.gms:google-services:4.4.0")
        classpath("com.google.dagger:hilt-android-gradle-plugin:2.48.1")
        val nav_version = "2.7.4"
        classpath("androidx.navigation:navigation-safe-args-gradle-plugin:$nav_version")

    }
    repositories {
        // other repositories...
        mavenCentral()
        google()
        maven {url = uri ("https://jitpack.io")}
    }

}




// Top-level build file where you can add configuration options common to all sub-projects/modules.
plugins {
    id("com.android.application") version "8.3.1" apply false
    id("org.jetbrains.kotlin.android") version "1.9.0" apply false
    id("com.google.dagger.hilt.android") version "2.44" apply false
}