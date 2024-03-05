plugins {
    id("com.android.application")
    id("org.jetbrains.kotlin.android")
    id("org.jetbrains.kotlin.kapt")
    id("com.google.dagger.hilt.android")
    id ("androidx.navigation.safeargs")
    id("com.google.devtools.ksp")
}

android {
    namespace = "com.example.drugbank"
    compileSdk = 34

    defaultConfig {
        applicationId = "com.drugbank.drugbank"
        minSdk = 28
        targetSdk = 34
        versionCode = 3
        versionName = "3.0"

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
    buildFeatures{
        dataBinding = true
        viewBinding = true
    }

    packagingOptions {
        exclude ("META-INF/DEPENDENCIES")
        exclude ("META-INF/LICENSE")
        exclude ("META-INF/LICENSE.txt")
        exclude ("META-INF/license.txt")
        exclude ("META-INF/NOTICE")
        exclude ("META-INF/NOTICE.txt")
        exclude ("META-INF/notice.txt")
        exclude ("META-INF/ASL2.0")
        exclude ("META-INF/INDEX.LIST")
    }
}

dependencies {
    implementation("androidx.core:core-ktx:1.9.0")
    implementation("androidx.appcompat:appcompat:1.6.1")
    implementation("com.google.android.material:material:1.10.0")
    implementation("androidx.constraintlayout:constraintlayout:2.1.4")
    implementation("androidx.legacy:legacy-support-v4:1.0.0")
    implementation("androidx.lifecycle:lifecycle-viewmodel-ktx:2.6.2")
    implementation("androidx.databinding:databinding-runtime:8.2.1")
    implementation("com.google.android.gms:play-services-drive:17.0.0")
    implementation("androidx.recyclerview:recyclerview:1.3.0")
    implementation("androidx.preference:preference:1.2.1")
    /*    implementation("androidx.annotation:annotation-jvm:1.7.1")*/
    testImplementation("junit:junit:4.13.2")
    androidTestImplementation("androidx.test.ext:junit:1.1.5")
    androidTestImplementation("androidx.test.espresso:espresso-core:3.5.1")

    //nav
    implementation("androidx.navigation:navigation-fragment-ktx:2.4.2")
    implementation("androidx.navigation:navigation-ui-ktx:2.4.2")
    implementation("androidx.lifecycle:lifecycle-livedata-ktx:2.4.1")


    // Retrofit
    var retrofit_version = "2.9.0"
    implementation("com.squareup.retrofit2:retrofit:$retrofit_version")
    implementation("com.squareup.retrofit2:converter-moshi:$retrofit_version")
    implementation("com.squareup.retrofit2:converter-gson:$retrofit_version")
    implementation ("com.squareup.retrofit2:converter-scalars:2.9.0")

    // OkHttp3
    implementation("com.squareup.okhttp3:okhttp:4.9.0")
    implementation("com.squareup.okhttp3:logging-interceptor:4.9.0")


    // Dagger Hilt
    var dagger_hilt_version = "2.48.1"
    implementation ("com.google.dagger:hilt-android:$dagger_hilt_version")
    kapt("com.google.dagger:hilt-android-compiler:$dagger_hilt_version")

    // Moshi Version
    var moshi_version = "1.11.0"
    implementation("com.squareup.moshi:moshi-kotlin:$moshi_version")

    // Coroutines Version
    var coroutines_version = "1.5.2"
    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-android:$coroutines_version")


    // Room version
    var room_version = "2.5.0"
    implementation("androidx.room:room-runtime:$room_version")
    annotationProcessor("androidx.room:room-compiler:$room_version")
    ksp("androidx.room:room-compiler:$room_version")
    implementation("androidx.room:room-ktx:$room_version")
    implementation("androidx.room:room-paging:$room_version")
    implementation("com.google.android.gms:play-services-tasks:18.0.2")

    // Circle Image
    implementation("de.hdodenhof:circleimageview:3.1.0")

    // =================== Google Services ===========================
    // Gson Version
    implementation("com.google.code.gson:gson:2.8.8")

    // GooglePlay Services Version
    implementation("com.google.android.gms:play-services-auth:20.7.0")
    implementation ("androidx.biometric:biometric:1.2.0-alpha03")

    // Access Token Outh2
    implementation("com.google.cloud:google-cloud-core:2.27.0")

    // FireBase Version
    implementation(platform("com.google.firebase:firebase-bom:32.5.0"))
    implementation("com.google.firebase:firebase-storage")

    // =================== Google Services ===========================


    // ================== Gif and Image View =========================
    // Picasso Version
    implementation("com.squareup.picasso:picasso:2.8")

    // Gif Image View Version
    implementation("pl.droidsonroids.gif:android-gif-drawable:1.2.28")

    // Flexbox version
    implementation("com.google.android.flexbox:flexbox:3.0.0")

    // Bottom av vavigation
    implementation ("com.github.ibrahimsn98:NiceBottomBar:2.2")


    // Paging for android
    implementation("androidx.paging:paging-common-ktx:3.2.1")
    implementation("androidx.paging:paging-runtime-ktx:3.2.1")
}