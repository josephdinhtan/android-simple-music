package com.jddev.simplemusic.domain.usecase

import com.jddev.simplemusic.domain.model.Track
import com.jddev.simplemusic.domain.repository.DeviceRepository
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class RequestDeviceScanUseCase @Inject constructor(
    private val deviceRepository: DeviceRepository,
) {
    suspend operator fun invoke(): List<Track> = deviceRepository.requestDeviceScan()
}