package com.mctech.showcase.feature.heros.data.datasource

import com.mctech.showcase.feature.heros.domain.entity.Comic
import com.mctech.showcase.feature.heros.domain.entity.Hero
import com.mctech.showcase.library.networking.marvel.api.Data

interface HeroDataSource {
    suspend fun loadHeroes(pageOffset: Int, countOfItems: Int = 10): Data<List<Hero>>
    suspend fun loadComicsOfHero(hero: Hero, pageOffset: Int, countOfItems: Int = 15): Data<List<Comic>>
}