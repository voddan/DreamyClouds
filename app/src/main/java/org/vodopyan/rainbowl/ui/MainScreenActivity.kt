package org.vodopyan.rainbowl.ui

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.SeekBar
import androidx.appcompat.app.AppCompatActivity
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.player_controls.view.*
import org.koin.android.ext.android.get
import org.koin.android.ext.android.inject
import org.vodopyan.rainbowl.R
import org.vodopyan.rainbowl.model.AppDataModel
import org.vodopyan.rainbowl.model.PlayerState


class MainScreenActivity : AppCompatActivity() {
    val dataModel by inject<AppDataModel>()
    val playerState = dataModel.players.first()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        // TODO: make UI react on state changes
        volumeSeekBar.setOnSeekBarChangeListener(buildVolumeBarChangeListener(playerState, volumeSeekBar))
        playButton.setOnClickListener { playerState.isPlaying = true }
        stopButton.setOnClickListener { playerState.isPlaying = false }

        playersList.adapter = PlayersListAdapter(get(), dataModel.players)
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


class PlayersListAdapter(context: Context, players: List<PlayerState>)
    : ArrayAdapter<PlayerState>(context, R.layout.player_controls, players)
{
    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
        val playerState = getItem(position) ?: throw IndexOutOfBoundsException("No item at index $position")

        val inflater = LayoutInflater.from(context)
        val view = inflater.inflate(R.layout.player_controls, parent, /*attachToRoot=*/false)  // TODO: try reusing view

        view.name.text = playerState.sound.name
        view.volumeSeekBar.setOnSeekBarChangeListener(buildVolumeBarChangeListener(playerState, view.volumeSeekBar))
        view.playButton.setOnClickListener { playerState.isPlaying = true }
        view.stopButton.setOnClickListener { playerState.isPlaying = false }

        return view
    }
}