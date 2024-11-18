package com.jddev.simplemusic.ui.debug

import com.jddev.simpletouch.utils.di.CoroutineScopeIO
import com.jddev.simpletouch.utils.logging.LogManager
import kotlinx.coroutines.CoroutineScope
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class DevUtility @Inject constructor(
    var logManager: LogManager,
    @CoroutineScopeIO var coroutineScope: CoroutineScope,
) {
    fun doSomething() {
    }
}