package com.mctech.showcase.architecture.components

import com.mctech.showcase.architecture.ComponentState
import org.assertj.core.api.Assertions
import org.junit.Test

class PaginatedComponentTest {

    @Test
    fun `should init components`() {
        val component = PaginatedComponent<String>()

        Assertions.assertThat(component.currentList.size).isEqualTo(0)
        Assertions.assertThat(component.isLoadingNextPage).isEqualTo(false)
    }

    @Test
    fun `should return loading from data`() {
        val component = PaginatedComponent<String>()
        component.currentList = mutableListOf(
            "Teste1",
            "Teste2"
        )

        Assertions.assertThat(component.loadingState()).isEqualTo(ComponentState.Loading.FromData)
    }

    @Test
    fun `should return loading from empty`() {
        val component = PaginatedComponent<String>()

        Assertions.assertThat(component.loadingState()).isEqualTo(ComponentState.Loading.FromEmpty)
    }
}