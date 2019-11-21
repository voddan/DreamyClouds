package org.vodopyan.rainbowl.ui

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.lifecycle.MutableLiveData
import kotlinx.android.synthetic.main.player_controls_view.view.*
import org.vodopyan.rainbowl.R
import org.vodopyan.rainbowl.model.SoundPlayer
import org.vodopyan.rainbowl.utils.*

/**
 * Controls for an audio player
 */
class PlayerControlsView(context: Context, attrs: AttributeSet? = null) : ConstraintLayout(context, attrs) {
    private val parent = parentLifecycle(context)

    val state: MutableLiveData<SoundPlayer> = MutableLiveData()

    init {
        val view = LayoutInflater.from(context).inflate(R.layout.player_controls_view, /*root=*/this, /*attachToRoot=*/true)

        state.observe(parent) { playerState ->
            view.name.text = playerState.sound.name

            val pictogram = resources.getDrawable(playerState.sound.drawableResource, null)
            view.backgroundPictogram.setImageDrawable(pictogram)

            val backgroundColor = resources.getColor(playerState.sound.backgroundColor, null)
            view.setBackgroundColor(backgroundColor)

            playerState.volume.observe(parent) { volume ->
                view.volumeSeekBar.setNormalProgress(volume)
            }

            view.playButton.setOnClickListener { playerState.play() }
            view.stopButton.setOnClickListener { playerState.stop() }
            view.volumeSeekBar.setOnSeekBarChangeListener { seekBar -> playerState.volume.value = seekBar.normalProgress() }
        }
    }
}