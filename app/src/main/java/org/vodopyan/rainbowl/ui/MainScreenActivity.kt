package org.vodopyan.rainbowl.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import androidx.activity.ComponentActivity
import androidx.appcompat.app.AppCompatActivity
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.players_2_column_item.view.*
import org.koin.android.ext.android.inject
import org.vodopyan.rainbowl.R
import org.vodopyan.rainbowl.model.AppDataModel
import org.vodopyan.rainbowl.model.DeviceVolumeController
import org.vodopyan.rainbowl.model.SoundPlayer
import org.vodopyan.rainbowl.utils.normalProgress
import org.vodopyan.rainbowl.utils.setNormalProgress
import org.vodopyan.rainbowl.utils.setOnSeekBarChangeListener


class MainScreenActivity : AppCompatActivity() {
    val dataModel by inject<AppDataModel>()

    // TODO: make deviceVolume lifecycle-aware
    val deviceVolume by lazy{ DeviceVolumeController(this) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        playersList.adapter = PlayersListAdapter(this, dataModel.players)

        globalPlayPauseButton.state.value = dataModel.allCanPlay
        globalPlayPauseButton.playCallback.value = dataModel::resumeAll
        globalPlayPauseButton.pauseCallback.value = dataModel::pauseAll

        deviceVolume.volumeChangeCallback = { volume -> globalVolumeBar.setNormalProgress(volume) }
        globalVolumeBar.setNormalProgress(deviceVolume.getDeviceVolume())
        globalVolumeBar.setOnSeekBarChangeListener { volumeBar -> deviceVolume.setDeviceVolume(volumeBar.normalProgress()) }
    }
}


class PlayersListAdapter(val activity: ComponentActivity, players: List<SoundPlayer>) :
    ArrayAdapter<List<SoundPlayer>>(
        activity,
        R.layout.players_2_column_item,
        /*rows=*/players.chunked(2)
) {
    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
        val row = getItem(position) ?: throw IndexOutOfBoundsException("No pair for index $position")

        val view = convertView ?: LayoutInflater.from(context).inflate(R.layout.players_2_column_item, null)

        view.leftItem.state.value = row.getOrNull(0)
        view.rightItem.state.value = row.getOrNull(1)

        return view
    }
}