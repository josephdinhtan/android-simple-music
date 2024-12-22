package com.jddev.simplemusic.domain.usecase

import com.jddev.simplemusic.domain.model.Artist
import com.jddev.simplemusic.domain.repository.MusicInfoRepository
import kotlinx.coroutines.flow.StateFlow
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class GetArtistsUseCase @Inject constructor(
    val musicInfoRepository: MusicInfoRepository
) {
    operator fun invoke(): StateFlow<List<Artist>> = musicInfoRepository.artists
}