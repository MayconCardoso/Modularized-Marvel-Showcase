package com.mctech.showcase.feature.heros

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.mctech.showcase.architecture.BaseViewModel
import com.mctech.showcase.architecture.ComponentState
import com.mctech.showcase.architecture.UserInteraction
import com.mctech.showcase.feature.heros.domain.entity.Comic
import com.mctech.showcase.feature.heros.domain.entity.Hero
import com.mctech.showcase.feature.heros.domain.interactions.LoadComicsOfHeroCase
import com.mctech.showcase.feature.heros.domain.interactions.LoadFirstPageOfHeroesCase
import com.mctech.showcase.feature.heros.domain.interactions.LoadNextPageOfHeroesCase
import com.mctech.showcase.feature.heros.domain.interactions.Result
import com.mctech.showcase.feature.heros.list.HeroListState
import kotlinx.coroutines.launch

class HeroViewModel(
    private val loadFirstPageOfHeroesCase: LoadFirstPageOfHeroesCase,
    private val loadNextPageOfHeroesCase: LoadNextPageOfHeroesCase,
    private val loadComicsOfHeroCase: LoadComicsOfHeroCase
) : BaseViewModel() {

    // To control the pagination.
    private var currentListOfHeroes = mutableListOf<Hero>()
    private var isLoadingNextPage   = false

    // LiveData to storage the list state.
    private val _heroesComponent = MutableLiveData<ComponentState<HeroListState>>(ComponentState.Initializing)
    val heroes: LiveData<ComponentState<HeroListState>> = _heroesComponent

    private val _comicHero = MutableLiveData<ComponentState<List<Comic>>>(ComponentState.Initializing)
    val comicHero: LiveData<ComponentState<List<Comic>>> = _comicHero
    lateinit var selectedHero : Hero

    override suspend fun handleUserInteraction(interaction: UserInteraction) {
        when (interaction) {
            is HeroViewInteraction.LoadFirstPage    -> loadFistPageOfHeroesInteraction()
            is HeroViewInteraction.LoadNextPage     -> loadNextPageOfHeroesInteraction()
            is HeroViewInteraction.LoadDetails      -> loadHeroDetailsInteraction(interaction.item)
        }
    }

    private fun loadFistPageOfHeroesInteraction() = internalHeroesFetcher(true, mutableListOf()) {
        loadFirstPageOfHeroesCase.execute()
    }

    private fun loadNextPageOfHeroesInteraction() = synchronized(isLoadingNextPage) {
        // It is already loading the next page.
        // So in order to do not waste user's resources it just return the computation.
        if (isLoadingNextPage) return

        isLoadingNextPage = true
        internalHeroesFetcher(false, currentListOfHeroes) {
            loadNextPageOfHeroesCase.execute()
        }
    }

    private suspend fun loadHeroDetailsInteraction(item: Hero) {
        selectedHero = item

        // Show loading on component.
        _comicHero.value = ComponentState.Loading

        // Fetch heroes by calling the use case.
        when (val comicsResult = loadComicsOfHeroCase.execute(item)) {

            // Success when fetching heroes
            is Result.Success -> {
                // Update screen
                _comicHero.value = ComponentState.Success(
                    comicsResult.result
                )
            }

            // Failure when fetching heroes
            is Result.Failure -> comicsResult.throwable.apply {
                logger.e(message.orEmpty(), this)
                _comicHero.value = ComponentState.Error(this)
            }
        }
    }

    private fun internalHeroesFetcher(
        moveListToTop: Boolean,
        currentList: List<Hero>,
        loaderAgent: suspend () -> Result<List<Hero>>
    ) {
        viewModelScope.launch {
            // Show loading on component.
            _heroesComponent.value = ComponentState.Loading

            // Fetch heroes by calling the use case.
            when (val heroesResult = loaderAgent.invoke()) {

                // Success when fetching heroes
                is Result.Success -> {
                    // Merge the lists in order to add the next page on the previous one.
                    currentListOfHeroes = mutableListOf<Hero>().apply {
                        addAll(currentList)
                        addAll(heroesResult.result)
                    }

                    // Update screen
                    _heroesComponent.value = ComponentState.Success(
                        HeroListState(
                            currentListOfHeroes,
                            moveListToTop
                        )
                    )
                }

                // Failure when fetching heroes
                is Result.Failure -> heroesResult.throwable.apply {
                    logger.e(message.orEmpty(), this)
                    _heroesComponent.value = ComponentState.Error(this)
                }
            }

            // Release the loading control block.
            synchronized(isLoadingNextPage) {
                isLoadingNextPage = false
            }
        }
    }
}