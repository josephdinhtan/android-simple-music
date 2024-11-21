package com.jddev.simplemusic.domain.usecase

import com.jddev.simplemusic.domain.repository.AppDataRepository
import com.jddev.simplemusic.domain.repository.DeviceRepository
import com.jddev.simplemusic.domain.repository.MusicControllerRepository
import com.jddev.simplemusic.domain.repository.SettingsRepository
import javax.inject.Inject

class InitializeDomainUseCase @Inject constructor(
    private val appDataRepository: AppDataRepository,
    private val deviceRepository: DeviceRepository,
    private val musicControllerRepository: MusicControllerRepository,
    private val settingsRepository: SettingsRepository
) {
    operator fun invoke() {
        // init repository
        appDataRepository
        deviceRepository
        musicControllerRepository
        settingsRepository
    }
}