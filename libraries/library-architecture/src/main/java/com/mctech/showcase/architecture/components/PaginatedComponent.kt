package com.mctech.showcase.architecture.components

import com.mctech.showcase.architecture.ComponentState

class PaginatedComponent<T> {
    var currentList = mutableListOf<T>()
    var isLoadingNextPage = false

    fun loadingState() : ComponentState.Loading {
        return if(currentList.isEmpty())
            ComponentState.Loading.FromEmpty
        else
            ComponentState.Loading.FromData
    }
}