package com.mctech.showcase.architecture.components

import android.view.View
import android.view.View.GONE
import android.view.View.VISIBLE
import androidx.databinding.BindingAdapter
import com.mctech.showcase.architecture.ComponentState

@BindingAdapter("showOnLoading", requireAll = false)
fun View.showOnLoading(state: ComponentState<*>) {
    visibility = if(state is ComponentState.Loading)
        VISIBLE
    else
        GONE
}

@BindingAdapter("showOnSuccess", requireAll = false)
fun View.showOnSuccess(state: ComponentState<*>) {
    visibility = if(state is ComponentState.Success)
        VISIBLE
    else
        GONE
}

@BindingAdapter("showOnError", requireAll = false)
fun View.showOnError(state: ComponentState<*>) {
    visibility = if(state is ComponentState.Error)
        VISIBLE
    else
        GONE
}

@BindingAdapter("hideOnError", requireAll = false)
fun View.hideOnError(state: ComponentState<*>) {
    visibility = if(state is ComponentState.Error)
        GONE
    else
        visibility
}