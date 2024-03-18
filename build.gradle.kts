// Top-level build file where you can add configuration options common to all sub-projects/modules.
buildscript {
    extra.apply {
        set("lifecycle_version", "2.7.0")
        set("navigation_version", "2.7.7")
        set("compose_compiler_version", "1.5.10")
        set("camerax_version", "1.3.2")
        set("room_version", "2.6.1")
    }
}

plugins {
    id("com.android.library") version "8.3.0" apply false
    id("com.android.application") version "8.3.0" apply false
    id("org.jetbrains.kotlin.android") version "1.9.0" apply false
}