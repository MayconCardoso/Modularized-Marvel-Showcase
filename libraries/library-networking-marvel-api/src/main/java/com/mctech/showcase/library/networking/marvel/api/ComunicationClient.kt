package com.mctech.showcase.library.networking.marvel.api

import com.mctech.showcase.feature.heros.domain.error.NetworkException
import com.mctech.showcase.library.networking.secureRequest

suspend fun <DATA : Data<*>> marvelApiRequest(target: suspend () -> MarvelApiResponse<DATA>) : DATA = try {
    secureRequest {
        // Call api function
        val response = target.invoke()

        // Check response. Return the specific data or throwing an exception.
        return@secureRequest response.takeIf { it.code == 200 }?.data
            ?: throw IllegalArgumentException("Bad request. Code: ${response.code}")
    }
}
catch (ex : Throwable){
    throw NetworkException()
}