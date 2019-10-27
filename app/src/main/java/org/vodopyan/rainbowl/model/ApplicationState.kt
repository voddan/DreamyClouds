package org.vodopyan.rainbowl.model

import android.content.Context
import com.google.android.exoplayer2.ExoPlayer
import com.google.android.exoplayer2.ExoPlayerFactory
import com.google.android.exoplayer2.source.LoopingMediaSource
import com.google.android.exoplayer2.source.ProgressiveMediaSource
import com.google.android.exoplayer2.upstream.DataSpec
import com.google.android.exoplayer2.upstream.RawResourceDataSource

class AppDataModel(val context: Context, val sounds: List<Sound>) {
    val players: List<PlayerState> = sounds.map { sound ->
        val player = buildLoopingPlayer(context, sound.audioResource)
        PlayerState(sound, isPlaying = false, volume = 0.0, player = player)
    }
}

data class Sound(
    val name: String,
    val audioResource: Int
)

/**
 * @param volume between 0.0 and 1.0
 * */
class PlayerState(
    val sound: Sound,
    isPlaying: Boolean,
    volume: Double,
    private val player: ExoPlayer
) {
    var isPlaying: Boolean = isPlaying
        set(value) {
            field = value
            player.playWhenReady = value
        }

    var volume: Double = volume
        set(value) {
            field = value
            player.audioComponent!!.volume = value.toFloat()
        }

    init {
        // invoke the setters
        this.isPlaying = isPlaying
        this.volume = volume
    }
}

fun buildLoopingPlayer(context: Context, rawResourceId: Int): ExoPlayer {
    val uri = RawResourceDataSource.buildRawResourceUri(rawResourceId)
    val dataSource = RawResourceDataSource(context)
    dataSource.open(DataSpec(uri))

    val audioSource = ProgressiveMediaSource.Factory {dataSource}.createMediaSource(uri);
    val loopingAudio = LoopingMediaSource(audioSource)

    val player = ExoPlayerFactory.newSimpleInstance(context)
    player.prepare(loopingAudio)

    return player
}