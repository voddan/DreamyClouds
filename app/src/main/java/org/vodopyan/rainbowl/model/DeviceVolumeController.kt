package org.vodopyan.rainbowl.model

import android.content.Context
import android.database.ContentObserver
import android.media.AudioManager
import android.net.Uri
import android.util.Log
import androidx.core.content.ContextCompat


private val TAG = DeviceVolumeController::class.java.simpleName

/**
 * A class to control the device volume level
 * @param context application context
 * @param audioStreamType an indicator of an audio stream to control. One of `AudioManager.STREAM_*` constants.
 * @property volumeChangeCallback a function which is called when volume changes
 * */
class DeviceVolumeController(
    val context: Context,
    val audioStreamType: Int = AudioManager.STREAM_MUSIC
) {
    var volumeChangeCallback: (Double) -> Unit = {}

    private val audioService = ContextCompat.getSystemService(context, AudioManager::class.java)
        ?: throw IllegalStateException("Unable to retrieve AudioManager system service")

    // TODO: make lifecycle-aware (register and unregister the observer)
    init {
        val observer = object: ContentObserver(null) {
            override fun onChange(selfChange: Boolean, uri: Uri?) {
                if(uri == null) {
                    Log.e(TAG, "Unsupported content change; URI: $uri")
                    return
                }

                Log.i(TAG, "Content change uri: $uri")

                if(uri.encodedAuthority == "settings" &&
                    uri.encodedPath?.startsWith("/system/volume") == true)
                {
                    Log.i(TAG, "Volume change event: $uri")
                    val volume = getDeviceVolume()
                    volumeChangeCallback(volume)
                }
            }
        }

        // TODO: call [context.contentResolver.unregisterContentObserver()] somewhere
        context.contentResolver.registerContentObserver(android.provider.Settings.System.CONTENT_URI, true, observer)
    }

    /**
     * Current stream [audioStreamType] volume for device.
     *
     * @return volume between 0.0 and 1.1
     * */
    fun getDeviceVolume(): Double {
        val volume = audioService.getStreamVolume(audioStreamType)
        val maxVolume = audioService.getStreamMaxVolume(audioStreamType)
        return volume.toDouble() / maxVolume
    }

    /**
     * Tries to set stream [audioStreamType] volume for device.
     *
     * @param volume between 0.0 and 1.0
     * */
    fun setDeviceVolume(volume: Double) {
        val maxVolume = audioService.getStreamMaxVolume(audioStreamType)
        val volumeIndex = (maxVolume * volume).toInt().coerceIn(0, maxVolume)
        try {
            audioService.setStreamVolume(audioStreamType, volumeIndex, 0)
        } catch (e: SecurityException) {
            Log.e(TAG, "Triggered 'Do Not Disturb' mode: $e")
        } finally {
            val realVolume = getDeviceVolume()
            volumeChangeCallback(realVolume)
        }
    }
}