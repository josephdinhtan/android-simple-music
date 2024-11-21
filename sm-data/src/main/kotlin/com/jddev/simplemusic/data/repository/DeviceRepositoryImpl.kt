package com.jddev.simplemusic.data.repository

import android.content.Context
import com.jddev.simplemusic.data.utils.getAllAudioFiles
import com.jddev.simplemusic.domain.model.Track
import com.jddev.simplemusic.domain.repository.DeviceRepository

class DeviceRepositoryImpl(val context: Context) : DeviceRepository {
    override suspend fun requestDeviceScan(): List<Track> = getAllAudioFiles(context)
}