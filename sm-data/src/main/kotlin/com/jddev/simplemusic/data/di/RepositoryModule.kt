package com.jddev.simplemusic.data.di

import android.content.Context
import com.jddev.simplemusic.data.managers.musicinfomanager.MusicInfoManager
import com.jddev.simplemusic.data.repository.AppDataRepositoryImpl
import com.jddev.simplemusic.data.repository.DeviceRepositoryImpl
import com.jddev.simplemusic.data.repository.PlaybackRepositoryImpl
import com.jddev.simplemusic.data.repository.MusicInfoRepositoryImpl
import com.jddev.simplemusic.data.repository.SettingsRepositoryImpl
import com.jddev.simplemusic.domain.repository.AppDataRepository
import com.jddev.simplemusic.domain.repository.DeviceRepository
import com.jddev.simplemusic.domain.repository.PlaybackRepository
import com.jddev.simplemusic.domain.repository.MusicInfoRepository
import com.jddev.simplemusic.domain.repository.SettingsRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import kotlinx.coroutines.CoroutineScope
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object RepositoryModule {

    @Singleton
    @Provides
    fun provideMusicController(@ApplicationContext context: Context): PlaybackRepository =
        PlaybackRepositoryImpl(context)

    @Singleton
    @Provides
    fun provideDeviceRepository(@ApplicationContext context: Context): DeviceRepository =
        DeviceRepositoryImpl(context)

    @Singleton
    @Provides
    fun provideMusicInfoRepository(
        @ApplicationContext context: Context,
        @CoroutineScopeIO coroutineScope: CoroutineScope,
        musicInfoManager: MusicInfoManager,
    ): MusicInfoRepository = MusicInfoRepositoryImpl(context, coroutineScope, musicInfoManager)

    @Singleton
    @Provides
    fun provideAppDataRepository(@ApplicationContext context: Context): AppDataRepository =
        AppDataRepositoryImpl()

    @Singleton
    @Provides
    fun provideSettingsRepository(@ApplicationContext context: Context): SettingsRepository =
        SettingsRepositoryImpl()
}