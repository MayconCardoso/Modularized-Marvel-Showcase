package com.mctech.showcase.library.networking.marvel.api

import com.mctech.showcase.feature.heros.domain.error.NetworkException
import kotlinx.coroutines.runBlocking
import org.assertj.core.api.Assertions
import org.junit.Test

class ComunicationClientKtTest{

    @Test
    fun `should return a known network error`() {
        runBlocking {

            val result = runCatching {
                marvelApiRequest {
                    MarvelApiResponse(
                        code = 404,
                        status = "Bad request",
                        data = null
                    )
                }
            }.exceptionOrNull()

            Assertions.assertThat(result)
                .isExactlyInstanceOf(
                    NetworkException::class.java
                )
        }
    }

    @Test
    fun `should return success block`() {
        runBlocking {

            val expectedValue = Data(
                offset = 0,
                limit = 0,
                total = 0,
                count = 0,
                results = "TesteOK"
            )

            val result = runCatching {
                marvelApiRequest {
                    MarvelApiResponse(
                        code = 200,
                        status = "Ok",
                        data = expectedValue
                    )
                }
            }.getOrNull()

            Assertions.assertThat(result).isEqualTo(expectedValue)
        }
    }
}