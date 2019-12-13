package com.mctech.showcase.feature.heros

import com.mctech.showcase.architecture.UserInteraction
import com.mctech.showcase.feature.heros.domain.entity.Hero

sealed class HeroViewInteraction : UserInteraction {
    object LoadFirstPage : HeroViewInteraction()
    object LoadNextPage : HeroViewInteraction()
    data class LoadDetails(val item: Hero) : HeroViewInteraction()
}