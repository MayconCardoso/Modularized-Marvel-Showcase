package com.mctech.showcase.feature.heros.domain.interactions

import com.mctech.showcase.feature.heros.domain.entity.Comic
import com.mctech.showcase.feature.heros.domain.entity.Hero
import com.mctech.showcase.feature.heros.domain.error.ComicError
import com.mctech.showcase.feature.heros.domain.error.HeroError
import com.mctech.showcase.feature.heros.domain.error.NetworkException
import com.mctech.showcase.feature.heros.domain.service.HeroService

class LoadComicsOfHeroCase(private val service : HeroService){
    suspend fun execute(hero : Hero): Result<List<Comic>> = try {
        Result.Success(service.loadComicsOfHero(hero))
    } catch (error: Exception) {
        Result.Failure(
            if (error is NetworkException) error
            else ComicError.UnknownQuotationException
        )
    }
}