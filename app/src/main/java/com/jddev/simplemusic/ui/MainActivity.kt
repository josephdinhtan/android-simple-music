package com.jddev.simplemusic.ui

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.ui.Modifier
import com.jddev.simplemusic.SimpleMusicApplication
import com.jddev.simplemusic.ui.debug.DevControlPanelContent
import com.jddev.simpletouch.utils.debugui.DevUtilityUi
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        val appContainer = (application as SimpleMusicApplication).container
        setContent {
            if (appContainer.appConfig.isDevMode()) {
                DevUtilityUi(
                    modifier = Modifier.fillMaxSize(),
                    isEnable = true,
                    logManager = appContainer.devUtility.logManager,
                    devControlPanelContent = { DevControlPanelContent(appContainer.devUtility) }
                ) {
                    SimpleMusicApp(appContainer = appContainer)
                }

            } else {
                SimpleMusicApp(appContainer = appContainer)
            }
        }
    }
}