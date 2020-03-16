val ktlint: Configuration by configurations.creating
val outputDir = "${project.buildDir}/reports/ktlint/"
val inputFiles = project.fileTree(mapOf("dir" to "src", "include" to "**/*.kt"))

plugins {
    id("com.android.library")
    kotlin("android")
    id("kotlin-android-extensions")
    kotlin("kapt")
}
android {
    compileSdkVersion(Versions.sdkCompile)
    defaultConfig {
        minSdkVersion(Versions.sdkMin)
        targetSdkVersion(Versions.sdkTarget)
        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
        versionCode = ReleaseConfig.versionCode
        versionName = ReleaseConfig.versionName
    }
    buildTypes {
        getByName("release") {
            isMinifyEnabled = true
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro"
            )
        }
    }
    lintOptions {
        isAbortOnError = false
        isCheckAllWarnings = true
        isWarningsAsErrors = false
        textOutput("stdout")
        textReport = true
    }
}
dependencies {
    implementation(fileTree(mapOf("dir" to "libs", "include" to listOf("*.jar"))))

    implementation(kotlin("stdlib-jdk7", Versions.kotlin))

    implementation("androidx.appcompat:appcompat:${Versions.appcompat}")
    implementation("androidx.constraintlayout:constraintlayout:${Versions.constraint}")
    implementation("androidx.core:core-ktx:${Versions.ktx}")
    implementation("androidx.recyclerview:recyclerview:${Versions.recyclerview}")
    implementation("androidx.room:room-runtime:${Versions.room}")
    implementation("androidx.room:room-rxjava2:${Versions.room}")
    implementation("androidx.transition:transition:${Versions.transitions}")
    implementation("com.android.support:multidex:${Versions.multidex}")
    implementation("com.facebook.stetho:stetho:${Versions.stetho}")
    implementation("com.github.nkzawa:socket.io-client:${Versions.socketIoClient}")
    implementation("com.google.code.gson:gson:${Versions.gson}")
    implementation("com.jakewharton.timber:timber:${Versions.timber}")
    implementation("com.squareup.okhttp3:logging-interceptor:${Versions.okhttp}")
    implementation("com.squareup.okhttp3:okhttp:${Versions.okhttp}")
    implementation("io.reactivex.rxjava2:rxandroid:${Versions.rxandroid}")
    implementation("io.reactivex.rxjava2:rxjava:${Versions.rxjava}")
    implementation("org.jetbrains.anko:anko-commons:${Versions.anko}")
    implementation("org.jetbrains.anko:anko-coroutines:${Versions.anko}")
    implementation("org.jetbrains.anko:anko-sdk27-coroutines:${Versions.anko}")
    implementation("org.jetbrains.anko:anko-sdk27:${Versions.anko}")
    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-core:${Versions.coroutines}")
    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-android:${Versions.coroutines}")
    implementation("org.koin:koin-android:${Versions.koin}")
    implementation("org.koin:koin-androidx-scope:${Versions.koin}")
    implementation("ru.terrakok.cicerone:cicerone:${Versions.cicerone}")

    api("com.caverock:androidsvg:${Versions.androidSvg}")
    api("com.github.bumptech.glide:glide:${Versions.glide}")
    api("com.github.bumptech.glide:okhttp3-integration:${Versions.okhttp3Integration}")
    api("com.google.android.material:material:${Versions.material}")
    api("com.hannesdorfmann:adapterdelegates4-kotlin-dsl:${Versions.adapterdelegates}")
    api("tech.schoolhelper:moxy-x-androidx:${Versions.moxyVersion}")
    api("tech.schoolhelper:moxy-x:${Versions.moxyVersion}")

    kapt("androidx.room:room-compiler:${Versions.room}")
    kapt("com.github.bumptech.glide:compiler:${Versions.glide}")
    kapt("tech.schoolhelper:moxy-x-compiler:${Versions.moxyVersion}")

    testImplementation("androidx.test.ext:junit:${Versions.Test.junitExt}")

    androidTestImplementation("androidx.test.espresso:espresso-core:${Versions.Test.espresso}")
    androidTestImplementation("androidx.test:runner:${Versions.Test.runner}")
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
    args = listOf("-F", "src/main/**/*.kt")
}
tasks {
    preBuild {
        dependsOn(ktlintFormat)
    }
}