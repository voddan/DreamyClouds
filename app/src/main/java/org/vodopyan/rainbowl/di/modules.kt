package org.vodopyan.rainbowl.di

import org.koin.dsl.module
import org.vodopyan.rainbowl.R
import org.vodopyan.rainbowl.model.AppDataModel
import org.vodopyan.rainbowl.model.Sound


val appModule = module {
    single {
        AppDataModel(
            context = get(),
            sounds = listOf(Sound("rain", R.raw.rain))
        )
    }
}