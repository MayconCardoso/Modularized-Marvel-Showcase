package com.mctech.showcase.feature.heros.list

import com.mctech.showcase.feature.heros.domain.entity.Hero

data class HeroListState(
    val heroes: List<Hero>,
    val moveToTop: Boolean
)