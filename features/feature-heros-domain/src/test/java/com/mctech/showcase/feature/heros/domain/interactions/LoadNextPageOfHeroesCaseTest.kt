package com.mctech.showcase.feature.heros.domain.interactions

import com.mctech.testing.data_factory.TestDataFactory
import com.mctech.showcase.feature.heros.domain.error.HeroError
import com.mctech.showcase.feature.heros.domain.service.HeroService
import com.nhaarman.mockitokotlin2.mock
import com.nhaarman.mockitokotlin2.whenever
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runBlockingTest
import org.assertj.core.api.Assertions
import org.junit.Before
import org.junit.Test

@ExperimentalCoroutinesApi
class LoadNextPageOfHeroesCaseTest{
    private val service = mock<HeroService>()
    private lateinit var loadNextPageOfHeroesCase: LoadNextPageOfHeroesCase

    @Before
    fun `before each test`() {
        loadNextPageOfHeroesCase = LoadNextPageOfHeroesCase(service)
    }

    @Test
    fun `should return heros`() = runBlockingTest {
        val expectedValue = TestDataFactory.createListOfHeroes()

        whenever(service.loadNextPageOfHeroes()).thenReturn(expectedValue)

        val result = loadNextPageOfHeroesCase.execute()

        Assertions.assertThat(result)
            .isExactlyInstanceOf(Result.Success::class.java)
            .isEqualTo(
                Result.Success(expectedValue)
            )
    }

    @Test
    fun `should return known exception`() = runBlockingTest {
        failureAssertion(
            exception = HeroError.UnknownQuotationException,
            expectedException = HeroError.UnknownQuotationException
        )
    }

    @Test
    fun `should return unknown exception`() = runBlockingTest {
        failureAssertion(
            exception = RuntimeException(),
            expectedException = HeroError.UnknownQuotationException
        )
    }

    private fun failureAssertion(exception: Throwable, expectedException: Exception) =
        runBlockingTest {
            whenever(service.loadNextPageOfHeroes()).thenThrow(exception)

            val result = loadNextPageOfHeroesCase.execute()
            val resultException = (result as Result.Failure).throwable

            Assertions.assertThat(result).isInstanceOf(Result.Failure::class.java)
            Assertions.assertThat(resultException).isEqualTo(expectedException)
        }

}