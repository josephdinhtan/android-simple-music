package com.jddev.simplemusic.data.repository

import android.content.Context
import com.jddev.simplemusic.domain.repository.DeviceRepository

class DeviceRepositoryImpl(val context: Context) : DeviceRepository {
    override fun initializer() {
        // TODO("Not yet implemented")
    }

    override suspend fun requestDeviceScan() {
        // request Android MediaProvider rescan all track
    }
}