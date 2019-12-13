package com.mctech.showcase.marvel.navigation

import android.view.View
import androidx.navigation.NavController
import androidx.navigation.fragment.FragmentNavigatorExtras
import com.mctech.showcase.feature.heros.HeroNavigation
import com.mctech.showcase.marvel.R

class AppNavigatorHandler : HeroNavigation {

    private var navController: NavController? = null

    override fun bind(navController: NavController) {
        this.navController = navController
    }

    override fun unbind() {
        navController = null
    }

    override fun navigateToDetails(vararg sharedElements: Pair<View, String>) {
        val extras = FragmentNavigatorExtras(*sharedElements)
        navController?.navigate(
            R.id.action_heroListFragment_to_heroDetailFragment,
            null,
            null,
            extras
        )
    }
}