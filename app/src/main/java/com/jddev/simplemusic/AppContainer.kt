package com.jddev.simplemusic

import android.content.Context
import com.jddev.simplemusic.appconfig.AppConfig
import com.jddev.simplemusic.ui.debug.DevUtility
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject

class AppContainer @Inject constructor (
    @ApplicationContext val applicationContext: Context,
    val appConfig: AppConfig,
    val devUtility: DevUtility,
)