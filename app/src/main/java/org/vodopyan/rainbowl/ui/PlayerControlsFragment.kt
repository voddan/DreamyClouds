package org.vodopyan.rainbowl.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.SeekBar
import androidx.fragment.app.Fragment
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProviders
import kotlinx.android.synthetic.main.player_controls_view.view.*
import org.vodopyan.rainbowl.R
import org.vodopyan.rainbowl.model.PlayerState


/**
 * Encapsulates controls for an audio player
 */
class PlayerControlsFragment : Fragment() {
    private lateinit var state: State

    class State : ViewModel() {
        val playerState = MutableLiveData<PlayerState>()
    }

    public fun setState(state: PlayerState) {
        this.state.playerState.value = state
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        state = ViewModelProviders.of(this).get(State::class.java)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.player_controls_view, /*root=*/container, /*attachToRoot=*/false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        this.arguments = savedInstanceState

        state.playerState.observe({ this.lifecycle }) { playerState ->
            view.name.text = playerState.sound.name
            view.playButton.setOnClickListener { playerState.isPlaying = true }
            view.stopButton.setOnClickListener { playerState.isPlaying = false }

//             TODO: make UI react on state changes
            view.volumeSeekBar.setOnSeekBarChangeListener(buildVolumeBarChangeListener(playerState, view.volumeSeekBar))
        }
    }

}

fun buildVolumeBarChangeListener(mediaPlayer: PlayerState, volumeBar: SeekBar): SeekBar.OnSeekBarChangeListener {
    fun SeekBar.normalProgress() = (progress - min) * 1.0 / (max - min)

    mediaPlayer.volume = volumeBar.normalProgress()

    return object : SeekBar.OnSeekBarChangeListener {
        private val mediaPlayer = mediaPlayer
        private val volumeBar = volumeBar

        override fun onProgressChanged(seekBar: SeekBar, position: Int, fromUser: Boolean) {
            assert(seekBar == volumeBar) { "Cannot apply change from a different volume bar" }
            mediaPlayer.volume = volumeBar.normalProgress()
        }

        override fun onStartTrackingTouch(seekBar: SeekBar) {}
        override fun onStopTrackingTouch(seekBar: SeekBar) {}
    }
}
