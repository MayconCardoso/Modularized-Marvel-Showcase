package com.mctech.showcase.feature.heros.data.datasource

import com.mctech.showcase.feature.heros.data.api.ComicsResponse
import com.mctech.showcase.feature.heros.data.api.HeroResponse
import com.mctech.showcase.feature.heros.data.api.ListItem
import com.mctech.showcase.feature.heros.data.api.Thumbnail
import com.mctech.showcase.feature.heros.domain.entity.Comic
import com.mctech.showcase.feature.heros.domain.entity.Hero
import org.assertj.core.api.Assertions
import org.junit.Test

class HeroRemoteDataSourceTest {

    @Test
    fun `should map response into a hero list`() {
        val response = listOf(
            HeroResponse(
                id = 10,
                name = "TesteName",
                description = "TesteDescription",
                thumbnail = Thumbnail(
                    path = "http:testeimagem.com",
                    extension = "jpg"
                ),
                comics = ListItem(
                    available = 10
                ),
                events = ListItem(
                    available = 20
                ),
                series = ListItem(
                    available = 30
                ),
                stories = ListItem(
                    available = 40
                )
            )
        )

        // Do the action
        val result = response.asHeroList()[0]

        // Assert mapped value
        Assertions.assertThat(result).isExactlyInstanceOf(Hero::class.java)
        Assertions.assertThat(result.id).isEqualTo(10)
        Assertions.assertThat(result.name).isEqualTo("TesteName")
        Assertions.assertThat(result.description).isEqualTo("TesteDescription")
        Assertions.assertThat(result.thumbnail).isEqualTo("https:testeimagem.com.jpg")
        Assertions.assertThat(result.countComics).isEqualTo(10)
        Assertions.assertThat(result.countEvents).isEqualTo(20)
        Assertions.assertThat(result.countSeries).isEqualTo(30)
        Assertions.assertThat(result.countStories).isEqualTo(40)
    }

    @Test
    fun `should map response into a comic list`() {
        val response = listOf(
            ComicsResponse(
                thumbnail = Thumbnail(
                    path = "http:testeimagem.com",
                    extension = "jpg"
                )
            )
        )

        // Do the action
        val result = response.asComicList()[0]

        // Assert mapped value
        Assertions.assertThat(result).isExactlyInstanceOf(Comic::class.java)
        Assertions.assertThat(result.thumbnail).isEqualTo("https:testeimagem.com.jpg")
    }
}