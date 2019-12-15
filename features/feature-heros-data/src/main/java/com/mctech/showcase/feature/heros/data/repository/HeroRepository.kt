package com.mctech.showcase.feature.heros.data.repository

import com.mctech.showcase.feature.heros.data.DataPaginationLoader
import com.mctech.showcase.feature.heros.data.datasource.HeroDataSource
import com.mctech.showcase.feature.heros.domain.entity.Comic
import com.mctech.showcase.feature.heros.domain.entity.Hero
import com.mctech.showcase.feature.heros.domain.service.HeroService

class HeroRepository(private val dataSource: HeroDataSource) : HeroService {

    private val heroesPageLoader = DataPaginationLoader<Hero>()
    private val comicsPageLoader = DataPaginationLoader<Comic>()

    override suspend fun loadFirstPageOfHeroes() = heroesPageLoader.loadFirstPage{
        dataSource.loadHeroes(it)
    }

    override suspend fun loadNextPageOfHeroes() = heroesPageLoader.loadNextPageOfHeroes{
        dataSource.loadHeroes(it)
    }

    override suspend fun loadFirstPageComicsOfHero(hero: Hero) = comicsPageLoader.loadFirstPage{
        dataSource.loadComicsOfHero(hero, it)
    }

    override suspend fun loadNextPageComicsOfHero(hero: Hero) = comicsPageLoader.loadNextPageOfHeroes {
        dataSource.loadComicsOfHero(hero, it)
    }
}