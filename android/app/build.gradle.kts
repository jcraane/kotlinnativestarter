plugins {
    id("com.android.application")
    id("org.jetbrains.kotlin.android")
    id("kotlin-android-extensions")
    kotlin("kapt")
}

val lifeCycleVersion = "2.1.0-alpha03"
val coroutinesVersion = "1.4.1"
val koinVersion = "1.0.2"

android {
    compileSdkVersion(28)
    defaultConfig {
        applicationId = "nl.jamiecraane.multiplatform.myapp"
        minSdkVersion(22)
        targetSdkVersion(28)
        versionCode = 1
        versionName = "1.0"
        testInstrumentationRunner = "android.support.test.runner.AndroidJUnitRunner"
    }
    buildTypes {
        getByName("release") {
            isMinifyEnabled = false
            proguardFiles(getDefaultProguardFile("proguard-android-optimize.txt"), "proguard-rules.pro")
        }
    }
    buildTypes {
        getByName("debug") {
            isMinifyEnabled = false
            applicationIdSuffix = ".debug"
            isShrinkResources = false
        }
    }
    dataBinding {
        isEnabled = true
    }
    packagingOptions {
        exclude("META-INF/common.kotlin_module")
        exclude("META-INF/*.kotlin_module")
    }
    configurations.all {
        exclude(
            group = "com.soywiz.korlibs.klock", module = "klock-android-debug"
        )
        exclude(
            group = "com.soywiz.korlibs.klock", module = "klock-android"
        )
    }
}

dependencies {
    implementation(project(":common"))
    implementation(fileTree(mapOf("dir" to "libs", "include" to listOf("*.jar"))))
    implementation("org.jetbrains.kotlin:kotlin-stdlib")
//    implementation("\"org.jetbrains.kotlin:kotlin-stdlib:1.4-M2")
    implementation("androidx.constraintlayout:constraintlayout:1.1.3")
    implementation("com.google.code.gson:gson:2.8.5")
    implementation("androidx.appcompat:appcompat:1.1.0")
    implementation("com.jakewharton.timber:timber:4.7.1")
    implementation("com.google.code.gson:gson:2.8.5")
    implementation("androidx.lifecycle:lifecycle-extensions:$lifeCycleVersion")
    implementation("androidx.lifecycle:lifecycle-viewmodel-ktx:$lifeCycleVersion")
    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-core:$coroutinesVersion")
    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-android:$coroutinesVersion")
    implementation("org.koin:koin-android:$koinVersion")
    implementation("org.koin:koin-androidx-viewmodel:$koinVersion")
    kapt("androidx.lifecycle:lifecycle-compiler:$lifeCycleVersion")
    testImplementation("junit:junit:4.12")
    testImplementation("org.jetbrains.kotlinx:kotlinx-coroutines-test:$coroutinesVersion")
    androidTestImplementation("com.android.support.test:runner:1.0.2")
    androidTestImplementation("com.android.support.test.espresso:espresso-core:3.0.2")
}

