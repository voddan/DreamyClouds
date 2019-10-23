package org.vodopyan.rainbowl

import android.content.Context
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import com.google.android.exoplayer2.ExoPlayer
import com.google.android.exoplayer2.ExoPlayerFactory
import com.google.android.exoplayer2.source.LoopingMediaSource
import com.google.android.exoplayer2.source.ProgressiveMediaSource
import com.google.android.exoplayer2.upstream.DataSpec
import com.google.android.exoplayer2.upstream.RawResourceDataSource


class MainActivity : AppCompatActivity() {
    lateinit var mediaPlayer: ExoPlayer

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        mediaPlayer = buildLoopingPlayer(this, R.raw.rain)
    }

    fun startPlayer(view: View) {
        mediaPlayer.playWhenReady = true
    }

    fun stopPlayer(view: View) {
        mediaPlayer.playWhenReady = false
    }
}

fun buildLoopingPlayer(context: Context, rawResourceId: Int): ExoPlayer {
    val uri = RawResourceDataSource.buildRawResourceUri(rawResourceId)
    val dataSource = RawResourceDataSource(context)
    dataSource.open(DataSpec(uri))

    val audioSource = ProgressiveMediaSource.Factory {dataSource}.createMediaSource(uri);
    val loopingAudio = LoopingMediaSource(audioSource)

    val player = ExoPlayerFactory.newSimpleInstance(context)
    player.prepare(loopingAudio)

    return player
}
