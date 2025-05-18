
plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.kotlin.android)
    alias(libs.plugins.kotlin.compose)
    alias(libs.plugins.google.services)
}

android {
    namespace = "com.project"
    compileSdk = 35

    defaultConfig {
        applicationId = "com.project"
        minSdk = 25
        targetSdk = 35
        versionCode = 1
        versionName = "1.0"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
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
        sourceCompatibility = JavaVersion.VERSION_11
        targetCompatibility = JavaVersion.VERSION_11
    }
    kotlinOptions {
        jvmTarget = "11"
    }
    buildFeatures {
        compose = true
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

   // implementation(libs.firebase.crashlytics.ktx)
    implementation(libs.firebase.auth.ktx)
    implementation(platform(libs.firebase.bom))
    implementation(libs.firebase.firestore.ktx)



    implementation(libs.androidx.navigation.compose)
    implementation(libs.androidx.graphics.shapes.android)
    implementation(libs.androidx.material.icons.extended)

    implementation(libs.koin.android)
    implementation(libs.koin.compose)
    implementation(libs.koin.navigation)
    implementation(libs.mockito.core)
    implementation(libs.mockito.android)
    implementation(libs.androidx.ui.text.google.fonts)
    implementation(libs.secrets.gradle.plugin)
    implementation(libs.places)
    implementation(libs.places.ktx)
    implementation(libs.material)
    implementation(libs.maps.compose)
    implementation(libs.play.services.maps)
    implementation(libs.androidx.foundation)
    //    implementation(libs.ads.mobile.sdk)
//    implementation(libs.androidx.datastore.preferences)

    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)
    androidTestImplementation(platform(libs.androidx.compose.bom))
    androidTestImplementation(libs.androidx.ui.test.junit4)
    debugImplementation(libs.androidx.ui.tooling)
    debugImplementation(libs.androidx.ui.test.manifest)
}