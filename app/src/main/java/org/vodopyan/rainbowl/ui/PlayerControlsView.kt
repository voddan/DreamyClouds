package org.vodopyan.rainbowl.ui

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.View
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.view.isVisible
import androidx.lifecycle.MutableLiveData
import kotlinx.android.synthetic.main.player_controls_view.view.*
import org.vodopyan.rainbowl.R
import org.vodopyan.rainbowl.model.SoundPlayer
import org.vodopyan.rainbowl.utils.*

/**
 * Controls for an audio player.
 * Set [PlayerControlsView.state] after initialization.
 * If [PlayerControlsView.state] is set to `null` the view becomes invisible.
 */
class PlayerControlsView(context: Context, attrs: AttributeSet? = null) : ConstraintLayout(context, attrs) {
    private val parent = parentLifecycle(context)

    val state: MutableLiveData<SoundPlayer?> = MutableLiveData()

    init {
        val view = LayoutInflater.from(context).inflate(R.layout.player_controls_view, /*root=*/this, /*attachToRoot=*/true)

        state.observe(parent, fun(playerState: SoundPlayer?) {
            if (playerState == null) {
                view.visibility = View.INVISIBLE
                view.backgroundPictogramButton.setOnClickListener(null)
                view.volumeSeekBar.setOnSeekBarChangeListener(null)
                return
            }

            val pictogram = resources.getDrawable(playerState.sound.drawableResource, null)
            view.backgroundPictogramButton.setImageDrawable(pictogram)

            view.backgroundPictogramButton.contentDescription = playerState.sound.name

            val backgroundColor = resources.getColor(playerState.sound.backgroundColor, null)
            view.setBackgroundColor(backgroundColor)

            playerState.volume.observe(parent) { volume ->
                view.volumeSeekBar.setNormalProgress(volume)
            }

            playerState.state.observe(parent) { state ->
                view.volumeSeekBar.isVisible = when (state) {
                    SoundPlayer.State.Playing -> true
                    SoundPlayer.State.Stopped -> false
                    SoundPlayer.State.Paused -> true
                }
            }

            view.backgroundPictogramButton.setOnClickListener { playerState.togglePlayStop() }
            view.volumeSeekBar.setOnSeekBarChangeListener { seekBar ->
                playerState.volume.value = seekBar.normalProgress()
            }
        })
    }
}