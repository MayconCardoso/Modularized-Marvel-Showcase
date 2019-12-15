package com.mctech.showcase.feature.heros.data

import com.mctech.showcase.library.networking.marvel.api.Data

class DataPaginationLoader<T> {
    private var currentPagingOffset = 0
    private var hasLoadedAll = false

    suspend fun loadFirstPage(loader: suspend (offset : Int) -> Data<List<T>>): List<T> {
        return loader.invoke(0).let { response ->
            currentPagingOffset = response.count
            hasLoadedAll = false

            response.results.apply {
                hasLoadedAll = isEmpty()
            }
        }
    }

    suspend fun loadNextPageOfHeroes(loader: suspend (offset: Int) -> Data<List<T>>): List<T> {
        // It is done.
        if (hasLoadedAll) {
            return listOf()
        }

        return loader.invoke(currentPagingOffset).let { response ->
            currentPagingOffset += response.count

            // The response is empty, so it means that the list has been all loaded.
            response.results.apply {
                hasLoadedAll = isEmpty()
            }
        }
    }
}