plugins {
    id("com.android.application")
    id("org.jetbrains.kotlin.android")
}

android {
    namespace = "com.example.henshinphone"
    compileSdk = 34

    defaultConfig {
        applicationId = "com.example.henshinphone"
        minSdk = 23
        targetSdk = 34
        versionCode = 1
        versionName = "1.0"
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
    }

    buildFeatures {
        compose = true
    }

    /**
     * ⚠️ 关键点：
     * Compose Compiler 1.5.14
     * ✔ 与 Compose BOM 2024.06.00 / 1.6.x 完全匹配
     */
    composeOptions {
        kotlinCompilerExtensionVersion = "1.5.14"
    }

    packaging {
        resources {
            excludes += "/META-INF/{AL2.0,LGPL2.1}"
        }
    }
}

dependencies {

    /**
     * ============================
     * Compose BOM（版本锚点）
     * ============================
     * 2024.06.00 ≈ Compose 1.6.x
     * ✔ 兼容 compileSdk 34
     * ✔ 兼容 AGP 8.5.x
     */
    val composeBom = platform("androidx.compose:compose-bom:2024.06.00")
    implementation(composeBom)
    androidTestImplementation(composeBom)

    // Compose 核心
    implementation("androidx.compose.ui:ui")
    implementation("androidx.compose.ui:ui-tooling-preview")
    implementation("androidx.compose.foundation:foundation")
    implementation("androidx.compose.material3:material3")
    implementation("androidx.compose.material:material-icons-extended")

    // Debug 工具
    debugImplementation("androidx.compose.ui:ui-tooling")

    // Activity
    implementation("androidx.activity:activity-compose:1.9.0")

    // Navigation（虽然你现在不用，但保留不影响）
    implementation("androidx.navigation:navigation-compose:2.7.7")

    // Media3（视频播放）
    implementation("androidx.media3:media3-exoplayer:1.3.1")
    implementation("androidx.media3:media3-ui:1.3.1")

    // Material（非 Compose，给系统组件用）
    implementation("com.google.android.material:material:1.11.0")
}
