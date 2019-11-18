package org.vodopyan.rainbowl.di

import org.koin.dsl.module
import org.vodopyan.rainbowl.R
import org.vodopyan.rainbowl.model.AppDataModel
import org.vodopyan.rainbowl.model.Sound


val appModule = module {
    single {
        AppDataModel(
            context = get(),
            sounds = listOf(
                Sound("rain", R.raw.rain, R.drawable.rain, R.color.rain_background),
                Sound("wind", R.raw.wind, R.drawable.wind_leaves, R.color.wind_background),
                Sound("stream", R.raw.stream, R.drawable.stream, R.color.stream_background),
                Sound("bird", R.raw.bird, R.drawable.bird, R.color.bird_background)
            )
        )
    }
}