package com.mctech.showcase.feature.heros.data.datasource

import com.mctech.showcase.feature.heros.data.api.HeroRetrofitApi
import com.mctech.showcase.feature.heros.domain.entity.Hero
import com.mctech.showcase.library.networking.marvel.api.Data
import com.mctech.showcase.library.networking.marvel.api.marvelApiRequest

class HeroRemoteDataSource(
    private val api: HeroRetrofitApi
) : HeroDataSource{
    override suspend fun loadHeroes(pageOffset: Int, countOfItems: Int): Data<List<Hero>> {
        return marvelApiRequest {
            api.getPaginatedHeroes(
                limit   = countOfItems,
                offset  = pageOffset
            )
        }
    }
}