package com.jddev.simplemusic.domain.repository

interface DeviceRepository {
    fun initializer()
    suspend fun requestDeviceScan()
}