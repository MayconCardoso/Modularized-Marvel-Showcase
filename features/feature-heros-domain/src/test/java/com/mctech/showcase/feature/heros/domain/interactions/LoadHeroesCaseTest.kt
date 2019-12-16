package com.mctech.showcase.feature.heros.domain.interactions

import com.mctech.testing.data_factory.TestDataFactory
import com.mctech.showcase.feature.heros.domain.error.HeroError
import com.mctech.showcase.feature.heros.domain.error.NetworkException
import com.mctech.showcase.feature.heros.domain.service.HeroService
import com.nhaarman.mockitokotlin2.mock
import com.nhaarman.mockitokotlin2.whenever
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runBlockingTest
import org.assertj.core.api.Assertions
import org.junit.Before
import org.junit.Test

@ExperimentalCoroutinesApi
class LoadHeroesCaseTest{
    private val service = mock<HeroService>()
    private lateinit var loadHeroesCase: LoadHeroesCase

    @Before
    fun `before each test`() {
        loadHeroesCase = LoadHeroesCase(service)
    }

    @Test
    fun `should return heros`() = runBlockingTest {
        val expectedValue = TestDataFactory.createListOfHeroes()
        val expectedResult = Result.Success(expectedValue)

        whenever(service.loadFirstPageOfHeroes()).thenReturn(expectedValue)

        val result = loadHeroesCase.execute()

        Assertions.assertThat(result)
            .isExactlyInstanceOf(Result.Success::class.java)
            .isEqualTo(expectedResult)
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

    @Test
    fun `should return network exception`() = runBlockingTest {
        failureAssertion(
            exception = NetworkException,
            expectedException = NetworkException
        )
    }

    private fun failureAssertion(exception: Throwable, expectedException: Exception) =
        runBlockingTest {
            whenever(service.loadFirstPageOfHeroes()).thenThrow(exception)

            val result = loadHeroesCase.execute()
            val resultException = (result as Result.Failure).throwable

            Assertions.assertThat(result).isInstanceOf(Result.Failure::class.java)
            Assertions.assertThat(resultException).isEqualTo(expectedException)
        }
}