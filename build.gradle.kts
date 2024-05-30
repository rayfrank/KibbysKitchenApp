plugins {
    id("com.android.application") version "8.1.4" apply false
    id("com.android.library") version "8.1.4" apply false
}

buildscript {
    repositories {
        google()
        mavenCentral()
        maven(url = "https://jitpack.io")
    }
    dependencies {
        classpath("com.google.gms:google-services:4.4.1")
    }
}


