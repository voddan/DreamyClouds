package org.vodopyan.dreamyclouds.model

import android.content.Context
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import org.vodopyan.dreamyclouds.utils.buildLoopingPlayer

class AppDataModel(val context: Context, val sounds: List<Sound>) {
    val players: List<SoundPlayer> =
        sounds.map { sound ->
            val player = buildLoopingPlayer(context, sound.audioResource)
            SoundPlayer(sound,
                state = SoundPlayer.State.Stopped,
                volume = 0.0,
                player = player,
                resumeAllOtherPlayers = this::resumeAll
            )
        }

    private val mutAllCanPlay = MutableLiveData(false)
    val allCanPlay: LiveData<Boolean> = mutAllCanPlay

    fun pauseAll() {
        mutAllCanPlay.value = false
        players.forEach { it.pause() }
    }

    fun resumeAll() {
        mutAllCanPlay.value = true
        players.forEach { it.resume() }
    }
}