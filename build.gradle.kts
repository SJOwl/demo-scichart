// Top-level build file where you can add configuration options common to all sub-projects/modules.
buildscript {
    repositories {
        google()
        jcenter()
    }
    dependencies {
        classpath("com.android.tools.build:gradle:${Versions.gradle}")
        classpath("com.pinterest:ktlint:${Versions.ktlint}")
        classpath("org.jetbrains.kotlin:kotlin-gradle-plugin:${Versions.kotlin}")
    }
}
plugins {
    id("com.gradle.build-scan") version "2.2.1"
}
buildScan {
    termsOfServiceUrl = "https://gradle.com/terms-of-service"
    setTermsOfServiceAgree("yes")
}
allprojects {
    repositories {
        google()
        jcenter()
    }
}
tasks {
    register("clean", Delete::class) {
        delete(buildDir)
    }
}