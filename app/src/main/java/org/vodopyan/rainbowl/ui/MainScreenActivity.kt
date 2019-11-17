package org.vodopyan.rainbowl.ui

import android.os.Bundle
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import androidx.activity.ComponentActivity
import androidx.appcompat.app.AppCompatActivity
import kotlinx.android.synthetic.main.activity_main.*
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

        firstPlayer.state.value = firstPlayerState

        playersList.adapter = PlayersListAdapter(this, dataModel.players)

        globalPlayPause.state.value = dataModel.allCanPlay
        globalPlayPause.playCallback.value = dataModel::resumeAll
        globalPlayPause.pauseCallback.value = dataModel::pauseAll
    }
}


class PlayersListAdapter(val activity: ComponentActivity, players: List<PlayerState>)
    : ArrayAdapter<PlayerState>(activity, R.layout.player_controls_view, players)
{
    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
        val playerState = getItem(position) ?: throw IndexOutOfBoundsException("No item at index $position")

        val view = PlayerControlsView(activity)
        view.state.value = playerState

        return view
    }
}