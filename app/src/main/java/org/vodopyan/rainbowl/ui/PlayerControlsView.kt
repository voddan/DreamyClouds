package org.vodopyan.rainbowl.ui

import android.annotation.SuppressLint
import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import android.widget.SeekBar
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import kotlinx.android.synthetic.main.player_controls_view.view.*
import org.vodopyan.rainbowl.R
import org.vodopyan.rainbowl.model.PlayerState
import org.vodopyan.rainbowl.utils.observe

/**
 * Controls for an audio player
 */
@SuppressLint("ViewConstructor")
class PlayerControlsView<Parent>(parent: Parent, attrs: AttributeSet? = null)
    : ConstraintLayout(parent, attrs) where Parent: Context, Parent: LifecycleOwner
{
    val state: MutableLiveData<MutableLiveData<PlayerState>> = MutableLiveData()

    private val livePlayerState: MutableLiveData<PlayerState>? get() = state.value

    init {
        val view = LayoutInflater.from(parent).inflate(R.layout.player_controls_view, /*root=*/this, /*attachToRoot=*/true)

        state.observe(parent) {
            livePlayerState?.observe(parent) { playerState ->
                view.name.text = playerState.sound.name
                view.volumeSeekBar.setNormalProgress(playerState.volume)
            }

            view.playButton.setOnClickListener { livePlayerState?.mutate { it.copy(isPlaying = true) } }
            view.stopButton.setOnClickListener { livePlayerState?.mutate { it.copy(isPlaying = false) } }
            view.volumeSeekBar.setOnSeekBarChangeListener { seekBar -> livePlayerState?.mutate { it.copy(volume = seekBar.normalProgress()) } }
        }

    }
}

/**
 * Normalised seek bar progress.
 * @return a value between 0.0 and 1.0
 * */
fun SeekBar.normalProgress(): Double = (progress - min) * 1.0 / (max - min)

/**
 * Sets a normalised seek bar position with [value].
 * @param value between 0.0 and 1.0
 * */
fun SeekBar.setNormalProgress(value: Double) {
    progress = (value * (max - min) + min).toInt().coerceIn(min, max)
}

/**
 * Adapter for SeekBar.setOnSeekBarChangeListener() to simplify using lambdas in Kotlin.
 * @param listener a function to be called on a change. Accepts [this] for convenience.
 * */
fun SeekBar.setOnSeekBarChangeListener(listener: (SeekBar) -> Unit) {
    setOnSeekBarChangeListener(object : SeekBar.OnSeekBarChangeListener {
        override fun onProgressChanged(seekBar: SeekBar, position: Int, fromUser: Boolean) {
            listener(seekBar)
        }

        override fun onStartTrackingTouch(seekBar: SeekBar) {}
        override fun onStopTrackingTouch(seekBar: SeekBar) {}
    })
}

/**
 * Mutates a non-null [value] of [this]
 * @param transform a transformer for [value]
 * */
fun <T: Any> MutableLiveData<T>.mutate(transform: (T) -> T) {
    val value = value
    if(value != null)
        setValue(transform(value))
}