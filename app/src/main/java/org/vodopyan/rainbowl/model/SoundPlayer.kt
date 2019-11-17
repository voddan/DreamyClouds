@file:Suppress("NON_EXHAUSTIVE_WHEN")

package org.vodopyan.rainbowl.model

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.google.android.exoplayer2.ExoPlayer


/**
 * A mutable structure that keeps the state of [player] for a particular [sound].
 *
 * @param volume between 0.0 and 1.0
 * @param player must be a looping player playing [Sound.audioResource].
 *              Use [buildLoopingPlayer] to build a looping player.
 * */
class SoundPlayer(
    val sound: Sound,
    state: State,
    volume: Double,
    private val player: ExoPlayer,
    private val resumeAllOtherPlayers: () -> Unit
) {
    enum class State { Playing, Stopped, Paused }

    private val _state = MutableLiveData(state)
    val state: LiveData<State> = _state

    val volume: MutableLiveData<Double> = MutableLiveData(volume)

    init {
        this.state.observeForever { value -> player.playWhenReady = (value == State.Playing) }
        this.volume.observeForever { value -> player.audioComponent!!.volume = value.toFloat() }
    }

    fun pause() {
        when(_state.value) {
            State.Playing -> _state.value = State.Paused
        }
    }

    fun resume() {
        when(_state.value) {
            State.Paused -> _state.value = State.Playing
        }
    }

    fun play() {
        when(_state.value) {
            State.Stopped, State.Paused -> {
                _state.value = State.Playing
                resumeAllOtherPlayers()
            }
        }
    }

    fun stop() {
        when(_state.value) {
            State.Playing -> _state.value = State.Stopped
        }
    }
}