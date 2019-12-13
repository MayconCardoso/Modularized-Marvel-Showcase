package com.mctech.showcase.feature.heros

import android.view.View
import com.mctech.showcase.architecture.Navigation

interface HeroNavigation : Navigation {
    fun navigateToDetails(vararg sharedElements : Pair<View, String>)
}