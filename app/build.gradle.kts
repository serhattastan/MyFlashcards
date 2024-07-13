plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.jetbrains.kotlin.android)
    alias(libs.plugins.google.gms.google.services)
    id("androidx.navigation.safeargs.kotlin") // Navigation Safe Args eklentisi
    id("kotlin-kapt") // Kotlin Kapt (Annotation Processing Tool) eklentisi
    id("com.google.dagger.hilt.android")
    alias(libs.plugins.google.firebase.crashlytics) // Dagger Hilt eklentisi
}

android {
    namespace = "com.cloffygames.myflashcards"
    compileSdk = 34

    buildFeatures {
        viewBinding = true // View Binding etkinleştirilmesi
        dataBinding = true // Data Binding etkinleştirilmesi
    }

    defaultConfig {
        applicationId = "com.cloffygames.myflashcards"
        minSdk = 30
        targetSdk = 34
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
        sourceCompatibility = JavaVersion.VERSION_1_8
        targetCompatibility = JavaVersion.VERSION_1_8
    }
    kotlinOptions {
        jvmTarget = "1.8"
    }
}

dependencies {

    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.appcompat)
    implementation(libs.material)
    implementation(libs.androidx.activity)
    implementation(libs.androidx.constraintlayout)
    implementation(libs.firebase.auth)
    implementation(libs.firebase.crashlytics)
    implementation(libs.firebase.firestore)
    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)

    implementation(libs.play.services.auth) // Google Play Services Auth kütüphanesi
    implementation(platform(libs.firebase.bom)) // Firebase BOM (Bill of Materials)

    implementation(libs.androidx.room.runtime) // Room Runtime kütüphanesi
    implementation(libs.androidx.room.ktx) // Room KTX uzantıları
    kapt(libs.androidx.room.compiler) // Room Compiler (Annotation Processing) kütüphanesi

    implementation(libs.androidx.navigation.fragment.ktx) // Navigation Fragment KTX kütüphanesi
    implementation(libs.androidx.navigation.ui.ktx) // Navigation UI KTX kütüphanesi

    implementation(libs.hilt.android) // Dagger Hilt kütüphanesi
    kapt(libs.hilt.android.compiler) // Dagger Hilt Compiler kütüphanesi

    implementation(libs.gson) // Gson kütüphanesi
    implementation(libs.androidx.work.runtime.ktx) // WorkManager KTX kütüphanesi
}