val ktlint by configurations.creating
val outputDir = "${project.buildDir}/reports/ktlint/"
val inputFiles = project.fileTree(mapOf("dir" to "src", "include" to "**/*.kt"))
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
        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
        versionCode = ReleaseConfig.versionCode
        versionName = ReleaseConfig.versionName
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

    implementation(project(":base"))
    implementation(project(":widgets"))    

    implementation("androidx.appcompat:appcompat:${Versions.appcompat}")
    implementation("androidx.constraintlayout:constraintlayout:${Versions.constraint}")
    implementation("androidx.core:core-ktx:${Versions.ktx}")
    implementation("androidx.recyclerview:recyclerview:${Versions.recyclerview}")
    implementation("androidx.room:room-runtime:${Versions.room}")
    implementation("androidx.room:room-rxjava2:${Versions.room}")
    implementation("com.android.support:multidex:${Versions.multidex}")
    implementation("com.facebook.stetho:stetho:${Versions.stetho}")
    implementation("com.jakewharton.timber:timber:${Versions.timber}")
    implementation("io.reactivex.rxjava2:rxandroid:${Versions.rxandroid}")
    implementation("io.reactivex.rxjava2:rxjava:${Versions.rxjava}")
    implementation("org.jetbrains.anko:anko-commons:${Versions.anko}")
    implementation("org.jetbrains.anko:anko-coroutines:${Versions.anko}")
    implementation("org.jetbrains.anko:anko-sdk27-coroutines:${Versions.anko}")
    implementation("org.jetbrains.anko:anko-sdk27:${Versions.anko}")
    implementation("org.koin:koin-android:${Versions.koin}")
    implementation("org.koin:koin-androidx-scope:${Versions.koin}")
    implementation("ru.terrakok.cicerone:cicerone:${Versions.cicerone}")    

    kapt("androidx.room:room-compiler:${Versions.room}")    

    testImplementation("androidx.room:room-testing:${Versions.Test.room}")
    testImplementation("com.nhaarman:mockito-kotlin:${Versions.Test.mockito}")
    testImplementation("com.squareup.okhttp3:mockwebserver:${Versions.okhttp}")
    testImplementation("junit:junit:${Versions.Test.junit}")
    testImplementation("org.amshove.kluent:kluent:${Versions.Test.kluent}")
    testImplementation("org.jetbrains.kotlin:kotlin-stdlib:${Versions.kotlin}")
    testImplementation("org.jetbrains.kotlin:kotlin-test-junit:${Versions.kotlin}")
    testImplementation("org.mockito:mockito-inline:2.21.0")
    testImplementation("org.robolectric:robolectric:${Versions.Test.robolectric}")    

    androidTestImplementation("androidx.test.espresso:espresso-core:${Versions.Test.espresso}")
    androidTestImplementation("androidx.test:runner:${Versions.androidx_runner}")
    androidTestImplementation("com.nhaarman:mockito-kotlin:${Versions.Test.mockito}")
}
dependencies {
    ktlint("com.github.shyiko:ktlint:0.30.0")    

    ktlint(project(":ktlintx"))
}
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