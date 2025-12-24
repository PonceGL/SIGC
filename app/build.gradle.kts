import java.util.Properties

plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.kotlin.android)
    alias(libs.plugins.hilt.android)
    alias(libs.plugins.ksp)
    alias(libs.plugins.google.services)
    alias(libs.plugins.firebase.crashlytics)
}

val localProperties = Properties()
val localPropertiesFile = rootProject.file("local.properties")
if (localPropertiesFile.exists()) {
    localProperties.load(localPropertiesFile.inputStream())
}

fun getSecretStrict(keyName: String, flavorSuffix: String): String {
    val fullKey = if (flavorSuffix.isEmpty()) keyName else "${keyName}_$flavorSuffix"

    val value = localProperties.getProperty(fullKey)

    if (value.isNullOrEmpty()) {
        throw GradleException(
            """
            |
            |❌ ERROR CRÍTICO DE CONFIGURACIÓN:
            |---------------------------------------------------
            |Falta la variable requerida: '$fullKey'
            |Ubicación esperada: archivo 'local.properties'
            |
            |Por favor agrega esta variable para el ambiente seleccionado
            |antes de intentar compilar nuevamente.
            |---------------------------------------------------
            """.trimMargin()
        )
    }

    return value
}

android {
    namespace = "com.poncegl.sigc"
    compileSdk = 36

    defaultConfig {
        applicationId = "com.poncegl.sigc"
        minSdk = 29
        targetSdk = 36
        versionCode = 1
        versionName = "1.0"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"

        buildConfigField("String", "SERVER_CLIENT_ID", "\"UNSET\"")

        vectorDrawables {
            useSupportLibrary = true
        }
    }

    signingConfigs {
        getByName("debug") {
            storeFile = file("keystore/debug.keystore")
            storePassword = "android"
            keyAlias = "androiddebugkey"
            keyPassword = "android"
        }
    }

    buildTypes {
        getByName("debug") {
            signingConfig = signingConfigs.getByName("debug")
        }

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
    buildFeatures {
        compose = true
        buildConfig = true
    }

//    Environments
    flavorDimensions += "environment"

    productFlavors {
        create("dev") {
            dimension = "environment"
            applicationIdSuffix = ".dev"
            versionNameSuffix = "-dev"
        }
        create("qa") {
            dimension = "environment"
            applicationIdSuffix = ".qa"
            versionNameSuffix = "-qa"
        }
        create("staging") {
            dimension = "environment"
            applicationIdSuffix = ".stg"
            versionNameSuffix = "-stg"
        }
        create("prod") {
            dimension = "environment"
        }
    }

    applicationVariants.all {
        val flavorName = flavorName

        val suffix = when(flavorName) {
            "dev" -> "DEV"
            "qa" -> "QA"
            "staging" -> "STAGING"
            "prod" -> "PROD"
            else -> ""
        }

        val secretsKeys = listOf(
            "SERVER_CLIENT_ID",
            "APP_NAME",
            "AUTH_HOST"
        )

        secretsKeys.forEach { key ->
            val value = getSecretStrict(key, suffix)
            buildConfigField("String", key, "\"$value\"")
        }

        val authHostValue = getSecretStrict("AUTH_HOST", suffix)

        val appNameVariant = when(flavorName) {
            "dev" -> "SIGC - DEV"
            "qa" -> "SIGC - QA"
            "staging" -> "SIGC - STG"
            else -> "SIGC"
        }

        resValue("string", "app_name_flavor", appNameVariant)
        resValue("string", "auth_host", authHostValue)

        buildConfigField("String", "APP_NAME", "\"$appNameVariant\"")
    }

    composeOptions {
        kotlinCompilerExtensionVersion = "1.5.1"
    }
    packaging {
        resources {
            excludes += "/META-INF/{AL2.0,LGPL2.1}"
        }
    }
}

dependencies {

    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.lifecycle.runtime.ktx)
    implementation(libs.androidx.lifecycle.viewmodel.compose)
    implementation(libs.androidx.activity.compose)
    implementation(platform(libs.androidx.compose.bom))
    implementation(libs.androidx.ui)
    implementation(libs.androidx.ui.graphics)
    implementation(libs.androidx.ui.tooling.preview)
    implementation(libs.androidx.material3)
    implementation(libs.androidx.material.icons.extended)
    implementation(libs.androidx.datastore.preferences)
    implementation(libs.androidx.core.splashscreen)
    implementation(libs.androidx.material3.window.size)
    implementation(libs.androidx.credentials)
    implementation(libs.androidx.credentials.play.services.auth)
    implementation(libs.kotlinx.coroutines.play.services)
    implementation(libs.androidx.compose.adaptive)
    implementation(libs.androidx.compose.adaptive.layout)
    implementation(libs.androidx.compose.adaptive.navigation)

    // firebase
    implementation(platform(libs.firebase.bom))
    implementation(libs.firebase.analytics)
    implementation(libs.firebase.crashlytics)
    implementation(libs.firebase.auth)
    implementation(libs.firebase.firestore)

    // google services
    implementation(libs.google.services)
    implementation(libs.googleid)

    // Navigation
    implementation(libs.androidx.navigation.compose)

    // Room
    implementation(libs.androidx.room.runtime)
    implementation(libs.androidx.room.ktx)
    ksp(libs.androidx.room.compiler)

    // Hilt
    implementation(libs.androidx.hilt.navigation.compose)
    implementation(libs.hilt.android)
    ksp(libs.hilt.compiler)

    // WorkManager
    implementation(libs.androidx.work.runtime.ktx)

    // Gson
    implementation(libs.gson)

    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)
    androidTestImplementation(platform(libs.androidx.compose.bom))
    androidTestImplementation(libs.androidx.ui.test.junit4)
    debugImplementation(libs.androidx.ui.tooling)
    debugImplementation(libs.androidx.ui.test.manifest)
}