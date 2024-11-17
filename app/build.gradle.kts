import com.android.build.gradle.internal.api.BaseVariantOutputImpl
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.jetbrains.kotlin.android)

    id("kotlin-kapt")
    id("com.google.dagger.hilt.android")
}

android {
    namespace = "com.jddev.simplemusic"
    compileSdk = 35

    defaultConfig {
        applicationId = "com.jddev.simplemusic"
        minSdk = 26
        targetSdk = 35
        versionCode = 1
        versionName = "1.0.0"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
        vectorDrawables {
            useSupportLibrary = true
        }
    }

    buildTypes {
        release {
            isMinifyEnabled = false
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro"
            )
        }

        debug {
            versionNameSuffix = "_debug"
            isMinifyEnabled = false
            isShrinkResources = false
            isDebuggable = true
        }

        applicationVariants.all {
            outputs.all {
                (this as? BaseVariantOutputImpl)?.outputFileName =
                    "SimpleMusic_${versionName}_${
                        SimpleDateFormat("yyMMdd", Locale.US).format(
                            Date()
                        )
                    }.apk"
            }
        }
    }

    flavorDimensions += "app_type"
    val dev = "dev"
    val prd = "prd"
    productFlavors {
        create(dev) {
            dimension = "app_type"
            applicationIdSuffix = ".dev"
            versionNameSuffix = "-dev"
            buildConfigField("String", "DEV_MODE", "\"dev\"")
            resValue("string", "app_name", "Simple Music Dev")
        }
        create(prd) {
            dimension = "app_type"
            versionNameSuffix = "-prd"
            buildConfigField("String", "DEV_MODE", "\"prd\"")
        }
    }

    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_1_8
        targetCompatibility = JavaVersion.VERSION_1_8
    }
    kotlinOptions {
        jvmTarget = "1.8"
    }
    buildFeatures {
        buildConfig = true
        compose = true
    }
    composeOptions {
        kotlinCompilerExtensionVersion = "1.5.1"
    }
    packaging {
        resources {
            excludes += "/META-INF/{AL2.0,LGPL2.1}"
        }
    }
}

dependencies {
    // simple-touch ui
    implementation ("com.github.josephdinhtan.android-core-architecture-lite:simpletouch-ui:1.0.0")
    implementation ("com.github.josephdinhtan.android-core-architecture-lite:simpletouch-utils:1.0.0")

    // ui module
    implementation(libs.androidx.material.icon)

    // navigation
    implementation(libs.androidx.navigation)

    // hilt
    implementation(libs.hilt.android)
    implementation(libs.androidx.hilt.navigation.compose)
    kapt(libs.hilt.android.compiler)
    // room
    implementation(libs.androidx.room.common)
    implementation(libs.androidx.room.ktx)
    kapt(libs.androidx.room.compiler)

    implementation(libs.timber)
    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.lifecycle.runtime.ktx)
    implementation(libs.androidx.activity.compose)
    implementation(platform(libs.androidx.compose.bom))
    implementation(libs.androidx.ui)
    implementation(libs.androidx.ui.graphics)
    implementation(libs.androidx.ui.tooling.preview)
    implementation(libs.androidx.material3)

    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)
    androidTestImplementation(platform(libs.androidx.compose.bom))
    androidTestImplementation(libs.androidx.ui.test.junit4)
    debugImplementation(libs.androidx.ui.tooling)
    debugImplementation(libs.androidx.ui.test.manifest)
}