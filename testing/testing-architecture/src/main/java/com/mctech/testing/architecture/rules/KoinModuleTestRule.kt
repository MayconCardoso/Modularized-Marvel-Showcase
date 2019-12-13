package com.mctech.testing.architecture.rules

import com.mctech.showcase.library.logger.Logger
import com.mctech.showcase.library.logger.MutedLogger
import org.junit.rules.TestWatcher
import org.junit.runner.Description
import org.koin.core.context.startKoin
import org.koin.dsl.module

class KoinModuleTestRule : TestWatcher() {
    override fun starting(description: Description?) {
        super.starting(description)
        startKoin {
            modules(module {
                single { MutedLogger() as Logger }
            })
        }
    }
}
