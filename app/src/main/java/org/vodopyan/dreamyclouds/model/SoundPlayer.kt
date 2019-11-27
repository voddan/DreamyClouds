@file:Suppress("NON_EXHAUSTIVE_WHEN")

package org.vodopyan.dreamyclouds.model

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

    private val mutState = MutableLiveData(state)
    val state: LiveData<State> = mutState

    val volume: MutableLiveData<Double> = MutableLiveData(volume)

    init {
        this.state.observeForever { value -> player.playWhenReady = (value == State.Playing) }
        this.volume.observeForever { value -> player.audioComponent!!.volume = value.toFloat() }
    }

    fun pause() {
        when(state.value) {
            State.Playing -> mutState.value = State.Paused
        }
    }

    fun resume() {
        when(state.value) {
            State.Paused -> mutState.value = State.Playing
        }
    }

    fun play() {
        when(state.value) {
            State.Stopped, State.Paused -> {
                mutState.value = State.Playing
                resumeAllOtherPlayers()
            }
        }
    }

    fun stop() {
        when(state.value) {
            State.Playing -> mutState.value = State.Stopped
        }
    }

    fun togglePlayStop() {
        when(state.value) {
            State.Stopped, State.Paused -> play()
            State.Playing -> stop()
        }
    }
}