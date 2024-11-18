package com.jddev.simplemusic.data.repository

import android.content.ComponentName
import android.content.Context
import android.net.Uri
import androidx.media3.common.MediaItem
import androidx.media3.common.MediaMetadata
import androidx.media3.common.Player
import androidx.media3.session.MediaController
import androidx.media3.session.SessionToken
import com.google.common.util.concurrent.ListenableFuture
import com.google.common.util.concurrent.MoreExecutors
import com.jddev.simplemusic.data.service.MusicService
import com.jddev.simplemusic.data.utils.toTrack
import com.jddev.simplemusic.domain.model.PlayerState
import com.jddev.simplemusic.domain.model.Track
import com.jddev.simplemusic.domain.repository.MusicControllerRepository
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import timber.log.Timber

class MusicControllerRepositoryImpl(val context: Context) : MusicControllerRepository {

    private var mediaControllerFuture: ListenableFuture<MediaController>
    private var mediaController: MediaController? = null

    private val _playerState = MutableStateFlow<PlayerState>(PlayerState.NONE)
    override val playerState = _playerState.asStateFlow()

    private val _currentMusic = MutableStateFlow<Track?>(null)
    override val currentMusic = _currentMusic.asStateFlow()

    private val _currentPosition = MutableStateFlow<Long>(0L)
    override val currentPosition = _currentPosition.asStateFlow()

    private val _totalDuration = MutableStateFlow<Long>(0L)
    override val totalDuration = _totalDuration.asStateFlow()

    private val _isShuffleEnabled = MutableStateFlow<Boolean>(false)
    override val isShuffleEnabled = _isShuffleEnabled.asStateFlow()

    private val _isRepeatOneEnabled = MutableStateFlow<Boolean>(false)
    override val isRepeatOneEnabled = _isRepeatOneEnabled.asStateFlow()

    private var timeSecondsUpdateJob: Job? = null
    private var mainCoroutineScope = CoroutineScope(Dispatchers.Main)

    init {
        val sessionToken =
            SessionToken(context, ComponentName(context, MusicService::class.java))
        mediaControllerFuture = MediaController.Builder(context, sessionToken).buildAsync()
        mediaControllerFuture.addListener(
            {
                mediaController = mediaControllerFuture.get()
                controllerListener()
            }, MoreExecutors.directExecutor()
        )
    }

    private fun controllerListener() {
        mediaController?.addListener(object : Player.Listener {
            override fun onEvents(player: Player, events: Player.Events) {
                Timber.e("onEvents: player $player")
                with(player) {
                    _playerState.tryEmit(playbackState.toPlayerState(isPlaying))
                    _currentMusic.tryEmit(currentMediaItem?.toTrack())
                    _currentPosition.tryEmit(currentPosition)
                    _totalDuration.tryEmit(duration)
                    _isShuffleEnabled.tryEmit(shuffleModeEnabled)
                    _isRepeatOneEnabled.tryEmit(repeatMode == Player.REPEAT_MODE_ONE)

//                    mediaControllerCallback?.invoke(
//                        playbackState.toPlayerState(isPlaying),
//                        currentMediaItem?.toSong(),
//                        currentPosition.coerceAtLeast(0L),
//                        duration.coerceAtLeast(0L),
//                        shuffleModeEnabled,
//                        repeatMode == Player.REPEAT_MODE_ONE
//                    )
                }
                super.onEvents(player, events)
            }

            override fun onIsPlayingChanged(isPlaying: Boolean) {
                Timber.e("onIsPlayingChanged: $isPlaying")
                if(isPlaying) {
                    startUpdatesCurrentPosition()
                } else {
                    stopUpdatesCurrentPosition()
                }
                super.onIsPlayingChanged(isPlaying)
            }
        })
    }

    private fun startUpdatesCurrentPosition() {
        stopUpdatesCurrentPosition()
        timeSecondsUpdateJob = mainCoroutineScope.launch {
            while(true) {
                getCurrentPosition()?.let {
                    _currentPosition.tryEmit(it)
                }
                delay(1000)
            }
        }
    }

    private fun stopUpdatesCurrentPosition() {
        timeSecondsUpdateJob?.cancel()
        timeSecondsUpdateJob = null
    }

    private fun Int.toPlayerState(isPlaying: Boolean) =
        when (this) {
            Player.STATE_IDLE -> PlayerState.STOPPED
            Player.STATE_ENDED -> PlayerState.STOPPED
            else -> if (isPlaying) PlayerState.PLAYING else PlayerState.PAUSED
        }

    override fun addMediaItems(tracks: List<Track>) {
        setupMediaAudio()
    }

    private fun setupMediaAudio() {
        val urlLocal =
            "android.resource://" + context.packageName + "/" + com.jddev.simplemusic.data.R.raw.some_one_you_loved
        val artworkImage =
            Uri.parse("android.resource://" + context.packageName + "/" + com.jddev.simplemusic.data.R.drawable.artwork_img)

        val mediaItems =
            MediaItem.Builder()
                .setMediaId(urlLocal)
                .setUri(Uri.parse(urlLocal))
                .setMediaMetadata(
                    MediaMetadata.Builder()
                        .setTitle("Title Demo")
                        .setSubtitle("Sub title demo test")
                        .setArtist("Artist test")
                        .setArtworkUri(artworkImage)
                        .build()
                )
                .build()

        Timber.d("setupMediaAudio() mediaController != null: ${mediaController != null}")
        mediaController?.setMediaItem(mediaItems)
        mediaController?.prepare()
    }

    override fun play(mediaItemIndex: Int) {
        Timber.d("play() mediaItemIndex: $mediaItemIndex")
        Timber.d("play() mediaController != null: ${mediaController != null}")

        mediaController?.apply {
            seekToDefaultPosition(mediaItemIndex)
            playWhenReady = true
            prepare()
        }
    }

    override fun resume() {
        Timber.d("resume()")
        mediaController?.play()
    }

    override fun pause() {
        Timber.d("pause()")
        mediaController?.pause()
    }

    override fun getCurrentPosition(): Long? = mediaController?.currentPosition

    override fun destroy() {
        Timber.d("destroy()")
        MediaController.releaseFuture(mediaControllerFuture)
//        mediaControllerCallback = null

        timeSecondsUpdateJob?.cancel()
        timeSecondsUpdateJob = null
    }

    override fun skipToNextTrack() {
        mediaController?.seekToNext()
    }

    override fun skipToPreviousTrack() {
        mediaController?.seekToPrevious()
    }

    override fun getCurrentTrack(): Track? {
        return mediaController?.currentMediaItem?.toTrack()
    }

    override fun seekTo(position: Long) {
        mediaController?.seekTo(position)
    }
}