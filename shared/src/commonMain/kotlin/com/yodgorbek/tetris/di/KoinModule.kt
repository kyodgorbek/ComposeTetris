package com.yodgorbek.tetris.di

import com.yodgorbek.tetris.game.logic.GameLogic
import com.yodgorbek.tetris.game.logic.Randomizer
import com.yodgorbek.tetris.ui.presentation.GameViewModel
import com.yodgorbek.tetris.util.AudioManager
import com.yodgorbek.tetris.util.PreferenceManager
import com.russhwolf.settings.Settings
import org.koin.core.context.startKoin
import org.koin.core.module.dsl.singleOf
import org.koin.core.module.dsl.viewModelOf
import org.koin.dsl.KoinAppDeclaration
import org.koin.dsl.module

val appModule = module {
    single { Settings() }
    singleOf(::PreferenceManager)
    singleOf(::AudioManager)
    singleOf(::GameLogic)
    singleOf(::Randomizer)
    viewModelOf(::GameViewModel)
}

fun initKoin(appDeclaration: KoinAppDeclaration = {}) =
    startKoin {
        appDeclaration()
        modules(appModule)
    }
