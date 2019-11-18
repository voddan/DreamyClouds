package org.vodopyan.rainbowl.ui

import android.annotation.SuppressLint
import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.MutableLiveData
import kotlinx.android.synthetic.main.player_controls_view.view.*
import org.vodopyan.rainbowl.R
import org.vodopyan.rainbowl.model.SoundPlayer
import org.vodopyan.rainbowl.utils.normalProgress
import org.vodopyan.rainbowl.utils.observe
import org.vodopyan.rainbowl.utils.setNormalProgress
import org.vodopyan.rainbowl.utils.setOnSeekBarChangeListener

/**
 * Controls for an audio player
 */
@SuppressLint("ViewConstructor")
class PlayerControlsView<Parent>(parent: Parent, attrs: AttributeSet? = null)
    : ConstraintLayout(parent, attrs) where Parent: Context, Parent: LifecycleOwner
{
    val state: MutableLiveData<SoundPlayer> = MutableLiveData()

    init {
        val view = LayoutInflater.from(parent).inflate(R.layout.player_controls_view, /*root=*/this, /*attachToRoot=*/true)

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