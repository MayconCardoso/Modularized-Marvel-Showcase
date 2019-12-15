package com.mctech.showcase.feature.heros.di

import com.mctech.showcase.feature.heros.HeroViewModel
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

/**
 * @author MAYCON CARDOSO on 2019-09-30.
 */
val heroesModule = module {
    viewModel {
        HeroViewModel(
            loadHeroesCase = get(),
            loadNextPageOfHeroesCase = get(),
            loadComicsOfHeroCase = get(),
            loadNexPageComicsOfHeroCase = get()
        )
    }
}