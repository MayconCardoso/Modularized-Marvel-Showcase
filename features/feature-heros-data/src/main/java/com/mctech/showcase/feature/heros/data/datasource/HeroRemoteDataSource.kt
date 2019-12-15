package com.mctech.showcase.feature.heros.data.datasource

import com.mctech.showcase.feature.heros.data.api.ComicsResponse
import com.mctech.showcase.feature.heros.data.api.HeroResponse
import com.mctech.showcase.feature.heros.data.api.HeroRetrofitApi
import com.mctech.showcase.feature.heros.domain.entity.Comic
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
        }.let { data ->
            Data(
                offset  = data.offset,
                count   = data.count,
                total   = data.total,
                limit   = data.limit,
                results = data.results.asHeroList().filter {
                    // Just to turn the app a little bit more beautiful we won't show heroes without image. :P
                    it.thumbnail.endsWith("image_not_available.jpg").not()
                }
            )
        }
    }

    override suspend fun loadComicsOfHero(hero: Hero, pageOffset: Int, countOfItems: Int): Data<List<Comic>> {
        return marvelApiRequest {
            api.getComicsOfSpecificHero(
                characterId = hero.id,
                offset = pageOffset,
                limit = countOfItems
            )
        }.let { data ->
            Data(
                offset  = data.offset,
                count   = data.count,
                total   = data.total,
                limit   = data.limit,
                results = data.results.asComicList().filter {
                    // Just to turn the app a little bit more beautiful we won't show heroes without image. :P
                    it.thumbnail.endsWith("image_not_available.jpg").not()
                }
            )
        }
    }
}

fun List<HeroResponse>.asHeroList() = this.map { heroResponse ->
    Hero(
        id = heroResponse.id,
        name = heroResponse.name,
        description = heroResponse.description,
        thumbnail = heroResponse.thumbnail.let {
            "${it.path}.${it.extension}".replace("http", "https")
        },
        countComics = heroResponse.comics.available,
        countEvents = heroResponse.events.available,
        countSeries = heroResponse.series.available,
        countStories = heroResponse.stories.available
    )
}

fun List<ComicsResponse>.asComicList() = this.map { comicResponse ->
    Comic(
        thumbnail = comicResponse.thumbnail.let {
            "${it.path}.${it.extension}".replace("http", "https")
        }
    )
}