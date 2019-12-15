package com.mctech.showcase.architecture.components

import org.assertj.core.api.Assertions
import org.junit.Test

class PaginatedComponentTest {

    @Test
    fun `should init components`() {
        val component = PaginatedComponent<String>()

        Assertions.assertThat(component.currentList.size).isEqualTo(0)
        Assertions.assertThat(component.isLoadingNextPage).isEqualTo(false)
    }
}