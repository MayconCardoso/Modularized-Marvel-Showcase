package com.mctech.showcase.feature.heros

import com.mctech.showcase.architecture.ComponentState
import com.mctech.showcase.feature.heros.domain.entity.Comic
import com.mctech.showcase.feature.heros.domain.entity.Hero
import com.mctech.showcase.feature.heros.domain.error.ComicError
import com.mctech.showcase.feature.heros.domain.error.HeroError
import com.mctech.showcase.feature.heros.domain.interactions.LoadComicsOfHeroCase
import com.mctech.showcase.feature.heros.domain.interactions.LoadFirstPageOfHeroesCase
import com.mctech.showcase.feature.heros.domain.interactions.LoadNextPageOfHeroesCase
import com.mctech.showcase.feature.heros.domain.interactions.Result
import com.mctech.showcase.feature.heros.list.HeroListState
import com.mctech.testing.architecture.BaseViewModelTest
import com.mctech.testing.architecture.extention.*
import com.mctech.testing.data_factory.TestDataFactory
import com.nhaarman.mockitokotlin2.mock
import com.nhaarman.mockitokotlin2.whenever
import org.assertj.core.api.Assertions
import org.junit.Before
import org.junit.Test

class HeroViewModelTest : BaseViewModelTest() {
    private lateinit var viewModel: HeroViewModel

    private val expectationHeroes = TestDataFactory.createListOfHeroes()
    private val expectationComics = TestDataFactory.createListOfComic()
    private val expectedHero = TestDataFactory.createHero(
        id = 10
    )

    private val loadFirstPageOfHeroesCase = mock<LoadFirstPageOfHeroesCase>()
    private val loadNextPageOfHeroesCase = mock<LoadNextPageOfHeroesCase>()
    private val loadComicsOfHeroCase = mock<LoadComicsOfHeroCase>()

    @Before
    fun `before each test`() {
        viewModel = HeroViewModel(
            loadFirstPageOfHeroesCase,
            loadNextPageOfHeroesCase,
            loadComicsOfHeroCase
        )
    }

    @Test
    fun `should init components`() {
        viewModel.heroes.collectValuesForTesting {
            it.assertCount(1)
            it.assertFirst().isEqualTo(
                ComponentState.Initializing
            )
        }


        viewModel.comicHero.collectValuesForTesting {
            it.assertCount(1)
            it.assertFirst().isEqualTo(
                ComponentState.Initializing
            )
        }
    }

    @Test
    fun `should display first list`() {
        viewModel.heroes.test(
            scenario = {
                whenever(loadFirstPageOfHeroesCase.execute()).thenReturn(
                    Result.Success(
                        expectationHeroes
                    )
                )
            },
            action = {
                viewModel.interact(HeroViewInteraction.LoadFirstPage)
            },
            assertion = {
                val successValue = it[2] as ComponentState.Success<HeroListState>

                it.assertCount(3)
                it.assertAtPosition(0).isEqualTo(ComponentState.Initializing)
                it.assertAtPosition(1).isEqualTo(ComponentState.Loading)
                it.assertAtPosition(2).isExactlyInstanceOf(ComponentState.Success::class.java)

                Assertions.assertThat(successValue.result.heroes).isEqualTo(expectationHeroes)
                Assertions.assertThat(successValue.result.moveToTop).isTrue()
            }
        )
    }

    @Test
    fun `should display next list`() {
        viewModel.heroes.test(
            scenario = {
                whenever(loadNextPageOfHeroesCase.execute()).thenReturn(
                    Result.Success(
                        expectationHeroes
                    )
                )
            },
            action = {
                viewModel.interact(HeroViewInteraction.LoadNextPage)
            },
            assertion = {
                val successValue = it[2] as ComponentState.Success<HeroListState>

                it.assertCount(3)
                it.assertAtPosition(0).isEqualTo(ComponentState.Initializing)
                it.assertAtPosition(1).isEqualTo(ComponentState.Loading)
                it.assertAtPosition(2).isExactlyInstanceOf(ComponentState.Success::class.java)

                Assertions.assertThat(successValue.result.heroes).isEqualTo(expectationHeroes)
                Assertions.assertThat(successValue.result.moveToTop).isFalse()
            }
        )
    }

    @Test
    fun `should display error when loading heroes`() {
        viewModel.heroes.test(
            scenario = {
                whenever(loadFirstPageOfHeroesCase.execute()).thenReturn(
                    Result.Failure(
                        HeroError.UnknownQuotationException
                    )
                )
            },
            action = {
                viewModel.interact(HeroViewInteraction.LoadFirstPage)
            },
            assertion = {
                val successValue = it[2] as ComponentState.Error

                it.assertCount(3)
                it.assertFirst().isEqualTo(ComponentState.Initializing)
                it.assertAtPosition(2).isExactlyInstanceOf(ComponentState.Error::class.java)

                Assertions.assertThat(successValue.reason).isEqualTo(
                    HeroError.UnknownQuotationException
                )
            }
        )
    }


    @Test
    fun `should display comic of hero`() {
        viewModel.comicHero.test(
            scenario = {
                whenever(loadComicsOfHeroCase.execute(expectedHero)).thenReturn(
                    Result.Success(
                        expectationComics
                    )
                )
            },
            action = {
                viewModel.interact(HeroViewInteraction.LoadDetails(expectedHero))
            },
            assertion = {
                val successValue = it[2] as ComponentState.Success<List<Comic>>

                it.assertCount(3)
                it.assertAtPosition(0).isEqualTo(ComponentState.Initializing)
                it.assertAtPosition(1).isEqualTo(ComponentState.Loading)
                it.assertAtPosition(2).isExactlyInstanceOf(ComponentState.Success::class.java)

                Assertions.assertThat(successValue.result).isEqualTo(expectationComics)
            }
        )
    }

    @Test
    fun `should display error when loading comics`() {
        viewModel.comicHero.test(
            scenario = {
                whenever(loadComicsOfHeroCase.execute(expectedHero)).thenReturn(
                    Result.Failure(
                        ComicError.UnknownQuotationException
                    )
                )
            },
            action = {
                viewModel.interact(HeroViewInteraction.LoadDetails(expectedHero))
            },
            assertion = {
                val successValue = it[2] as ComponentState.Error

                it.assertCount(3)
                it.assertFirst().isEqualTo(ComponentState.Initializing)
                it.assertAtPosition(2).isExactlyInstanceOf(ComponentState.Error::class.java)

                Assertions.assertThat(successValue.reason).isEqualTo(
                    ComicError.UnknownQuotationException
                )
            }
        )
    }

}