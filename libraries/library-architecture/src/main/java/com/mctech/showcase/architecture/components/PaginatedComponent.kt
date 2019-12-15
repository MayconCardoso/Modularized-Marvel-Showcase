package com.mctech.showcase.architecture.components

class PaginatedComponent<T> {
    var currentList = mutableListOf<T>()
    var isLoadingNextPage = false
}