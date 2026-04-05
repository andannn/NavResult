plugins {
    alias(libs.plugins.kotlin.multiplatform)
    alias(libs.plugins.android.kotlin.multiplatform.library)
    alias(libs.plugins.compose.compiler)
    alias(libs.plugins.serialization)
}

kotlin {
    android {
        namespace = "me.andannn.navresult.sample.navigation.compose.library"
        compileSdk = 36
    }

    listOf(
        iosArm64(),
        iosSimulatorArm64()
    ).forEach { iosTarget ->
        iosTarget.binaries.framework {
            baseName = "ComposeApp"
            isStatic = true
        }
    }

    sourceSets.commonMain.dependencies {
        implementation(project(":navresult"))
        implementation(libs.jetbrains.compose.material3)
        implementation(libs.navigation.compose)
    }
}
