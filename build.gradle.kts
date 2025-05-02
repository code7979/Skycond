plugins {
    val agpVersion = "8.7.3"
    val kotlinVersion = "2.0.0"

    id("com.android.application") version agpVersion apply false
    id("com.android.library") version agpVersion apply false
    id("org.jetbrains.kotlin.android") version kotlinVersion apply false
}