package com.mctech.testing.data_factory

import com.mctech.showcase.feature.heros.domain.entity.Comic
import com.mctech.showcase.feature.heros.domain.entity.Hero

object TestDataFactory {
    fun createListOfHeroes(count: Int = 0): List<Hero> {
        val list = mutableListOf<Hero>()
        for (x in 0 until count) {
            list.add(createHero())
        }
        return list
    }

    fun createHero(
        id: Long = 0,
        name: String = "",
        description: String = "",
        thumbnail: String = "",
        countStories: Int = 0,
        countSeries: Int = 0,
        countEvents: Int = 0,
        countComics: Int = 0
    ) = Hero(
        id,
        name,
        description,
        thumbnail,
        countComics,
        countSeries,
        countStories,
        countEvents
    )


    fun createListOfComic(count: Int = 0): List<Comic> {
        val list = mutableListOf<Comic>()
        for (x in 0 until count) {
            list.add(createComic())
        }
        return list
    }

    fun createComic(
        thumbnail: String = ""
    ) = Comic(
        thumbnail
    )
}