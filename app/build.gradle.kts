plugins {
    alias(libs.plugins.android.application)
}

android {
    namespace = "com.project.domotique"
    compileSdk {
        version = release(36)
    }

    defaultConfig {
        applicationId = "com.project.domotique"
        minSdk = 24
        targetSdk = 36
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
}

dependencies {
    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.appcompat)
    implementation(libs.material)
    implementation(libs.androidx.activity)
    implementation(libs.androidx.constraintlayout)

    //noinspection UseTomlInstead
    implementation("androidx.fragment:fragment-ktx:1.8.9")
    //noinspection UseTomlInstead
    implementation("androidx.lifecycle:lifecycle-viewmodel-ktx:2.10.0")
    //noinspection UseTomlInstead
    implementation("com.google.code.gson:gson:2.13.2")
    //noinspection UseTomlInstead
    implementation("com.github.bumptech.glide:glide:5.0.5")
    //noinspection UseTomlInstead
    implementation("androidx.navigation:navigation-fragment-ktx:2.9.7")
    //noinspection UseTomlInstead
    implementation("androidx.navigation:navigation-ui-ktx:2.9.7")
    implementation(libs.androidx.lifecycle.livedata.ktx)

    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)
}