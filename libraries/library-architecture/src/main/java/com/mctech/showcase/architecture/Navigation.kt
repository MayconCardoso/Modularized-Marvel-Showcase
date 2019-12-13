package com.mctech.showcase.architecture

import androidx.navigation.NavController

interface Navigation {
    fun bind(navController: NavController)
    fun unbind()
}