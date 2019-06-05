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
        versionCode = ReleaseConfig.versionCode
        versionName = ReleaseConfig.versionName

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
    }

    buildTypes {
        getByName("release") {
            isMinifyEnabled = true
            proguardFiles(getDefaultProguardFile("proguard-android.txt"), "proguard-rules.pro")
        }
    }
    lintOptions {
        isAbortOnError = false
        textReport = true
        textOutput("stdout")
        isCheckAllWarnings = true
        isWarningsAsErrors = false
    }

}

dependencies {

    implementation(fileTree(mapOf("dir" to "libs", "include" to listOf("*.jar"))))
    implementation("org.jetbrains.kotlin:kotlin-stdlib-jdk7:${Versions.kotlin}")
    implementation("androidx.appcompat:appcompat:${Versions.appcompat}")
    implementation("androidx.core:core-ktx:${Versions.coreKtx}")
    implementation("androidx.constraintlayout:constraintlayout:${Versions.constraint}")
    implementation("org.jetbrains.anko:anko-commons:${Versions.anko}")
    implementation("org.jetbrains.anko:anko-coroutines:${Versions.anko}")
    implementation("org.jetbrains.anko:anko-sdk27:${Versions.anko}")
    implementation("org.jetbrains.anko:anko-sdk27-coroutines:${Versions.anko}")
    implementation("io.reactivex.rxjava2:rxjava:${Versions.rxjava}")
    implementation("io.reactivex.rxjava2:rxandroid:${Versions.rxandroid}")
    implementation("androidx.recyclerview:recyclerview:${Versions.recyclerview}")

    api("com.caverock:androidsvg:${Versions.androidSvg}")
    api("com.github.bumptech.glide:glide:${Versions.glide}")
    api("com.github.bumptech.glide:okhttp3-integration:${Versions.okhttp3Integration}")
    kapt("com.github.bumptech.glide:compiler:${Versions.glide}")

    testImplementation("junit:junit:${Versions.Test.junit}")
    androidTestImplementation("androidx.test:runner:${Versions.Test.runner}")
    androidTestImplementation("androidx.test.espresso:espresso-core:${Versions.Test.espresso}")

    val ktlint by configurations.creating

    dependencies {
        ktlint("com.github.shyiko:ktlint:${Versions.ktlint}")
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
        args = listOf("-F", "src/main/**/*.kt")
    }

    tasks {
        preBuild {
            dependsOn(ktlintFormat)
        }
    }
}