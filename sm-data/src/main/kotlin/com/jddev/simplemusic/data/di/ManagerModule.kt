package com.jddev.simplemusic.data.di

import android.content.Context
import com.jddev.simplemusic.data.managers.musicinfomanager.MusicInfoManager
import com.jddev.simplemusic.data.managers.musicinfomanager.MusicInfoManagerImpl
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import kotlinx.coroutines.CoroutineScope
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object ManagerModule {

    @Singleton
    @Provides
    fun provideMusicInfoManager(
        @ApplicationContext context: Context, @CoroutineScopeIO coroutineScope: CoroutineScope
    ): MusicInfoManager = MusicInfoManagerImpl(context, coroutineScope)
}