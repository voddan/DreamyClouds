package org.vodopyan.rainbowl.utils

import android.content.Context
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.LiveData
import androidx.lifecycle.Observer
import com.google.android.exoplayer2.ExoPlayer
import com.google.android.exoplayer2.ExoPlayerFactory
import com.google.android.exoplayer2.source.LoopingMediaSource
import com.google.android.exoplayer2.source.ProgressiveMediaSource
import com.google.android.exoplayer2.upstream.DataSpec
import com.google.android.exoplayer2.upstream.RawResourceDataSource

fun <T> LiveData<T>.observe(owner: LifecycleOwner, observer: (T) -> Unit) =
    observe(owner, Observer(observer))


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