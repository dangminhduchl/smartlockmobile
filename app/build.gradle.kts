plugins {
    id("com.android.application")
}

android {
    namespace = "com.example.smartlockjava"
    compileSdk = 34

    defaultConfig {
        applicationId = "com.example.smartlockjava"
        minSdk = 24
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
        sourceCompatibility = JavaVersion.VERSION_17
        targetCompatibility = JavaVersion.VERSION_17
    }
    buildFeatures {
        viewBinding = true
    }
    buildToolsVersion = "34.0.0"
}

dependencies {

    implementation("androidx.appcompat:appcompat:1.6.1")
    implementation("com.google.android.material:material:1.11.0")
    implementation("androidx.constraintlayout:constraintlayout:2.1.4")
    implementation("androidx.lifecycle:lifecycle-livedata-ktx:2.7.0")
    implementation("androidx.lifecycle:lifecycle-viewmodel-ktx:2.7.0")
    implementation("androidx.navigation:navigation-fragment:2.7.7")
    implementation("androidx.navigation:navigation-ui:2.7.7")
    implementation("com.squareup.retrofit2:retrofit:2.9.0")
    implementation("com.squareup.retrofit2:converter-gson:2.9.0")
    implementation("com.squareup.okhttp3:logging-interceptor:4.9.0")
    implementation("androidx.camera:camera-view:1.3.2")
    implementation("com.google.android.gms:play-services-vision-common:19.1.3")
    testImplementation("junit:junit:4.13.2")
    androidTestImplementation("androidx.test.ext:junit:1.1.5")
    androidTestImplementation("androidx.test.espresso:espresso-core:3.5.1")
    implementation("com.auth0.android:jwtdecode:2.0.2")
    implementation("com.google.mlkit:face-detection:16.1.6")
    // CameraX
    fun cameraxVersion() = "1.4.0-alpha04"
    implementation("androidx.camera:camera-core:1.3.2")
    implementation("androidx.camera:camera-camera2:1.3.2")
    implementation("androidx.camera:camera-lifecycle:1.3.2")
    implementation("androidx.camera:camera-video:1.3.2")
    implementation("androidx.camera:camera-view:1.3.2")
    implementation("androidx.camera:camera-mlkit-vision:1.4.0-alpha04")
    implementation("androidx.camera:camera-extensions:1.3.2")
    implementation("org.tensorflow:tensorflow-lite:2.4.0")
    implementation("org.tensorflow:tensorflow-lite-gpu:2.4.0")
    implementation("org.tensorflow:tensorflow-lite-support:0.2.0")
}
