
plugins {
    id("com.android.application")
    id("org.jetbrains.kotlin.android")

    id("androidx.navigation.safeargs.kotlin")
    id("com.google.gms.google-services")
}

android {
    namespace = "com.utad.freetoplaylist"
    compileSdk = 33

    defaultConfig {
        applicationId = "com.utad.freetoplaylist"
        minSdk = 24
        targetSdk = 33
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
        sourceCompatibility = JavaVersion.VERSION_1_8
        targetCompatibility = JavaVersion.VERSION_1_8
    }
    kotlinOptions {
        jvmTarget = "1.8"
    }
    viewBinding {
        enable = true
    }
}

dependencies {

    implementation("androidx.core:core-ktx:1.9.0")
    implementation("androidx.appcompat:appcompat:1.6.1")
    implementation("com.google.android.material:material:1.9.0")
    implementation("androidx.constraintlayout:constraintlayout:2.1.4")
    testImplementation("junit:junit:4.13.2")
    androidTestImplementation("androidx.test.ext:junit:1.1.5")
    androidTestImplementation("androidx.test.espresso:espresso-core:3.5.1")


    val interceptor = "4.11.0"
    val glide = "4.16.0"
    val retrofit = "2.9.0"
    val navVersion = "2.5.3"

    //Retrofit
    implementation ("com.squareup.retrofit2:retrofit:$retrofit")
    implementation ("com.squareup.retrofit2:converter-gson:$retrofit")
    implementation ("com.squareup.okhttp3:logging-interceptor:$interceptor")

    //Glide
    implementation ("com.github.bumptech.glide:glide:$glide")

    //Navigation
    implementation ("androidx.navigation:navigation-fragment-ktx:$navVersion")
    implementation ("androidx.navigation:navigation-ui-ktx:$navVersion")

    //Corroutines
    implementation ("androidx.lifecycle:lifecycle-runtime-ktx:2.6.1")

    //DataStore
    implementation ("androidx.datastore:datastore-preferences:1.0.0")

    //Firebase dependencias básicas
    val firebaseVersion = "32.2.3"
    implementation(platform("com.google.firebase:firebase-bom:$firebaseVersion"))

    //Firebase - Authentification
    implementation ("com.google.firebase:firebase-auth-ktx:22.1.2")

    //Firebase - Messaging
    implementation ("com.google.firebase:firebase-messaging-ktx:23.2.1")

    //Firebase - Authentification para poder probar auth por SMS sin publicar
    implementation("com.google.firebase:firebase-appcheck-debug")
    implementation("com.google.firebase:firebase-appcheck-ktx")



}