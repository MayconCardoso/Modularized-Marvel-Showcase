package com.mctech.showcase.feature.heros.domain.entity

data class Hero(
    val id: String,
    val name: String,
    val description: String?,
    val thumbnail: String,
    val comics: List<String>
)