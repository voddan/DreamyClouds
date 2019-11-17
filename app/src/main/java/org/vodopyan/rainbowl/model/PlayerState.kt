package org.vodopyan.rainbowl.model

import androidx.lifecycle.MutableLiveData
import com.google.android.exoplayer2.ExoPlayer


/**
 * A mutable structure that keeps the state of [player] for a particular [sound].
 *
 * @param volume between 0.0 and 1.0
 * @param player must be a looping player playing [Sound.audioResource].
 *              Use [buildLoopingPlayer] to build a looping player.
 * */
class PlayerState(
    val sound: Sound,
    isPlaying: Boolean,
    volume: Double,
    private val player: ExoPlayer
) {
    val isPlaying: MutableLiveData<Boolean> = MutableLiveData(isPlaying)
    val volume: MutableLiveData<Double> = MutableLiveData(volume)

    init {
        this.isPlaying.observeForever { value -> player.playWhenReady = value }
        this.volume.observeForever { value -> player.audioComponent!!.volume = value.toFloat() }
    }
}