@file:Suppress("MayBeConstant", "SpellCheckingInspection")

package dependencies

object Dependencies{
    val kotlin_stdlib = "org.jetbrains.kotlin:kotlin-stdlib:${Versions.kotlin}"
    val androidx_core_ktx = "androidx.core:core-ktx:1.3.2"
    val androidx_appcompat = "androidx.appcompat:appcompat:1.2.0"
    val material_design = "com.google.android.material:material:1.2.1"
    val material_dialogs = "com.afollestad.material-dialogs:core:${Versions.material_dialogs}"
    val material_dialogs_input = "com.afollestad.material-dialogs:input:${Versions.material_dialogs}"

    val constraint_layout = "androidx.constraintlayout:constraintlayout:2.0.2"
    val swipe_refresh_layout = "androidx.swiperefreshlayout:swiperefreshlayout:1.1.0"

    val gson = "com.google.code.gson:gson:${Versions.gson}"

    val dagger2_core = "com.google.dagger:dagger:${Versions.dagger2}"                             // Dagger 2 Core
    val dagger2_android = "com.google.dagger:dagger-android:${Versions.dagger2}"                  // Dagger 2 Android
    val dagger2_android_support = "com.google.dagger:dagger-android-support:${Versions.dagger2}"  // Dagger 2 Android Support


    val firebase_bom = "com.google.firebase:firebase-bom:${Versions.firebase_bom}"
    val firebase_analitics = "com.google.firebase:firebase-analytics-ktx"
    val firebase_auth = "com.google.firebase:firebase-auth-ktx"
    val firebase_firestore = "com.google.firebase:firebase-firestore-ktx"
    val firebase_crashlytics = 	"com.google.firebase:firebase-crashlytics-ktx"

    val kotlin_coroutines = "org.jetbrains.kotlinx:kotlinx-coroutines-android:${Versions.kotlin_coroutines}"
    val kotlin_coroutines_android = "org.jetbrains.kotlinx:kotlinx-coroutines-android:${Versions.kotlin_coroutines}"
    val kotlin_coroutines_play_services = "org.jetbrains.kotlinx:kotlinx-coroutines-play-services:${Versions.coroutines_play_services}"

    val retrofit = "com.squareup.retrofit2:retrofit:${Versions.retrofit}"                                                    // Retrofit
    val retrofit_gson_converter = "com.squareup.retrofit2:converter-gson:${Versions.retrofit}"                               // Retrofit GSON converter
    val retrofit_logging_interceptor = "com.squareup.okhttp3:logging-interceptor:${Versions.retrofit_logging_interceptor}"   // Retrofit Logging interceptor

    val glide = "com.github.bumptech.glide:glide:${Versions.glide}"

    /*Jetpack Room*/
    val room = "androidx.room:room-runtime:${Versions.room}"              // Room
    val room_compiler = "androidx.room:room-compiler:${Versions.room}"    // Room Compiler
    val room_testing = "androidx.room:room-testing:${Versions.room}"      // Room Test helpers
    val room_extensions = "androidx.room:room-ktx:${Versions.room}"       // Room extensions add coroutines support for database transactions

    /*Jetpack Navigation*/
    val navigation_fragment = "androidx.navigation:navigation-fragment-ktx:${Versions.navigation}"       // Navigation Fragment
    val navigation_ui = "androidx.navigation:navigation-ui-ktx:${Versions.navigation}"                   // Navigation UI

}

object AnnotationProcessing{
    val dagger2_compiler = "com.google.dagger:dagger-compiler:${Versions.dagger2}"                       // Dagger 2 Compiler
    val dagger2_android_processor = "com.google.dagger:dagger-android-processor:${Versions.dagger2}"               // Dagger 2 Android Processor

    val glide = "com.github.bumptech.glide:compiler:${Versions.glide}"



}

object TestAnnotationProcessing {
    val dagger2_compiler = "com.google.dagger:dagger-compiler:${Versions.dagger2}"                    // Dagger 2 Compiler
}