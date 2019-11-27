package org.vodopyan.dreamyclouds.model

/**
 * Immutable data that represents all information about a sound.
 * */
data class Sound(
    val name: String,
    val audioResource: Int,
    val drawableResource: Int,
    val backgroundColor: Int
)
