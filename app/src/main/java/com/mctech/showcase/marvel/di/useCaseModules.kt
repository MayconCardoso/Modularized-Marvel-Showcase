package com.mctech.showcase.marvel.di

import com.mctech.showcase.feature.heros.domain.interactions.LoadComicsOfHeroCase
import com.mctech.showcase.feature.heros.domain.interactions.LoadHeroesCase
import com.mctech.showcase.feature.heros.domain.interactions.LoadNexPageComicsOfHeroCase
import com.mctech.showcase.feature.heros.domain.interactions.LoadNextPageOfHeroesCase
import org.koin.dsl.module

val useCaseModules = module {
    factory { LoadHeroesCase(service = get()) }
    factory { LoadNextPageOfHeroesCase(service = get()) }
    factory { LoadComicsOfHeroCase(service = get()) }
    factory { LoadNexPageComicsOfHeroCase(service = get()) }
}