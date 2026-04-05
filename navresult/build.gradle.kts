plugins {
    alias(libs.plugins.kotlin.multiplatform)
    alias(libs.plugins.android.kotlin.multiplatform.library)
    alias(libs.plugins.compose.compiler)
    alias(libs.plugins.maven.publish)
}

kotlin {
    android {
        namespace = "io.github.andannn"
        compileSdk = 36
    }

    listOf(
        iosArm64(),
        iosSimulatorArm64()
    )

    sourceSets {
        commonMain.dependencies {
            implementation(libs.androidx.savedstate)
            implementation(libs.jetbrains.compose.runtime)
            implementation(libs.jetbrains.compose.runtime.saveable)
        }
    }
}

mavenPublishing {
    pom {
        name = "NavResult"
        description = "A small helper for Jetpack Compose that simplifies sending results between composables."
        url = "https://github.com/andannn/NavResult"

        licenses {
            license {
                name.set("The Apache License, Version 2.0")
                url.set("http://www.apache.org/licenses/LICENSE-2.0.txt")
                distribution.set("http://www.apache.org/licenses/LICENSE-2.0.txt")
            }
        }

        developers {
            developer {
                id.set("andannn")
                name.set("Andannn")
            }
        }

        scm {
            url = "https://github.com/andannn/NavResult.git"
            connection = "scm:git:git://github.com/andannn/NavResult.git"
            developerConnection = "scm:git:ssh://git@github.com/andannn/NavResult.git"
        }
    }
}
