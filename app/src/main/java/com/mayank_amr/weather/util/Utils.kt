package com.mayank_amr.weather.util

import android.view.View
import android.view.animation.TranslateAnimation

/**
 * @Project Weather
 * @Created_by Mayank Kumar on 02-05-2021 07:44 PM
 */

fun View.visible(isVisible: Boolean) {
    visibility = if (isVisible) View.VISIBLE else View.GONE
}

fun View.slideUp(duration: Int = 500) {
    visibility = View.VISIBLE
    val animate = TranslateAnimation(0f, 0f, this.height.toFloat(), 0f)
    animate.duration = duration.toLong()
    animate.fillAfter = true
    this.startAnimation(animate)
}