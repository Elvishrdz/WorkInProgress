@file:Suppress("MayBeConstant", "SpellCheckingInspection")

package dependencies

import org.gradle.api.JavaVersion

object Versions {

    val compile_sdk = 30
    val build_tools = "30.0.0"
    val application_id = "com.eahm.learn"


    val min_sdk = 21
    val target_sdk = 30
    val version_code = 1
    val version_name = "1.0"

    val java = JavaVersion.VERSION_1_8
    val kotlin = "1.4.20"
    val dagger2 = "2.28"
    val firebase_bom = "26.1.1"
    val kotlin_coroutines = "1.4.2"
    val coroutines_play_services = "1.3.2"

    val retrofit = "2.9.0"
    val retrofit_logging_interceptor = "4.7.2"

    val glide = "4.11.0"



    val mockk_version = "1.9.2"
    val junit_jupiter_version = "5.6.0"
    val junit_4_version = "4.12"


    val room = "2.2.5"
    val navigation = "2.3.0"

    val gson = "2.8.6"
    val material_dialogs = "3.2.1"
}