package com.jddev.simplemusic.domain.repository

import com.jddev.simplemusic.domain.model.Track

interface DeviceRepository {
    suspend fun requestDeviceScan(): List<Track>
}