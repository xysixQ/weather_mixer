import java.util.Properties

val localProperties = Properties().apply {
    val localFile = rootProject.file("local.properties")
    if (localFile.exists()) {
        localFile.inputStream().use(::load)
    }
}

fun String.toBuildConfigString(): String = "\"${replace("\\", "\\\\").replace("\"", "\\\"")}\""

fun localConfig(name: String): String = localProperties.getProperty(name, "")

plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.kotlin.compose)
}

android {
    namespace = "com.weathermixer.sixq"
    compileSdk {
        version = release(37)
    }

    defaultConfig {
        applicationId = "com.weathermixer.sixq"
        minSdk = 24
        targetSdk = 37
        versionCode = 89
        versionName = "1.2.32"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
        buildConfigField(
            "String",
            "AI_ADVICE_API_KEY",
            localConfig("aiAdviceApiKey").toBuildConfigString()
        )
        buildConfigField(
            "String",
            "BAIDU_IP_LOCATION_API_KEY",
            localConfig("baiduIpLocationApiKey").toBuildConfigString()
        )
        buildConfigField(
            "String",
            "AMAP_API_KEY",
            localConfig("amapApiKey").toBuildConfigString()
        )
        buildConfigField(
            "String",
            "QWEATHER_API_KEY",
            localConfig("qWeatherApiKey").toBuildConfigString()
        )
        buildConfigField(
            "String",
            "QWEATHER_API_HOST",
            localConfig("qWeatherApiHost").toBuildConfigString()
        )
        buildConfigField(
            "String",
            "SENIVERSE_API_KEY",
            localConfig("seniverseApiKey").toBuildConfigString()
        )
        buildConfigField(
            "String",
            "OPENWEATHER_API_KEY",
            localConfig("openWeatherApiKey").toBuildConfigString()
        )
        buildConfigField(
            "String",
            "VISUAL_CROSSING_API_KEY",
            localConfig("visualCrossingApiKey").toBuildConfigString()
        )
        buildConfigField(
            "String",
            "METEOSTAT_RAPIDAPI_KEY",
            localConfig("meteostatRapidApiKey").toBuildConfigString()
        )
    }

    buildTypes {
        release {
            optimization {
                enable = false
            }
        }
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_11
        targetCompatibility = JavaVersion.VERSION_11
    }
    buildFeatures {
        buildConfig = true
        compose = true
    }
}

kotlin {
    compilerOptions {
        jvmTarget.set(org.jetbrains.kotlin.gradle.dsl.JvmTarget.JVM_11)
    }
}

dependencies {
    implementation(platform(libs.androidx.compose.bom))
    implementation(libs.androidx.activity.compose)
    implementation(libs.androidx.compose.animation)
    implementation(libs.androidx.compose.foundation)
    implementation(libs.androidx.compose.material.icons.extended)
    implementation(libs.androidx.compose.material3)
    implementation(libs.androidx.compose.ui)
    implementation(libs.okhttp)
    implementation(libs.androidx.compose.ui.tooling.preview)
    implementation(libs.androidx.core.ktx)
    debugImplementation(libs.androidx.compose.ui.tooling)
    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.espresso.core)
    androidTestImplementation(libs.androidx.junit)
}
