/*plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.jetbrains.kotlin.android)
//    alias(libs.plugins.kapt) // Hilt - Kotlin Kapt
 //  alias(libs.plugins.hilt) // Hilt - Dagger Hilt

}*/

plugins {
    id("com.android.application")
    id("org.jetbrains.kotlin.android")
    id("kotlin-kapt")
    id("com.google.dagger.hilt.android")

}

android {
    namespace = "com.tappytaps.storky"
    compileSdk = 34

    defaultConfig {
        applicationId = "com.tappytaps.storky"
        minSdk = 23
        targetSdk = 34
        versionCode = 1
        versionName = "1.0"

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
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_1_8
        targetCompatibility = JavaVersion.VERSION_1_8
    }
    kotlinOptions {
        jvmTarget = "1.8"
    }
    buildFeatures {
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

    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.lifecycle.runtime.ktx)
    implementation(libs.androidx.activity.compose)
    implementation(platform(libs.androidx.compose.bom))
    implementation(libs.androidx.ui)
    implementation(libs.androidx.ui.graphics)
    implementation(libs.androidx.ui.tooling.preview)
    implementation(libs.androidx.material3)
    implementation(libs.androidx.runtime.livedata)

    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)
    androidTestImplementation(platform(libs.androidx.compose.bom))
    androidTestImplementation(libs.androidx.ui.test.junit4)
    debugImplementation(libs.androidx.ui.tooling)
    debugImplementation(libs.androidx.ui.test.manifest)


    implementation("androidx.hilt:hilt-navigation-compose:1.2.0") //hilt
    implementation("com.google.dagger:hilt-android:2.51.1") //hilt
    kapt("com.google.dagger:hilt-android-compiler:2.51.1") //hilt


    implementation("androidx.room:room-runtime:2.6.1") //room
    annotationProcessor("androidx.room:room-compiler:2.6.1") //room
    implementation("androidx.room:room-ktx:2.6.1") //room
    kapt("androidx.room:room-compiler:2.6.1") //room

    implementation("io.coil-kt:coil-compose:2.1.0") //pro načítání Vector Drawable.

    implementation("androidx.compose.material3:material3:1.2.1")
    implementation("androidx.compose.material3:material3-window-size-class:1.2.1")
    implementation("androidx.compose.material3:material3-adaptive-navigation-suite:1.3.0-beta04")

    implementation("com.google.accompanist:accompanist-pager:0.35.1-alpha")
    implementation("com.google.accompanist:accompanist-pager-indicators:0.35.1-alpha")

    implementation("com.itextpdf:itextpdf:5.5.13.4")

    implementation("com.google.android.gms:play-services-ads:23.2.0")


    

}


kapt {
    correctErrorTypes = true
}

