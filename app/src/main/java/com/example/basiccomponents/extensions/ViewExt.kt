package com.example.basiccomponents.extensions

import android.animation.ValueAnimator
import android.view.View
import androidx.activity.OnBackPressedCallback
import androidx.core.animation.doOnEnd
import androidx.core.view.isVisible

fun View.alphaFade(b: Boolean) {
    val start = if (b) 0F else 1F
    val end = if (b) 1F else 0F

    val valueAnimator = ValueAnimator.ofFloat(start, end)
    valueAnimator.addUpdateListener {
        val value = it.animatedValue as Float
        this.alpha = value
    }
    valueAnimator.apply {
        duration = 500L
        start()
    }
}