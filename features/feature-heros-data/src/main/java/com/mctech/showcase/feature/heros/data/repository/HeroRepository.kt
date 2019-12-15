package com.mctech.showcase.feature.heros.data.repository

import com.mctech.showcase.feature.heros.data.datasource.HeroDataSource
import com.mctech.showcase.feature.heros.domain.entity.Comic
import com.mctech.showcase.feature.heros.domain.entity.Hero
import com.mctech.showcase.feature.heros.domain.service.HeroService

class HeroRepository(private val dataSource: HeroDataSource) : HeroService {

    private var currentPagingOffset = 0
    private var hasLoadedAllHeroes = false

    private var currentPagingComicsOffset = 0
    private var hasLoadedAllComicsOfHeroes = false

    override suspend fun loadFirstPageOfHeroes(): List<Hero> {
        return dataSource.loadHeroes(0).let { response ->
            currentPagingOffset = response.count
            hasLoadedAllHeroes = false

            response.results.apply {
                hasLoadedAllHeroes = isEmpty()
            }
        }
    }

    override suspend fun loadNextPageOfHeroes(): List<Hero> {
        // It is done.
        if (hasLoadedAllHeroes) {
            return listOf()
        }

        return dataSource.loadHeroes(currentPagingOffset).let { response ->
            currentPagingOffset += response.count

            // The response is empty, so it means that the list has been all loaded.
            response.results.apply {
                hasLoadedAllHeroes = isEmpty()
            }
        }
    }

    override suspend fun loadFirstPageComicsOfHero(hero: Hero): List<Comic> {
        return dataSource.loadComicsOfHero(hero, 0).let { response ->
            currentPagingComicsOffset = response.count
            hasLoadedAllComicsOfHeroes = false

            response.results.apply {
                hasLoadedAllComicsOfHeroes = isEmpty()
            }
        }
    }

    override suspend fun loadNextPageComicsOfHero(hero: Hero): List<Comic> {
        // It is done.
        if (hasLoadedAllComicsOfHeroes) {
            return listOf()
        }

        return dataSource.loadComicsOfHero(hero, currentPagingComicsOffset).let { response ->
            currentPagingComicsOffset += response.count

            // The response is empty, so it means that the list has been all loaded.
            response.results.apply {
                hasLoadedAllComicsOfHeroes = isEmpty()
            }
        }
    }
}