package org.vodopyan.rainbowl.model

import android.content.Context
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import org.vodopyan.rainbowl.utils.buildLoopingPlayer

class AppDataModel(val context: Context, val sounds: List<Sound>) {
    val players: List<PlayerState> =
        sounds.map { sound ->
            val player = buildLoopingPlayer(context, sound.audioResource)
            PlayerState(sound,
                state = PlayerState.State.Stopped,
                volume = 0.0,
                player = player,
                resumeAllOtherPlayers = this::resumeAll
            )
        }

    private val _allCanPlay = MutableLiveData<Boolean>(true)
    val allCanPlay: LiveData<Boolean> = _allCanPlay

    fun pauseAll() {
        _allCanPlay.value = false
        players.forEach { it.pause() }
    }

    fun resumeAll() {
        _allCanPlay.value = true
        players.forEach { it.resume() }
    }
}