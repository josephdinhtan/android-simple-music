package com.jddev.simplemusic.domain.usecase

import com.jddev.simplemusic.domain.repository.AppDataRepository
import com.jddev.simplemusic.domain.repository.DeviceRepository
import com.jddev.simplemusic.domain.repository.PlaybackRepository
import com.jddev.simplemusic.domain.repository.MusicInfoRepository
import com.jddev.simplemusic.domain.repository.SettingsRepository
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class InitializeDomainUseCase @Inject constructor(
    private val appDataRepository: AppDataRepository,
    private val deviceRepository: DeviceRepository,
    private val musicInfoRepository: MusicInfoRepository,
    private val musicControllerRepository: PlaybackRepository,
    private val settingsRepository: SettingsRepository
) {
    operator fun invoke() {
        // init repository
        appDataRepository.initializer()
        deviceRepository.initializer()
        musicInfoRepository.initializer()
        musicControllerRepository.initializer()
        settingsRepository.initializer()
    }
}