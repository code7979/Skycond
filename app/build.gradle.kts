plugins {
    id("com.android.application")
    id("org.jetbrains.kotlin.android")
    id("app.cash.sqldelight") version "2.0.2"
}

android {
    namespace = "abhiket.skycond"
    compileSdk = 34

    defaultConfig {
        applicationId = "abhiket.skycond"
        minSdk = 24
        targetSdk = 34
        versionCode = 1
        versionName = "1.0"
    }

    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_17
        targetCompatibility = JavaVersion.VERSION_17
    }

    kotlinOptions {
        jvmTarget = "17"
    }

    packaging {
        resources.excludes.add("META-INF/INDEX.LIST")
    }

    buildFeatures {
        viewBinding = true
    }
}

sqldelight {
    databases {
        create("Database") {
            packageName.set("abhiket")
        }
    }
}

dependencies {
    // CORE_ANDROID
    implementation("androidx.core:core-ktx:1.13.0")
    implementation("androidx.appcompat:appcompat:1.7.0")
    implementation("androidx.annotation:annotation:1.9.1")

    // VIEW_PAGER
    implementation("androidx.viewpager2:viewpager2:1.1.0")

    // CONSTRAINT_LAYOUT
    implementation("androidx.constraintlayout:constraintlayout:2.2.1")

    // CARD_VIEW
    implementation("androidx.cardview:cardview:1.0.0")

    // KTX_LIBRARIES
    implementation("androidx.activity:activity-ktx:1.9.3")
    implementation("androidx.lifecycle:lifecycle-livedata-ktx:2.8.7")
    implementation("androidx.lifecycle:lifecycle-viewmodel-ktx:2.8.7")

    implementation("androidx.coordinatorlayout:coordinatorlayout:1.3.0")
    implementation("androidx.swiperefreshlayout:swiperefreshlayout:1.1.0")

    // RETROFIT2
    implementation("com.squareup.retrofit2:retrofit:2.11.0")
    implementation("com.squareup.retrofit2:converter-gson:2.11.0")

    // //SQLITE
    implementation("app.cash.sqldelight:android-driver:2.0.2")
    implementation("app.cash.sqldelight:primitive-adapters:2.0.2")

    // MATERIAL
    implementation("com.google.android.material:material:1.12.0")
}