package com.mctech.showcase.feature.heros.domain.interactions

import com.mctech.showcase.feature.heros.domain.error.ComicError
import com.mctech.showcase.feature.heros.domain.error.NetworkException
import com.mctech.showcase.feature.heros.domain.service.HeroService
import com.mctech.testing.data_factory.TestDataFactory
import com.nhaarman.mockitokotlin2.mock
import com.nhaarman.mockitokotlin2.whenever
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runBlockingTest
import org.assertj.core.api.Assertions
import org.junit.Before
import org.junit.Test

/**
 * @author MAYCON CARDOSO on 2019-12-15.
 */
@ExperimentalCoroutinesApi
class LoadNexPageComicsOfHeroCaseTest {
    private val service = mock<HeroService>()
    private lateinit var loadNexPageComicsOfHeroCase: LoadNexPageComicsOfHeroCase
    private val hero = TestDataFactory.createHero(
        id = 10
    )

    @Before
    fun `before each test`() {
        loadNexPageComicsOfHeroCase = LoadNexPageComicsOfHeroCase(service)
    }

    @Test
    fun `should return comics`() = runBlockingTest {
        val expectedValue = TestDataFactory.createListOfComic()

        whenever(service.loadNextPageComicsOfHero(hero)).thenReturn(expectedValue)

        val result = loadNexPageComicsOfHeroCase.execute(hero)

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
    fun `should return network exception`() = runBlockingTest {
        failureAssertion(
            exception = NetworkException,
            expectedException = NetworkException
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
            whenever(service.loadNextPageComicsOfHero(hero)).thenThrow(exception)

            val result = loadNexPageComicsOfHeroCase.execute(hero)
            val resultException = (result as Result.Failure).throwable

            Assertions.assertThat(result).isInstanceOf(Result.Failure::class.java)
            Assertions.assertThat(resultException).isEqualTo(expectedException)
        }

}