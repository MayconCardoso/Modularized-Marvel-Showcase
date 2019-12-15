package com.mctech.showcase.feature.heros

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.mctech.showcase.architecture.BaseViewModel
import com.mctech.showcase.architecture.ComponentState
import com.mctech.showcase.architecture.UserInteraction
import com.mctech.showcase.architecture.components.PaginatedComponent
import com.mctech.showcase.feature.heros.domain.entity.Comic
import com.mctech.showcase.feature.heros.domain.entity.Hero
import com.mctech.showcase.feature.heros.domain.interactions.*
import com.mctech.showcase.feature.heros.list.HeroListState
import kotlinx.coroutines.launch

class HeroViewModel(
    private val loadHeroesCase: LoadHeroesCase,
    private val loadNextPageOfHeroesCase: LoadNextPageOfHeroesCase,
    private val loadComicsOfHeroCase: LoadComicsOfHeroCase,
    private val loadNexPageComicsOfHeroCase: LoadNexPageComicsOfHeroCase
) : BaseViewModel() {

    // To control the pagination.
    private var heroPagination = PaginatedComponent<Hero>()
    private var comicPagination = PaginatedComponent<Comic>()

    // LiveData to storage the list state.
    private val _heroesComponent = MutableLiveData<ComponentState<HeroListState>>(ComponentState.Initializing)
    val heroes: LiveData<ComponentState<HeroListState>> = _heroesComponent

    private val _comicHero = MutableLiveData<ComponentState<List<Comic>>>(ComponentState.Initializing)
    val comicHero: LiveData<ComponentState<List<Comic>>> = _comicHero
    lateinit var selectedHero : Hero

    override suspend fun handleUserInteraction(interaction: UserInteraction) {
        when (interaction) {
            is HeroViewInteraction.LoadFirstPageOfHeroes    -> loadFistPageOfHeroesInteraction()
            is HeroViewInteraction.LoadNextPageOfHeroes     -> loadNextPageOfHeroesInteraction()
            is HeroViewInteraction.LoadDetails              -> loadHeroDetailsInteraction(interaction.item)
            is HeroViewInteraction.LoadNextPageOfComics     -> loadNextPageOfComicsInteraction()
        }
    }

    private fun loadFistPageOfHeroesInteraction() = internalHeroesFetcher(true, mutableListOf()) {
        loadHeroesCase.execute()
    }

    private fun loadNextPageOfHeroesInteraction() = synchronized(heroPagination.isLoadingNextPage) {
        if (heroPagination.isLoadingNextPage) return

        heroPagination.isLoadingNextPage = true
        internalHeroesFetcher(false, heroPagination.currentList) {
            loadNextPageOfHeroesCase.execute()
        }
    }

    private fun loadHeroDetailsInteraction(item: Hero) {
        selectedHero = item
        internalComicsFetcher(mutableListOf()){
            loadComicsOfHeroCase.execute(item)
        }
    }

    private fun loadNextPageOfComicsInteraction() = synchronized(comicPagination.isLoadingNextPage){
        if (comicPagination.isLoadingNextPage) return

        comicPagination.isLoadingNextPage = true
        internalComicsFetcher(comicPagination.currentList) {
            loadNexPageComicsOfHeroCase.execute(selectedHero)
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
                    heroPagination.currentList = mutableListOf<Hero>().apply {
                        addAll(currentList)
                        addAll(heroesResult.result)
                    }

                    // Update screen
                    _heroesComponent.value = ComponentState.Success(
                        HeroListState(
                            heroPagination.currentList,
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
            synchronized(heroPagination.isLoadingNextPage) {
                heroPagination.isLoadingNextPage = false
            }
        }
    }

    private fun internalComicsFetcher(
        currentList: List<Comic>,
        loaderAgent: suspend () -> Result<List<Comic>>
    ) {
        viewModelScope.launch {
            // Show loading on component.
            _comicHero.value = ComponentState.Loading

            // Fetch comics by calling the use case.
            when (val comicsResult = loaderAgent.invoke()) {

                // Success when fetching heroes
                is Result.Success -> {
                    // Merge the lists in order to add the next page on the previous one.
                    comicPagination.currentList = mutableListOf<Comic>().apply {
                        addAll(currentList)
                        addAll(comicsResult.result)
                    }

                    // Update screen
                    _comicHero.value = ComponentState.Success(
                        comicPagination.currentList
                    )
                }

                // Failure when fetching heroes
                is Result.Failure -> comicsResult.throwable.apply {
                    logger.e(message.orEmpty(), this)
                    _comicHero.value = ComponentState.Error(this)
                }
            }

            // Release the loading control block.
            synchronized(comicPagination.isLoadingNextPage) {
                comicPagination.isLoadingNextPage = false
            }
        }
    }
}