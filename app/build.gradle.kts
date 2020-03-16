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

    implementation("android.arch.lifecycle:extensions:${Versions.arch_extentions}")
    implementation("androidx.appcompat:appcompat:${Versions.appcompat}")
    implementation("androidx.constraintlayout:constraintlayout:${Versions.constraint}")
    implementation("androidx.core:core-ktx:${Versions.ktx}")
    implementation("androidx.media:media:${Versions.androidx_media}")
    implementation("androidx.recyclerview:recyclerview:${Versions.recyclerview}")
    implementation("androidx.room:room-ktx:${Versions.room}")
    implementation("androidx.room:room-runtime:${Versions.room}")
    implementation("androidx.room:room-rxjava2:${Versions.room}")
    implementation("androidx.work:work-runtime-ktx:${Versions.workManager}")
    implementation("com.android.support:multidex:${Versions.multidex}")
    implementation("com.arthenica:mobile-ffmpeg-full:${Versions.ffmpeg}")
    implementation("com.crashlytics.sdk.android:crashlytics:${Versions.crashlytics}")
    implementation("com.facebook.stetho:stetho:${Versions.stetho}")
    implementation("com.google.android.gms:play-services-auth:${Versions.play_services_auth}")
    implementation("com.google.android.material:material:${Versions.material}")
    implementation("com.google.code.gson:gson:${Versions.gson}")
    implementation("com.google.firebase:firebase-analytics:${Versions.firebaseAnalytics}")
    implementation("com.google.firebase:firebase-firestore:${Versions.firestore}")
    implementation("com.google.firebase:firebase-perf:${Versions.firebase_perf}")
    implementation("com.jakewharton.retrofit:retrofit2-kotlin-coroutines-adapter:${Versions.retrofit2_kotlin_coroutines_adapter}")
    implementation("com.jakewharton.timber:timber:${Versions.timber}")
    implementation("com.jakewharton:process-phoenix:${Versions.processPhoenix}")
    implementation("com.squareup.okhttp3:logging-interceptor:${Versions.okhttp}")
    implementation("com.squareup.okhttp3:okhttp:${Versions.okhttp}")
    implementation("com.squareup.retrofit2:adapter-rxjava2:${Versions.retrofit}")
    implementation("com.squareup.retrofit2:converter-gson:${Versions.retrofit}")
    implementation("com.squareup.retrofit2:converter-moshi:${Versions.retrofit}")
    implementation("com.squareup.retrofit2:retrofit:${Versions.retrofit}")
    implementation("io.reactivex.rxjava2:rxandroid:${Versions.rxandroid}")
    implementation("io.reactivex.rxjava2:rxjava:${Versions.rxjava}")
    implementation("jp.wasabeef:glide-transformations:4.0.0")
    implementation("org.jetbrains.anko:anko-commons:${Versions.anko}")
    implementation("org.jetbrains.anko:anko-coroutines:${Versions.anko}")
    implementation("org.jetbrains.anko:anko-sdk27-coroutines:${Versions.anko}")
    implementation("org.jetbrains.anko:anko-sdk27:${Versions.anko}")
    implementation("dev.scottpierce.kotlin-html:kotlin-html-writer-jvm:0.7.22")
    implementation("org.jsoup:jsoup:${Versions.jsoup}")
    implementation("org.koin:koin-android:${Versions.koin}")
    implementation("org.koin:koin-androidx-scope:${Versions.koin}")
    implementation("ru.terrakok.cicerone:cicerone:${Versions.cicerone}")
    implementation(group = "commons-lang", name = "commons-lang", version = "2.6")

    debugImplementation("com.squareup.leakcanary:leakcanary-android:2.2")

    kapt("androidx.room:room-compiler:${Versions.room}")
    kapt("tech.schoolhelper:moxy-x-compiler:${Versions.moxyVersion}")

    testImplementation("androidx.test.ext:junit:${Versions.Test.junitExt}")
    testImplementation("com.nhaarman.mockitokotlin2:mockito-kotlin:${Versions.Test.mockitoNHaarmann}")
    testImplementation("com.squareup.okhttp3:mockwebserver:${Versions.okhttp}")
    testImplementation("org.amshove.kluent:kluent:${Versions.Test.kluent}")
    testImplementation("org.hamcrest:hamcrest:2.2")
    testImplementation("org.jetbrains.anko:anko-coroutines:${Versions.anko}")
    testImplementation("org.jetbrains.anko:anko-sdk27-coroutines:${Versions.anko}")
    testImplementation("org.jetbrains.kotlin:kotlin-stdlib:${Versions.kotlin}")
    testImplementation("org.jetbrains.kotlin:kotlin-test-junit:${Versions.kotlin}")
    testImplementation("org.jetbrains.kotlinx:kotlinx-coroutines-android:${Versions.coroutines}")
    testImplementation("org.jetbrains.kotlinx:kotlinx-coroutines-core:${Versions.coroutines}")
    testImplementation("org.jetbrains.kotlinx:kotlinx-coroutines-test:${Versions.coroutines}")
    testImplementation("org.koin:koin-test:${Versions.Test.koin}")
    testImplementation("org.mockito:mockito-inline:${Versions.Test.mockitoInline}")
    testImplementation("org.robolectric:robolectric:${Versions.Test.robolectric}")

    androidTestImplementation("android.arch.persistence.room:testing:${Versions.Test.room}")
    androidTestImplementation("androidx.test.espresso.idling:idling-concurrent:${Versions.Test.espresso}")
    androidTestImplementation("androidx.test.espresso:espresso-accessibility:${Versions.Test.espresso}")
    androidTestImplementation("androidx.test.espresso:espresso-contrib:${Versions.Test.espresso}")
    androidTestImplementation("androidx.test.espresso:espresso-core:${Versions.Test.espresso}")
    androidTestImplementation("androidx.test.espresso:espresso-idling-resource:${Versions.Test.espresso}")
    androidTestImplementation("androidx.test.espresso:espresso-intents:${Versions.Test.espresso}")
    androidTestImplementation("androidx.test.espresso:espresso-web:${Versions.Test.espresso}")
    androidTestImplementation("androidx.test.ext:junit:${Versions.Test.junitExt}")
    androidTestImplementation("androidx.test:core:${Versions.Test.core}")
    androidTestImplementation("androidx.test:rules:${Versions.Test.rules}")
    androidTestImplementation("androidx.test:runner:${Versions.androidx_runner}")
    androidTestImplementation("androidx.test:runner:${Versions.Test.runner}")
    androidTestImplementation("androidx.work:work-testing:${Versions.workManager}")
    androidTestImplementation("com.nhaarman.mockitokotlin2:mockito-kotlin:${Versions.Test.mockitoNHaarmann}")
    androidTestImplementation("org.koin:koin-test:${Versions.Test.koin}")
    androidTestImplementation("org.mockito:mockito-inline:${Versions.Test.mockitoInline}")
}
dependencies {
    ktlint("com.pinterest:ktlint:${Versions.ktlint}")

    ktlint(project(":ktlintx"))
}
val ktlintFormat by tasks.creating(JavaExec::class) {
    inputs.files(inputFiles)
    outputs.dir(outputDir)
    description = "Fix Kotlin code style deviations."
    classpath = ktlint
    main = "com.pinterest.ktlint.Main"
    args = listOf("-F", "src/**/*.kt")
}
tasks {
    preBuild {
        dependsOn(ktlintFormat)
    }
}