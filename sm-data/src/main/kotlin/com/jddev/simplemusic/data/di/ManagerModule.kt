package com.jddev.simplemusic.data.di

import android.content.Context
import com.jddev.simplemusic.data.managers.MusicInfoManager
import com.jddev.simplemusic.data.managers.MusicInfoManagerImpl
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object ManagerModule {

    @Singleton
    @Provides
    fun provideMusicInfoManager(@ApplicationContext context: Context): MusicInfoManager =
        MusicInfoManagerImpl(context)
}