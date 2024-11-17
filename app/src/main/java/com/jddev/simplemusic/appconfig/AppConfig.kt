package com.jddev.simplemusic.appconfig

import com.jddev.simplemusic.BuildConfig
import javax.inject.Inject

@Suppress("KotlinConstantConditions")
class AppConfig @Inject constructor() {
    fun isDevMode(): Boolean {
        return BuildConfig.DEV_MODE == "dev"
    }

    fun isDebugMode(): Boolean {
        return BuildConfig.DEBUG
    }
}