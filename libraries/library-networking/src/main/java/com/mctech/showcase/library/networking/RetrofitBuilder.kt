package com.mctech.showcase.library.networking

import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object RetrofitBuilder {

    operator fun invoke(apiURL: String, httpClient: OkHttpClient) =
        with(Retrofit.Builder()) {
            baseUrl(apiURL)
            client(httpClient)
            addConverterFactory(GsonConverterFactory.create())
            build()
        }
}