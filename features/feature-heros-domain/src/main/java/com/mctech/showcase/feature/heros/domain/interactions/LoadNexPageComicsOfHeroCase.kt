package com.mctech.showcase.feature.heros.domain.interactions

import com.mctech.showcase.feature.heros.domain.entity.Comic
import com.mctech.showcase.feature.heros.domain.entity.Hero
import com.mctech.showcase.feature.heros.domain.error.ComicError
import com.mctech.showcase.feature.heros.domain.error.NetworkException
import com.mctech.showcase.feature.heros.domain.service.HeroService

class LoadNexPageComicsOfHeroCase(private val service: HeroService) {
    suspend fun execute(hero: Hero): Result<List<Comic>> = try {
        Result.Success(service.loadNextPageComicsOfHero(hero))
    } catch (error: Exception) {
        Result.Failure(
            if (error is NetworkException) error
            else ComicError.UnknownQuotationException
        )
    }
}