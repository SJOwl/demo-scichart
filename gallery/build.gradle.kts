val ktlint by configurations.creating
val outputDir = "${project.buildDir}/reports/ktlint/"
val inputFiles = project.fileTree(mapOf("dir" to "src", "include" to "**/*.kt"))
plugins {
    id("com.android.application")
    kotlin("android")
    id("kotlin-android-extensions")
    kotlin("kapt")
}
android {
    compileSdkVersion(Versions.sdkCompile)
    defaultConfig {
        applicationId = "bare.bones.project.gallery"
        minSdkVersion(Versions.sdkMin)
        targetSdkVersion(Versions.sdkTarget)
        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
        versionCode = ReleaseConfig.versionCode
        versionName = ReleaseConfig.versionName
    }
    lintOptions {
        isAbortOnError = false
        isCheckAllWarnings = true
        isWarningsAsErrors = false
        textOutput("stdout")
        textReport = true
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

    implementation(project(":base"))
    implementation(project(":widgets"))    

    implementation("androidx.appcompat:appcompat:${Versions.appcompat}")
    implementation("androidx.constraintlayout:constraintlayout:${Versions.constraint}")
    implementation("androidx.core:core-ktx:${Versions.coreKtx}")
    implementation("org.jetbrains.anko:anko-commons:${Versions.anko}")
    implementation("org.jetbrains.anko:anko-coroutines:${Versions.anko}")
    implementation("org.jetbrains.anko:anko-sdk27-coroutines:${Versions.anko}")
    implementation("org.jetbrains.anko:anko-sdk27:${Versions.anko}")
    implementation("org.jetbrains.kotlin:kotlin-stdlib-jdk7:${Versions.kotlin}")    

    testImplementation("junit:junit:${Versions.Test.junit}")    

    androidTestImplementation("androidx.test.espresso:espresso-core:${Versions.Test.espresso}")
    androidTestImplementation("androidx.test:runner:${Versions.Test.runner}")
}
dependencies {
    ktlint("com.github.shyiko:ktlint:${Versions.ktlint}")    

    ktlint(project(":ktlintx"))
}
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