package com.mctech.showcase.feature.heros

import com.mctech.showcase.architecture.UserInteraction
import com.mctech.showcase.feature.heros.domain.entity.Hero

sealed class HeroViewInteraction : UserInteraction {
    object LoadFirstPageOfHeroes : HeroViewInteraction()
    object LoadNextPageOfHeroes : HeroViewInteraction()
    data class LoadDetails(val item: Hero) : HeroViewInteraction()
    object LoadNextPageOfComics : HeroViewInteraction()
}