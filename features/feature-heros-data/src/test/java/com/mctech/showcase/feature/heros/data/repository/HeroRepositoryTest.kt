package com.mctech.showcase.feature.heros.data.repository

import com.mctech.showcase.feature.heros.data.datasource.HeroDataSource
import com.mctech.showcase.feature.heros.domain.entity.Comic
import com.mctech.showcase.feature.heros.domain.entity.Hero
import com.mctech.testing.data_factory.TestDataFactory
import com.mctech.showcase.library.networking.marvel.api.Data
import com.nhaarman.mockitokotlin2.*
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runBlockingTest
import org.assertj.core.api.Assertions
import org.junit.Before
import org.junit.Test

@ExperimentalCoroutinesApi
class HeroRepositoryTest{
    private val dataSource = mock<HeroDataSource>()
    private lateinit var repository: HeroRepository
    private val hero = TestDataFactory.createHero(
        id = 10
    )

    @Before
    fun `before each test`() {
        repository = HeroRepository(dataSource)
    }

    @Test
    fun `should load first page`() = runBlockingTest {
        val expectedValue = TestDataFactory.createListOfHeroes()
        val expectedData  = Data(
            offset = 0,
            limit = 10,
            total = 10,
            count = 10,
            results = expectedValue
        )

        whenever(dataSource.loadHeroes(0, 10)).thenReturn(expectedData)

        val result = repository.loadFirstPageOfHeroes()

        Assertions.assertThat(result).isEqualTo(expectedValue)
    }

    @Test
    fun `should load next page`() = runBlockingTest {
        val expectedValue = TestDataFactory.createListOfHeroes(20)
        val expectedData  = Data(
            offset = 0,
            limit = 10,
            total = 10,
            count = 10,
            results = expectedValue
        )

        whenever(dataSource.loadHeroes(0)).thenReturn(expectedData)
        whenever(dataSource.loadHeroes(10)).thenReturn(expectedData)

        // Load first page to accumulate items.
        repository.loadFirstPageOfHeroes()
        verify(dataSource).loadHeroes(0, 10)

        // Load next page.
        repository.loadNextPageOfHeroes()
        verify(dataSource).loadHeroes(10, 10)
    }

    @Test
    fun `should not load when all items have been loaded`() = runBlockingTest {
        val expectedValue = listOf<Hero>()
        val expectedData  = Data(
            offset = 0,
            limit = 0,
            total = 0,
            count = 0,
            results = expectedValue
        )

        whenever(dataSource.loadHeroes(0)).thenReturn(expectedData)

        // Load first page to accumulate items.
        repository.loadFirstPageOfHeroes()
        verify(dataSource).loadHeroes(0, 10)

        // Load next page.
        repository.loadNextPageOfHeroes()
        verifyNoMoreInteractions(dataSource)
    }

    @Test
    fun `should load first page of comics`() = runBlockingTest {
        val expectedValue = TestDataFactory.createListOfComic()
        val expectedData  = Data(
            offset = 0,
            limit = 10,
            total = 10,
            count = 10,
            results = expectedValue
        )

        whenever(dataSource.loadComicsOfHero(hero, 0)).thenReturn(expectedData)

        // Load first page to accumulate items.
        repository.loadFirstPageComicsOfHero(hero)
        verify(dataSource).loadComicsOfHero(hero, 0)
    }

    @Test
    fun `should load next page of comics`() = runBlockingTest {
        val expectedValue = TestDataFactory.createListOfComic(30)
        val expectedData  = Data(
            offset = 0,
            limit = 15,
            total = 15,
            count = 15,
            results = expectedValue
        )

        whenever(dataSource.loadComicsOfHero(hero, 0)).thenReturn(expectedData)
        whenever(dataSource.loadComicsOfHero(hero, 15)).thenReturn(expectedData)

        // Load first page to accumulate items.
        repository.loadFirstPageComicsOfHero(hero)
        verify(dataSource).loadComicsOfHero(hero, 0, 15)

        // Load next page.
        repository.loadNextPageComicsOfHero(hero)
        verify(dataSource).loadComicsOfHero(hero, 15, 15)
    }

    @Test
    fun `should not load when all comics items have been loaded`() = runBlockingTest {
        val expectedValue = listOf<Comic>()
        val expectedData  = Data(
            offset = 0,
            limit = 0,
            total = 0,
            count = 0,
            results = expectedValue
        )

        whenever(dataSource.loadComicsOfHero(hero, 0)).thenReturn(expectedData)

        // Load first page to accumulate items.
        repository.loadFirstPageComicsOfHero(hero)
        verify(dataSource).loadComicsOfHero(hero, 0, 15)

        // Load next page.
        repository.loadNextPageComicsOfHero(hero)
        verifyNoMoreInteractions(dataSource)
    }
}