plugins {
  alias(libs.plugins.android.application)
  id("org.jetbrains.kotlin.android")
  alias(libs.plugins.kotlin.serialization)
}

android {
    namespace = "com.example.gestionrendez_vous"
    compileSdk = 34
    defaultConfig {
        applicationId = "com.example.gestionrendez_vous"
        minSdk = 24
        targetSdk = 34
        versionCode = 1
        versionName = "1.0"
    }

    buildTypes {
        release {
            isMinifyEnabled = false
            proguardFiles(getDefaultProguardFile("proguard-android-optimize.txt"), "proguard-rules.pro")
        }
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_17
        targetCompatibility = JavaVersion.VERSION_17
    }
    buildFeatures {
      viewBinding = true
      aidl = false
      buildConfig = false
      shaders = false
    }

    packaging {
      resources {
        excludes += "/META-INF/{AL2.0,LGPL2.1}"
      }
    }
}

kotlin {
    jvmToolchain(17)
}

dependencies {
  // Core Android dependencies
  implementation(libs.androidx.core.ktx)
  implementation("androidx.appcompat:appcompat:1.6.1")
  implementation("com.google.android.material:material:1.11.0")
  implementation("androidx.constraintlayout:constraintlayout:2.1.4")
  
  // Arch Components
  implementation(libs.androidx.lifecycle.runtime.ktx)
  implementation("androidx.lifecycle:lifecycle-viewmodel-ktx:2.7.0")
  implementation("androidx.lifecycle:lifecycle-livedata-ktx:2.7.0")
  implementation("androidx.activity:activity-ktx:1.9.1")

  // Retrofit & Network
  implementation("com.squareup.retrofit2:retrofit:2.9.0")
  implementation("com.squareup.retrofit2:converter-gson:2.9.0")
  implementation("com.squareup.okhttp3:logging-interceptor:4.11.0")

  // Security Crypto
  implementation("androidx.security:security-crypto:1.1.0-alpha06")

  // Local tests: jUnit, coroutines, Android runner
  testImplementation(libs.junit)
  testImplementation(libs.kotlinx.coroutines.test)

  // Instrumented tests: jUnit rules and runners
  androidTestImplementation(libs.androidx.test.core)
  androidTestImplementation(libs.androidx.test.ext.junit)
  androidTestImplementation(libs.androidx.test.runner)
  androidTestImplementation(libs.androidx.test.espresso.core)
}
