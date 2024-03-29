// Top-level build file where you can add configuration options common to all sub-projects/modules.
plugins {
    id("com.android.application") version "8.1.1" apply false
    id("org.jetbrains.kotlin.android") version "1.9.0" apply false
    id("com.google.devtools.ksp") version "1.9.10-1.0.13" apply false

    id ("com.google.dagger.hilt.android") version "2.48.1" apply false
    id("androidx.navigation.safeargs") version "2.7.4" apply false
    id("com.google.android.libraries.mapsplatform.secrets-gradle-plugin") version "2.0.1" apply false

}
buildscript {

    repositories {
        google()
        mavenCentral()
    }
    buildscript {
        dependencies {
            classpath("com.google.android.libraries.mapsplatform.secrets-gradle-plugin:secrets-gradle-plugin:2.0.1")
        }
    }
}