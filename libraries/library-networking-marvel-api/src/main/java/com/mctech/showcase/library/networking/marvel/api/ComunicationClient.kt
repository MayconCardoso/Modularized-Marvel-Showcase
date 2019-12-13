package com.mctech.showcase.library.networking.marvel.api

import com.mctech.showcase.feature.heros.domain.error.NetworkException
import com.mctech.showcase.library.networking.secureRequest

suspend fun <DATA : Data<*>> marvelApiRequest(target: suspend () -> MarvelApiResponse<DATA>): DATA =
    try {
        secureRequest {
            val response = target.invoke()

            response.takeIf { it.code == 200 }?.data
                ?: throw IllegalArgumentException("Bad request. Code: ${response.code}")
        }
    } catch (ex: Throwable) {
        ex.printStackTrace()
        throw NetworkException()
    }