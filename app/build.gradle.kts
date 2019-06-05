plugins {
    id("com.android.application")
    id("kotlin-android")
    id("kotlin-android-extensions")
    kotlin("kapt")
}

android {
    compileSdkVersion(Versions.sdkCompile)
    defaultConfig {
        applicationId = ReleaseConfig.applicationId
        minSdkVersion(Versions.sdkMin)
        targetSdkVersion(Versions.sdkTarget)
        versionCode = ReleaseConfig.versionCode
        versionName = ReleaseConfig.versionName
        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
    }
    buildTypes {
        getByName("release") {
            isMinifyEnabled = true
            isShrinkResources = true
            proguardFiles(getDefaultProguardFile("proguard-android.txt"), "proguard-rules.pro")
        }
    }
}

dependencies {
    implementation(fileTree(mapOf("dir" to "libs", "include" to listOf("*.jar"))))
    implementation(kotlin("stdlib-jdk7", Versions.kotlin))

    implementation(project(":widgets"))
    implementation(project(":base"))

    implementation("androidx.appcompat:appcompat:${Versions.appcompat}")
    implementation("com.android.support:multidex:${Versions.multidex}")
    implementation("androidx.core:core-ktx:${Versions.ktx}")
    implementation("androidx.recyclerview:recyclerview:${Versions.recyclerview}")
    implementation("androidx.constraintlayout:constraintlayout:${Versions.constraint}")
    implementation("org.jetbrains.anko:anko-commons:${Versions.anko}")
    implementation("org.jetbrains.anko:anko-coroutines:${Versions.anko}")
    implementation("org.jetbrains.anko:anko-sdk27:${Versions.anko}")
    implementation("org.jetbrains.anko:anko-sdk27-coroutines:${Versions.anko}")
    implementation("org.koin:koin-android:${Versions.koin}")
    implementation("org.koin:koin-androidx-scope:${Versions.koin}")
    implementation("io.reactivex.rxjava2:rxjava:${Versions.rxjava}")
    implementation("io.reactivex.rxjava2:rxandroid:${Versions.rxandroid}")
    implementation("ru.terrakok.cicerone:cicerone:${Versions.cicerone}")
    implementation("com.jakewharton.timber:timber:${Versions.timber}")
    implementation("androidx.room:room-runtime:${Versions.room}")
    kapt("androidx.room:room-compiler:${Versions.room}")
    implementation("androidx.room:room-rxjava2:${Versions.room}")
    implementation("com.facebook.stetho:stetho:${Versions.stetho}")

    testImplementation("androidx.room:room-testing:${Versions.Test.room}")
    testImplementation("junit:junit:${Versions.Test.junit}")
    testImplementation("com.squareup.okhttp3:mockwebserver:${Versions.okhttp}")
    testImplementation("org.jetbrains.kotlin:kotlin-stdlib:${Versions.kotlin}")
    testImplementation("org.jetbrains.kotlin:kotlin-test-junit:${Versions.kotlin}")
    testImplementation("org.amshove.kluent:kluent:${Versions.Test.kluent}")
    testImplementation("com.nhaarman:mockito-kotlin:${Versions.Test.mockito}")
    testImplementation("org.robolectric:robolectric:${Versions.Test.robolectric}")
    testImplementation("org.mockito:mockito-inline:2.21.0")

    androidTestImplementation("com.nhaarman:mockito-kotlin:${Versions.Test.mockito}")
    androidTestImplementation("androidx.test:runner:${Versions.androidx_runner}")
    androidTestImplementation("androidx.test.espresso:espresso-core:${Versions.Test.espresso}")
}

val ktlint by configurations.creating

dependencies {
    ktlint("com.github.shyiko:ktlint:0.30.0")
    ktlint(project(":ktlintx"))
}

val outputDir = "${project.buildDir}/reports/ktlint/"
val inputFiles = project.fileTree(mapOf("dir" to "src", "include" to "**/*.kt"))

val ktlintFormat by tasks.creating(JavaExec::class) {
    inputs.files(inputFiles)
    outputs.dir(outputDir)

    description = "Fix Kotlin code style deviations."
    classpath = ktlint
    main = "com.github.shyiko.ktlint.Main"
    args = listOf("-F", "src/**/*.kt")
}

tasks {
    preBuild {
        dependsOn(ktlintFormat)
    }
}