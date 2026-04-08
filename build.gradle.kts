plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.kotlin.android)
}

android {
    namespace = "info.ails.myapps"
    compileSdk = 36

    defaultConfig {
        applicationId = "info.ails.myapps"
        minSdk = 33
        targetSdk = 36
        versionCode = 7
        versionName = "1.3.4"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
    }

    signingConfigs {
        create("release") {
            // Use environment variables for CI/CD
            val ksPath = System.getenv("KEYSTORE_PATH")
            val ksPassword = System.getenv("KEYSTORE_PASSWORD")
            val ksAlias = System.getenv("KEY_ALIAS")
            val ksKeyPassword = System.getenv("KEY_PASSWORD")

            if (ksPath != null) storeFile = file(ksPath)
            if (ksPassword != null) storePassword = ksPassword
            if (ksAlias != null) keyAlias = ksAlias
            if (ksKeyPassword != null) keyPassword = ksKeyPassword
        }
    }

    buildTypes {
        release {
            isMinifyEnabled = false
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro"
            )
            signingConfig = signingConfigs.getByName("release")
        }
    }

    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_11
        targetCompatibility = JavaVersion.VERSION_11
    }

    kotlin {
        jvmToolchain(11)
    }

    buildFeatures {
        viewBinding = true
    }
}

dependencies {
    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.appcompat)
    implementation(libs.material)
    implementation(libs.androidx.constraintlayout)
    implementation(libs.androidx.lifecycle.livedata.ktx)
    implementation(libs.androidx.lifecycle.viewmodel.ktx)
    implementation(libs.androidx.navigation.fragment.ktx)
    implementation(libs.androidx.navigation.ui.ktx)
    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)
}
