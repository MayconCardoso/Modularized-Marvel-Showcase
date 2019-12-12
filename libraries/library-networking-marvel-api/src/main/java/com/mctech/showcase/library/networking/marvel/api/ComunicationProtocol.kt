package com.mctech.showcase.library.networking.marvel.api

data class MarvelApiResponse<out DATA : Data<*>>(
    val code: Int,
    val status: String,
    val data: DATA
)

data class Data<out RESULT>(
    val offset: Int,
    val limit: Int,
    val total: Int,
    val count: Int,
    val results: RESULT
)