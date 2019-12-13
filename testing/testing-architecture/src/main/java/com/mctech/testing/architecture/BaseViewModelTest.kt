package com.mctech.testing.architecture

import com.mctech.testing.architecture.rules.CoroutinesMainTestRule
import com.mctech.testing.architecture.rules.KoinModuleTestRule
import org.junit.Rule
import org.koin.test.AutoCloseKoinTest

abstract class BaseViewModelTest : AutoCloseKoinTest() {
    @get:Rule
    val koinRule = KoinModuleTestRule()
    @get:Rule
    val coroutinesTestRule = CoroutinesMainTestRule()
}