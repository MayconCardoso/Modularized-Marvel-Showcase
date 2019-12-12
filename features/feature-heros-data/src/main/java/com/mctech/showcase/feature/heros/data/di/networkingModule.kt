package com.mctech.showcase.feature.heros.data.di

import com.mctech.showcase.feature.heros.data.BuildConfig
import com.mctech.showcase.library.networking.RetrofitBuilder
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.Response
import okhttp3.logging.HttpLoggingInterceptor
import org.koin.dsl.module
import retrofit2.Retrofit
import java.util.concurrent.TimeUnit

val heroNetworkingModule = module {
    single {
        val logger = HttpLoggingInterceptor().apply {
            level = HttpLoggingInterceptor.Level.BODY
        }

        // It is gonna add the "api key" in every single request by changing the original URL.
        val securityApiKeyInterceptor = object : Interceptor{
            override fun intercept(chain: Interceptor.Chain): Response {
                val originalRequest = chain.request()
                val originalHttpUrl = originalRequest.url

                val newUrl = originalHttpUrl
                    .newBuilder()
                    .addQueryParameter("apikey", BuildConfig.MarvelApiPublicKey)
                    .build()

                return chain.proceed(originalRequest.newBuilder().url(newUrl).build())
            }
        }

        OkHttpClient.Builder()
            .addInterceptor(logger)
            .addInterceptor(securityApiKeyInterceptor)
            .connectTimeout(30, TimeUnit.SECONDS)
            .readTimeout(30, TimeUnit.SECONDS)
            .writeTimeout(30, TimeUnit.SECONDS)
            .build()
    }

    single {
        RetrofitBuilder(
            apiURL = "https://gateway.marvel.com/v1/public/",
            httpClient = get()
        ) as Retrofit
    }
}