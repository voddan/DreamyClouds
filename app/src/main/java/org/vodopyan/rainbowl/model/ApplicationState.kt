package org.vodopyan.rainbowl.model

import android.content.Context
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.google.android.exoplayer2.ExoPlayer
import com.google.android.exoplayer2.ExoPlayerFactory
import com.google.android.exoplayer2.source.LoopingMediaSource
import com.google.android.exoplayer2.source.ProgressiveMediaSource
import com.google.android.exoplayer2.upstream.DataSpec
import com.google.android.exoplayer2.upstream.RawResourceDataSource

class AppDataModel(val context: Context, val sounds: List<Sound>) {
    val players: List<MutableLiveData<PlayerState>> = sounds.map { sound ->
        val state = PlayerState(sound, isPlaying = false, volume = 0.0, context = context)
        MutableLiveData(state)
    }
}

data class Sound(
    val name: String,
    val audioResource: Int
)

/**
 * @param volume between 0.0 and 1.0
 * */
data class PlayerState(
    val sound: Sound,
    val isPlaying: Boolean,
    val volume: Double,
    private val context: Context,
    private val player: ExoPlayer = buildLoopingPlayer(context, sound.audioResource)
) {
    init {
        player.playWhenReady = isPlaying
        player.audioComponent!!.volume = volume.toFloat()
    }
}

private fun buildLoopingPlayer(context: Context, rawResourceId: Int): ExoPlayer {
    val uri = RawResourceDataSource.buildRawResourceUri(rawResourceId)
    val dataSource = RawResourceDataSource(context)
    dataSource.open(DataSpec(uri))

    val audioSource = ProgressiveMediaSource.Factory {dataSource}.createMediaSource(uri);
    val loopingAudio = LoopingMediaSource(audioSource)

    val player = ExoPlayerFactory.newSimpleInstance(context)
    player.prepare(loopingAudio)

    return player
}