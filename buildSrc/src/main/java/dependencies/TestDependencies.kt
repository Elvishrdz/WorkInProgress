package dependencies

object TestDependencies{
    val junit = "junit:junit:4.13.1"

    val jupiter_api = "org.junit.jupiter:junit-jupiter-api:${Versions.junit_jupiter_version}"
    val jupiter_params = "org.junit.jupiter:junit-jupiter-params:${Versions.junit_jupiter_version}"
    val jupiter_engine = "org.junit.jupiter:junit-jupiter-engine:${Versions.junit_jupiter_version}"
    val mockk = "io.mockk:mockk:${Versions.mockk_version}"
    val junit4 = "junit:junit:${Versions.junit_4_version}"
}

object AndroidTestDependencies{
    val junit = "androidx.test.ext:junit:1.1.2"
    val espresso = "androidx.test.espresso:espresso-core:3.3.0"
}

object TestInstrumentationRunners{
    val android_junit = "androidx.test.runner.AndroidJUnitRunner"
}