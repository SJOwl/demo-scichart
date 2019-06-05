plugins {
    id("com.android.library")
    kotlin("android")
    id("kotlin-android-extensions")
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
    api("com.aurelhubert:ahbottomnavigation:2.3.4")

    implementation(project(":base"))

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