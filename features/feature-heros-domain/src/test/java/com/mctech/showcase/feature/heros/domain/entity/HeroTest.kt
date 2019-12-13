package com.mctech.showcase.feature.heros.domain.entity

import com.mctech.testing.data_factory.TestDataFactory
import kotlinx.coroutines.test.runBlockingTest
import org.assertj.core.api.Assertions.assertThat
import org.junit.Test

class HeroTest {
    @Test
    fun `should return heros`() = runBlockingTest {
        val expectedValue = TestDataFactory.createHero(
            countComics = 2,
            countSeries = 4,
            countEvents = 1,
            countStories = 12
        )

        assertThat(expectedValue.countOfComics()).isEqualTo("2 comics")
        assertThat(expectedValue.countOfSeries()).isEqualTo("4 series")
        assertThat(expectedValue.countOfEvents()).isEqualTo("1 events")
        assertThat(expectedValue.countOfStories()).isEqualTo("12 stories")
    }
}