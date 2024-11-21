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

    private val _currentTrack = MutableStateFlow<Track?>(null)
    override val currentTrack = _currentTrack.asStateFlow()

    private val _isReady = MutableStateFlow<Boolean>(false)
    override val isReady = _isReady.asStateFlow()

    private val _playerState = MutableStateFlow<PlayerState>(PlayerState.NO_TRACK)
    override val playerState = _playerState.asStateFlow()

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

    private val availableTracks = mutableListOf<Track>()

    init {
        _isReady.tryEmit(false)
        val sessionToken =
            SessionToken(context, ComponentName(context, MusicService::class.java))
        mediaControllerFuture = MediaController.Builder(context, sessionToken).buildAsync()
        mediaControllerFuture.addListener(
            {
                mediaController = mediaControllerFuture.get()
                controllerListener()
                _isReady.tryEmit(false)
                Timber.d("mediaController ready")
            }, MoreExecutors.directExecutor()
        )
    }

    private fun controllerListener() {
        mediaController?.addListener(object : Player.Listener {
            override fun onEvents(player: Player, events: Player.Events) {
                Timber.e("onEvents: player $player")
                with(player) {
                    _playerState.tryEmit(playbackState.toPlayerState(isPlaying))
                    _currentTrack.tryEmit(currentMediaItem?.toTrack())
                    _currentPosition.tryEmit(currentPosition)
                    _totalDuration.tryEmit(duration)
                    _isShuffleEnabled.tryEmit(shuffleModeEnabled)
                    _isRepeatOneEnabled.tryEmit(repeatMode == Player.REPEAT_MODE_ONE)
                }
                super.onEvents(player, events)
            }

            override fun onIsPlayingChanged(isPlaying: Boolean) {
                Timber.e("onIsPlayingChanged: $isPlaying")
                if (isPlaying) {
                    startUpdatesCurrentPosition()
                } else {
                    stopUpdatesCurrentPosition()
                }
                super.onIsPlayingChanged(isPlaying)
            }

            override fun onMediaItemTransition(mediaItem: MediaItem?, reason: Int) {
                Timber.d("onMediaItemTransition track: ${mediaItem?.toTrack()?.title}")
                mediaItem?.toTrack()?.let { track ->
                    _currentTrack.tryEmit(track)
                }
                super.onMediaItemTransition(mediaItem, reason)
            }
        })
    }

    private fun startUpdatesCurrentPosition() {
        stopUpdatesCurrentPosition()
        timeSecondsUpdateJob = mainCoroutineScope.launch {
            while (true) {
                mediaController?.currentPosition?.let {
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
        if (tracks.isEmpty()) return
        Timber.d("addMediaItems tracks[0]: ${tracks[0].title}")
        Timber.d("addMediaItems tracks[0]: ${tracks[0].trackUrl}")
        availableTracks.clear()
        availableTracks.addAll(tracks)
        setupMediaAudioItems(tracks)
    }

    private fun setupMediaAudioItems(tracks: List<Track>) {
        val mediaItems = tracks.map { track ->
            MediaItem.Builder()
                .setMediaId(track.trackUrl)
                .setUri(Uri.parse(track.trackUrl))
                .setMediaMetadata(
                    MediaMetadata.Builder()
                        .setTitle(track.title)
                        .setSubtitle(track.subtitle)
                        .setArtist(track.subtitle)
                        .build()
                )
                .build()
        }
        mediaController?.setMediaItems(mediaItems)
        mediaController?.prepare()
    }

    override fun play(mediaId: String) {
        val mediaItemIndex = availableTracks.indexOfFirst { it.id == mediaId }
        mediaController?.apply {
            seekToDefaultPosition(mediaItemIndex)
            playWhenReady = true
            prepare()
        }
    }

    override fun resumeCurrentTrack() {
        Timber.d("resume()")
        mediaController?.play()
    }

    override fun pauseCurrentTrack() {
        Timber.d("pause()")
        mediaController?.pause()
    }

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

    override fun seekCurrentTrackTo(position: Long) {
        mediaController?.seekTo(position)
    }

    private fun emitTrackChanged(trackIndex: Int?) {
        if (trackIndex == null) return
        val track = availableTracks[trackIndex]
        _currentTrack.tryEmit(track)
    }
}