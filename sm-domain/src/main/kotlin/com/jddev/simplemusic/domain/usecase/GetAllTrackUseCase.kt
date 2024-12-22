package com.jddev.simplemusic.domain.usecase

import com.jddev.simplemusic.domain.model.Track
import com.jddev.simplemusic.domain.repository.MusicInfoRepository
import kotlinx.coroutines.flow.StateFlow
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class GetAllTrackUseCase @Inject constructor(
    val musicInfoRepository: MusicInfoRepository
) {
    operator fun invoke(): StateFlow<List<Track>> = musicInfoRepository.allTracks
}