package com.jddev.simplemusic.data.di

import android.content.Context
import com.jddev.simplemusic.data.repository.MusicControllerRepositoryImpl
import com.jddev.simplemusic.domain.repository.MusicControllerRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object RepositoryModule {

    @Singleton
    @Provides
    fun provideMusicController(@ApplicationContext context: Context): MusicControllerRepository =
        MusicControllerRepositoryImpl(context)
}