package com.jddev.simplemusic.domain.usecase

import com.jddev.simplemusic.domain.model.Album
import com.jddev.simplemusic.domain.repository.MusicInfoRepository
import kotlinx.coroutines.flow.StateFlow
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class GetAlbumsUseCase @Inject constructor(
    val musicInfoRepository: MusicInfoRepository
) {
    operator fun invoke(): StateFlow<List<Album>> = musicInfoRepository.albums
}