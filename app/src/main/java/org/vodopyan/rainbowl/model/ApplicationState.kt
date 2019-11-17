package org.vodopyan.rainbowl.model

import android.content.Context
import androidx.lifecycle.MutableLiveData
import org.vodopyan.rainbowl.utils.buildLoopingPlayer

class AppDataModel(val context: Context, val sounds: List<Sound>) {
    val players: List<PlayerState> =
        sounds.map { sound ->
            val player = buildLoopingPlayer(context, sound.audioResource)
            PlayerState(sound, isPlaying = false, volume = 0.0, player = player)
        }

    val isPlaying: MutableLiveData<Boolean> = MutableLiveData(true)
}