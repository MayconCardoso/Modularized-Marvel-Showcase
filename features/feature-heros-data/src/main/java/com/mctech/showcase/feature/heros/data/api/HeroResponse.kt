package com.mctech.showcase.feature.heros.data.api

data class HeroResponse(
    val id: Long,
    val name: String,
    val description: String?,
    val thumbnail: Thumbnail,
    val comics : ListItem,
    val series : ListItem,
    val stories : ListItem,
    val events : ListItem
)

data class ComicsResponse (
    val thumbnail : Thumbnail
)

data class Thumbnail(
    val path: String,
    val extension: String
)

data class ListItem(
    val available : Int?
)