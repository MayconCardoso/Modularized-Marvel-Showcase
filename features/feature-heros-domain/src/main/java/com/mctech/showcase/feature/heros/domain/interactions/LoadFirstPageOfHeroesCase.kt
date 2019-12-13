package com.mctech.showcase.feature.heros.domain.interactions

import com.mctech.showcase.feature.heros.domain.entity.Hero
import com.mctech.showcase.feature.heros.domain.error.HeroError
import com.mctech.showcase.feature.heros.domain.error.NetworkException
import com.mctech.showcase.feature.heros.domain.service.HeroService

class LoadFirstPageOfHeroesCase(private val service: HeroService) {
    suspend fun execute(): Result<List<Hero>> = try {
        Result.Success(service.loadFirstPageOfHeroes())
    } catch (error: Exception) {
        Result.Failure(
            if (error is NetworkException) error
            else HeroError.UnknownQuotationException
        )
    }
}