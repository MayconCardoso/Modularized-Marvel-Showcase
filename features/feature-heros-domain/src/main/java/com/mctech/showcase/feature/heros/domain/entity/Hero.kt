package com.mctech.showcase.feature.heros.domain.entity

data class Hero(
    val id: Long,
    val name: String,
    val description: String?,
    val thumbnail: String,
    val countComics : Int?,
    val countSeries : Int?,
    val countStories : Int?,
    val countEvents : Int?
){
    fun countOfComics() = "$countComics comics"
    fun countOfSeries() = "$countSeries series"
    fun countOfEvents() = "$countEvents events"
    fun countOfStories() = "$countStories stories"
}