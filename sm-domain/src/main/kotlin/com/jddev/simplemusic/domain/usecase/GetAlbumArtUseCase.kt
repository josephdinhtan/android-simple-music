package com.jddev.simplemusic.domain.usecase

import android.graphics.Bitmap
import com.jddev.simplemusic.domain.repository.MusicInfoRepository
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class GetAlbumArtUseCase @Inject constructor(
    val musicInfoRepository: MusicInfoRepository
) {
    operator fun invoke(albumId: Long?, artistId: Long): Bitmap? =
        musicInfoRepository.getAlbumArt(albumId, artistId)
}