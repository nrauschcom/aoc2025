plugins {
    alias(libs.plugins.kotlinMultiplatform)
    alias(libs.plugins.kotlinxSerialization)
}

group = "me.user"
version = "1.0-SNAPSHOT"

repositories {
    mavenCentral()
}

kotlin {
    val hostOs = System.getProperty("os.name")
    val isArm64 = System.getProperty("os.arch") == "aarch64"
    val isMingwX64 = hostOs.startsWith("Windows")
    val tryhardTarget = linuxX64("tryhard")
    val nativeTarget = when {
        hostOs == "Linux" && !isArm64 -> linuxX64("host")
        isMingwX64 -> mingwX64("host")
        else -> throw GradleException("Host OS is not supported in Kotlin/Native.")
    }

    jvm()

    tryhardTarget.apply {
        binaries {
            executable {
                entryPoint = "main"
                runTaskProvider?.configure {
                    standardInput = System.`in`
                    standardOutput = System.out
                }
            }
        }
    }

    nativeTarget.apply {
        binaries {
            executable {
                entryPoint = "main"
                runTaskProvider?.configure {
                    standardInput = System.`in`
                    standardOutput = System.out
                }
            }
        }
    }

    sourceSets {
        val commonMain by getting {
            dependencies {
                implementation("org.jetbrains.kotlinx:kotlinx-coroutines-core:1.10.2")
                implementation("com.squareup.okio:okio:3.16.2")
                implementation(kotlin("stdlib-common"))
                implementation("org.jetbrains.kotlin:kotlin-stdlib-common")
            }
        }

        val tryhardMain by getting {
        }

        val hostMain by getting {
            dependsOn(tryhardMain)
        }

        /*val jvmMain by getting {
            dependsOn(commonMain)
        }*/
    }
}
