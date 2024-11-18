package com.jddev.simplemusic

import android.app.Application
import com.jddev.simpletouch.utils.logging.AppTree
import dagger.hilt.android.HiltAndroidApp
import javax.inject.Inject

@HiltAndroidApp
class SimpleMusicApplication : Application() {
    @Inject
    lateinit var appTree: AppTree

    @Inject
    lateinit var container: AppContainer

    override fun onCreate() {
        super.onCreate()
    }
}