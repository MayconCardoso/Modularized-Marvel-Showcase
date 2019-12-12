package com.mctech.showcase.feature.heros.data.di

import com.mctech.showcase.feature.heros.data.BuildConfig
import com.mctech.showcase.feature.heros.data.api.HeroRetrofitApi
import com.mctech.showcase.feature.heros.data.datasource.HeroDataSource
import com.mctech.showcase.feature.heros.data.datasource.HeroRemoteDataSource
import com.mctech.showcase.feature.heros.data.repository.HeroRepository
import com.mctech.showcase.feature.heros.domain.service.HeroService
import com.mctech.showcase.library.networking.RetrofitBuilder
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.Response
import okhttp3.logging.HttpLoggingInterceptor
import org.koin.dsl.module
import retrofit2.Retrofit
import java.math.BigInteger
import java.security.MessageDigest
import java.util.concurrent.TimeUnit

val heroesDataModule = module {
    single {
        val logger = HttpLoggingInterceptor().apply {
            level = HttpLoggingInterceptor.Level.BODY
        }

        // It is gonna add the "api key" in every single request by changing the original URL.
        val securityApiKeyInterceptor =
            createFixedParametersInterceptor()

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

    // Provide API
    single<HeroRetrofitApi> {
        val retrofit: Retrofit = get()
        retrofit.create(HeroRetrofitApi::class.java)
    }

    // Provide DataSource
    single {
        HeroRemoteDataSource(
            api = get()
        ) as HeroDataSource
    }

    // Provide Service
    single {
        HeroRepository(
            dataSource = get()
        ) as HeroService
    }
}

private fun createFixedParametersInterceptor(): Interceptor {
    return object : Interceptor {
        override fun intercept(chain: Interceptor.Chain): Response {

            val timeStamp = System.currentTimeMillis().toString()
            val apiPublicKey = BuildConfig.MarvelApiPublicKey
            val apiPrivateKey = BuildConfig.MarvelApiPrivateKey
            val apiSecurityHash = "$timeStamp$apiPrivateKey$apiPublicKey".md5()

            // Create a new URL with fixed parameters.
            val newUrl = chain.request().url
                .newBuilder()
                .addQueryParameter("ts", timeStamp)
                .addQueryParameter("apikey", apiPublicKey)
                .addQueryParameter("hash", apiSecurityHash)
                .build()

            // Return the intercepted URL with all fixed parameters.
            return chain.proceed(chain.request().newBuilder().url(newUrl).build())
        }
    }
}

private fun String.md5(): String {
    val md = MessageDigest.getInstance("MD5")
    return BigInteger(1, md.digest(toByteArray())).toString(16).padStart(32, '0')
}