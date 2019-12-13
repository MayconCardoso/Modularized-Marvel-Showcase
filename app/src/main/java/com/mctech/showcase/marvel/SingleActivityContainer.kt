package com.mctech.showcase.marvel

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.Navigation
import com.mctech.showcase.feature.heros.HeroNavigation
import org.koin.android.ext.android.inject

class SingleActivityContainer : AppCompatActivity() {
    private val navigator : HeroNavigation by inject()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_container)
    }

    override fun onStart() {
        super.onStart()
        navigator.bind(getNavigationController())
    }

    override fun onStop() {
        navigator.unbind()
        super.onStop()
    }

    override fun onSupportNavigateUp(): Boolean {
        return getNavigationController().navigateUp()
    }

    private fun getNavigationController() = Navigation.findNavController(this, R.id.nav_host_fragment)
}