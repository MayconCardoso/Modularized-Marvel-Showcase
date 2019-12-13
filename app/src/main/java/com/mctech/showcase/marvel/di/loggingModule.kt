package com.mctech.showcase.marvel.di

import com.mctech.showcase.library.logger.Logger
import com.mctech.showcase.library.logger.android.LogcatLogger
import org.koin.dsl.module

val loggingModule = module {
    single<Logger> { LogcatLogger }
}