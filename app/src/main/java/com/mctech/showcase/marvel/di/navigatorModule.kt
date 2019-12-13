package com.mctech.showcase.marvel.di

import com.mctech.showcase.feature.heros.HeroNavigation
import com.mctech.showcase.marvel.navigation.AppNavigatorHandler
import org.koin.dsl.module

val navigatorModule = module {

    single <HeroNavigation> {
        AppNavigatorHandler()
    }

}