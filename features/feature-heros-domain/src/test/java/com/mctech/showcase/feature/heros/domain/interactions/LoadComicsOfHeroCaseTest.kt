package com.mctech.showcase.feature.heros.domain.interactions

import com.mctech.showcase.feature.heros.domain.error.ComicError
import com.mctech.showcase.feature.heros.domain.service.HeroService
import com.mctech.testing.data_factory.TestDataFactory
import com.nhaarman.mockitokotlin2.mock
import com.nhaarman.mockitokotlin2.whenever
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runBlockingTest
import org.assertj.core.api.Assertions
import org.junit.Before
import org.junit.Test

@ExperimentalCoroutinesApi
class LoadComicsOfHeroCaseTest{
    private val service = mock<HeroService>()
    private val hero = TestDataFactory.createHero(
        id = 10
    )

    private lateinit var loadComicsOfHeroCase: LoadComicsOfHeroCase

    @Before
    fun `before each test`() {
        loadComicsOfHeroCase = LoadComicsOfHeroCase(service)
    }

    @Test
    fun `should return comics`() = runBlockingTest {
        val expectedValue = TestDataFactory.createListOfComic()

        whenever(service.loadComicsOfHero(hero)).thenReturn(expectedValue)

        val result = loadComicsOfHeroCase.execute(hero)

        Assertions.assertThat(result)
            .isExactlyInstanceOf(Result.Success::class.java)
            .isEqualTo(
                Result.Success(expectedValue)
            )
    }

    @Test
    fun `should return known exception`() = runBlockingTest {
        failureAssertion(
            exception = ComicError.UnknownQuotationException,
            expectedException = ComicError.UnknownQuotationException
        )
    }

    @Test
    fun `should return unknown exception`() = runBlockingTest {
        failureAssertion(
            exception = RuntimeException(),
            expectedException = ComicError.UnknownQuotationException
        )
    }

    private fun failureAssertion(exception: Throwable, expectedException: Exception) =
        runBlockingTest {
            whenever(service.loadComicsOfHero(hero)).thenThrow(exception)

            val result = loadComicsOfHeroCase.execute(hero)
            val resultException = (result as Result.Failure).throwable

            Assertions.assertThat(result).isInstanceOf(Result.Failure::class.java)
            Assertions.assertThat(resultException).isEqualTo(expectedException)
        }
}