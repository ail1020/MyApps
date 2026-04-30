plugins {
    alias(libs.plugins.android.application)
}

android {
    namespace = "info.ails.myapps"
    compileSdk = 36

    defaultConfig {
        applicationId = "info.ails.myapps"
        minSdk = 35
        targetSdk = 36
        versionCode = 7
        versionName = "1.3.4"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
    }

    signingConfigs {
        create("release") {
            val ksPath = System.getenv("KEYSTORE_PATH")
            val ksPassword = System.getenv("KEYSTORE_PASSWORD")
            val ksAlias = System.getenv("KEY_ALIAS")

            if (ksPath != null) storeFile = file(ksPath)
            if (ksPassword != null) storePassword = ksPassword
            if (ksAlias != null) keyAlias = ksAlias

            // Use store password for key if no separate key password
            if (ksPassword != null) keyPassword = ksPassword

            // Explicitly set PKCS12 if your keystore is in that format
            storeType = "PKCS12"
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
        sourceCompatibility = JavaVersion.VERSION_21
        targetCompatibility = JavaVersion.VERSION_21
    }

    buildFeatures {
        viewBinding = true
    }
    compileSdkMinor = 1
    buildToolsVersion = "36.1.0"
}

kotlin {
    jvmToolchain(21)
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
