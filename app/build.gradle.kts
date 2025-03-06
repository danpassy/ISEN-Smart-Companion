plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.kotlin.android)
    alias(libs.plugins.kotlin.compose)
    kotlin("kapt") // Enable KAPT
}

android {
    namespace = "fr.isen.boussougou.isensmartcompanion"
    compileSdk = 35

    defaultConfig {
        applicationId = "fr.isen.boussougou.isensmartcompanion"
        minSdk = 25
        targetSdk = 35
        versionCode = 1
        versionName = "1.0"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"

        val apiKey = providers.gradleProperty("GEMINI_API_KEY").orElse("AIzaSyDxdu9bnw2hVBEHgbzED3QOW-gqmRQAUec").get()
        buildConfigField("String", "GEMINI_API_KEY", "\"${apiKey}\"")

    }

    buildTypes {
        release {
            isMinifyEnabled = true
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
        buildConfig = true
    }
}

dependencies {

    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.lifecycle.runtime.ktx)
    implementation(libs.androidx.activity.compose)
    implementation(libs.androidx.ui)
    implementation(libs.androidx.ui.graphics)
    implementation(libs.androidx.ui.tooling.preview)
    implementation(libs.androidx.ui.tooling.preview)
    implementation(libs.androidx.material3)
    implementation(libs.androidx.navigation.compose)
    implementation(libs.androidx.material.icons.extended)
    implementation(platform(libs.androidx.compose.bom))

    // Room (Base de données locale)
    implementation("androidx.room:room-runtime:2.6.1")
    kapt("androidx.room:room-compiler:2.6.1")
    implementation("androidx.room:room-ktx:2.6.1")

    //Lifecycle
    implementation("androidx.lifecycle:lifecycle-viewmodel-ktx:2.8.0")
    implementation("androidx.lifecycle:lifecycle-runtime-ktx:2.8.0")


    implementation("androidx.compose.material:material-icons-extended:<version>")
    implementation("androidx.compose.material:material-icons-extended:1.5.4")

    //DataStore for Local store
    implementation("androidx.datastore:datastore-preferences:1.0.0")

    // WorkManager (tâches en arrière-plan)
    implementation("androidx.work:work-runtime-ktx:2.8.1")

    // Gemini API
    implementation("com.google.ai.client.generativeai:generativeai:0.9.0")

    implementation ("com.jakewharton.threetenabp:threetenabp:1.4.4")


    implementation("androidx.compose.foundation:foundation")

    // Retrofit & Gson pour les appels API
    implementation(libs.androidx.material.icons)
    implementation(libs.androidx.material.icons.extended)

    // Ajout des dépendances Retrofit et Gson Converter
    implementation(libs.retrofit)
    implementation(libs.converter.gson)

    //Test
    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)
    androidTestImplementation(platform(libs.androidx.compose.bom))
    androidTestImplementation(libs.androidx.ui.test.junit4)
    debugImplementation(libs.androidx.ui.tooling)
    debugImplementation(libs.androidx.ui.test.manifest)
}
