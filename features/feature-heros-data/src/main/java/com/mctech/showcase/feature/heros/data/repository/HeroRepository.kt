package com.mctech.showcase.feature.heros.data.repository

import com.mctech.showcase.feature.heros.data.datasource.HeroDataSource
import com.mctech.showcase.feature.heros.domain.entity.Hero
import com.mctech.showcase.feature.heros.domain.service.HeroService

class HeroRepository(private val dataSource: HeroDataSource) : HeroService {

    private var currentPagingOffset = 0
    private var hasLoadedAllHeroes = false

    override suspend fun loadFirstPageOfHeroes(): List<Hero> {
        return dataSource.loadHeroes(0).let { response ->
            currentPagingOffset = response.count
            hasLoadedAllHeroes = false

            response.results
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
}