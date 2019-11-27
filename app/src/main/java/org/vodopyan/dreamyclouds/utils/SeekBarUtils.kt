package org.vodopyan.dreamyclouds.utils

import android.widget.SeekBar

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