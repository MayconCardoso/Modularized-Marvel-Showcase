package com.mctech.showcase.feature.heros.domain.service

import com.mctech.showcase.feature.heros.domain.entity.Comic
import com.mctech.showcase.feature.heros.domain.entity.Hero

interface HeroService {
    suspend fun loadFirstPageOfHeroes(): List<Hero>
    suspend fun loadNextPageOfHeroes(): List<Hero>
    suspend fun loadFirstPageComicsOfHero(hero: Hero): List<Comic>
    suspend fun loadNextPageComicsOfHero(hero: Hero): List<Comic>
}