package com.mctech.showcase.marvel

import android.app.Application
import com.mctech.showcase.feature.heros.data.di.heroesDataModule
import com.mctech.showcase.feature.heros.di.heroesModule
import com.mctech.showcase.marvel.di.loggingModule
import com.mctech.showcase.marvel.di.navigatorModule
import com.mctech.showcase.marvel.di.useCaseModules
import org.koin.android.ext.koin.androidContext
import org.koin.android.ext.koin.androidLogger
import org.koin.core.context.startKoin

class App : Application() {
    override fun onCreate() {
        super.onCreate()
        initDependencyInjection()
    }

    private fun initDependencyInjection() {
        startKoin {
            androidLogger()
            androidContext(this@App)
            modules(
                listOf(
                    // Platform
                    useCaseModules,

                    // Libraries
                    loggingModule,
                    heroesDataModule,
                    navigatorModule,

                    // Features
                    heroesModule
                )
            )
        }
    }
}
