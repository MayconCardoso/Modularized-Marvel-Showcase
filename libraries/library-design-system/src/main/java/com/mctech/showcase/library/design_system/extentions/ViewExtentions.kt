package com.mctech.showcase.library.design_system.extentions

import android.view.View
import android.view.animation.AccelerateInterpolator
import android.view.animation.AlphaAnimation
import android.view.animation.Animation

fun View.animateShowByState(shouldShow: Boolean) : Animation{
    return if (shouldShow) animateShow()
    else animateHide()
}

fun View.animateHideByState(shouldShow: Boolean) : Animation{
    return animateShowByState(!shouldShow)
}

fun View.animateHide() : Animation {
    val animation = AlphaAnimation(1F, 0F).apply {
        duration = 200
        interpolator = AccelerateInterpolator()
        fillAfter = true
        setAnimationListener(object : AnimationListener() {
            override fun onAnimationEnd(animation: Animation?) {
                visibility = View.GONE
            }
        })
    }

    startAnimation(animation)
    return animation
}

fun View.animateShow(): Animation {
    val animation =  AlphaAnimation(0F, 1F).apply {
        duration = 200
        interpolator = AccelerateInterpolator()
        fillAfter = true
        setAnimationListener(object : AnimationListener() {
            override fun onAnimationStart(animation: Animation?) {
                visibility = View.VISIBLE
            }
        })
    }

    startAnimation(animation)
    return animation
}

abstract class AnimationListener : Animation.AnimationListener {
    override fun onAnimationRepeat(animation: Animation?) = Unit
    override fun onAnimationEnd(animation: Animation?) = Unit
    override fun onAnimationStart(animation: Animation?) = Unit
}