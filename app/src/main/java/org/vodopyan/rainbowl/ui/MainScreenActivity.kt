package org.vodopyan.rainbowl.ui

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import androidx.appcompat.app.AppCompatActivity
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.player_controls_view.view.*
import org.koin.android.ext.android.inject
import org.vodopyan.rainbowl.R
import org.vodopyan.rainbowl.model.AppDataModel
import org.vodopyan.rainbowl.model.PlayerState


class MainScreenActivity : AppCompatActivity() {
    val dataModel by inject<AppDataModel>()
    val firstPlayerState = dataModel.players.first()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)


        val firstPlayerFragment = firstPlayer as PlayerControlsFragment
        firstPlayerFragment.setState(firstPlayerState)

        playersList.adapter = PlayersListAdapter(this, dataModel.players)
    }
}


class PlayersListAdapter(context: Context, players: List<PlayerState>)
    : ArrayAdapter<PlayerState>(context, R.layout.player_controls_view, players)
{
    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
        val playerState = getItem(position) ?: throw IndexOutOfBoundsException("No item at index $position")

        val inflater = LayoutInflater.from(context)
        val view = inflater.inflate(R.layout.player_controls_view, parent, /*attachToRoot=*/false)  // TODO: try reusing view

        view.name.text = playerState.sound.name
        view.volumeSeekBar.setOnSeekBarChangeListener(buildVolumeBarChangeListener(playerState, view.volumeSeekBar))
        view.playButton.setOnClickListener { playerState.isPlaying = true }
        view.stopButton.setOnClickListener { playerState.isPlaying = false }

        return view
    }
}