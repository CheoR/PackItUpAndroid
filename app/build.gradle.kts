plugins {
    id("com.android.application")
    id("org.jetbrains.kotlin.android")
}

android {
    namespace = "com.example.packitupandroid"
    compileSdk = 34

    defaultConfig {
        applicationId = "com.example.packitupandroid"
        minSdk = 34
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
        sourceCompatibility = JavaVersion.VERSION_17
        targetCompatibility = JavaVersion.VERSION_17
    }
    kotlinOptions {
        jvmTarget = "17"
        freeCompilerArgs += "-opt-in=androidx.compose.material3.ExperimentalMaterial3Api"
    }
    buildFeatures {
        compose = true
//        viewBinding = true
    }
    composeOptions {
        kotlinCompilerExtensionVersion = rootProject.extra["compose_compiler_version"].toString()
    }
    packaging {
        resources {
            excludes += "/META-INF/{AL2.0,LGPL2.1}"
        }
    }
}

dependencies {

    val composeBom = platform("androidx.compose:compose-bom:2023.08.00")

    implementation(composeBom)
    implementation("androidx.compose.ui:ui")
    implementation("androidx.core:core-ktx:1.12.0")
    implementation("androidx.compose.ui:ui-graphics")
    implementation("androidx.compose.material3:material3")
    implementation("androidx.compose.ui:ui-tooling-preview")
    implementation("androidx.activity:activity-compose:1.8.2")
    implementation("androidx.compose.material:material-icons-extended")
    // for adaptive sizing
    implementation("androidx.compose.material3:material3-window-size-class")
    implementation("androidx.navigation:navigation-compose:${rootProject.extra["navigation_version"]}")
    implementation("androidx.lifecycle:lifecycle-runtime-ktx:${rootProject.extra["lifecycle_version"]}")
    implementation("androidx.lifecycle:lifecycle-runtime-compose:${rootProject.extra["lifecycle_version"]}")
    implementation("androidx.lifecycle:lifecycle-viewmodel-compose:${rootProject.extra["lifecycle_version"]}")

    // CameraX
    implementation("androidx.camera:camera-core:${rootProject.extra["camerax_version"]}")
    implementation("androidx.camera:camera-camera2:${rootProject.extra["camerax_version"]}")
    implementation("androidx.camera:camera-lifecycle:${rootProject.extra["camerax_version"]}")
    implementation("androidx.camera:camera-video:${rootProject.extra["camerax_version"]}")
    implementation("androidx.camera:camera-view:${rootProject.extra["camerax_version"]}")
    implementation("androidx.camera:camera-extensions:${rootProject.extra["camerax_version"]}")

    testImplementation("junit:junit:4.13.2")

    androidTestImplementation(composeBom)
    androidTestImplementation("androidx.test.ext:junit:1.1.5")
    androidTestImplementation("androidx.compose.ui:ui-test-junit4")
    androidTestImplementation("androidx.test.espresso:espresso-core:3.5.1")
    androidTestImplementation("androidx.navigation:navigation-testing:${rootProject.extra["navigation_version"]}")

    debugImplementation("androidx.compose.ui:ui-tooling")
    debugImplementation("androidx.compose.ui:ui-test-manifest")
}