package com.mctech.showcase.architecture.extention

import androidx.fragment.app.Fragment
import androidx.lifecycle.LiveData
import androidx.lifecycle.Observer
import androidx.lifecycle.lifecycleScope
import com.mctech.showcase.architecture.ComponentState
import kotlinx.coroutines.launch

fun <T> Fragment.bindData(observable : LiveData<T>, block : (result : T) -> Unit) {
    lifecycleScope.launch {
        observable.observe(this@bindData, Observer {
            block(it)
        })
    }
}

fun <T> Fragment.bindState(observable : LiveData<ComponentState<T>>, block : (result : ComponentState<T>) -> Unit) {
    lifecycleScope.launch {
        observable.observe(this@bindState, Observer {
            block(it)
        })
    }
}
