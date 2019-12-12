package com.mctech.showcase.feature.heros.data.api

import com.mctech.showcase.feature.heros.domain.entity.Hero
import com.mctech.showcase.library.networking.marvel.api.Data
import com.mctech.showcase.library.networking.marvel.api.MarvelApiResponse
import retrofit2.http.GET
import retrofit2.http.Query

interface HeroRetrofitApi {
    @GET("characters")
    suspend fun getPaginatedHeroes(
        @Query("limit") limit: Int,
        @Query("offset") offset: Int
    ): MarvelApiResponse<Data<List<Hero>>>
}