package com.example.basiccomponents.extensions

import android.animation.ValueAnimator
import android.view.View
import androidx.activity.OnBackPressedCallback
import androidx.core.animation.doOnEnd
import androidx.core.view.isVisible

fun View.alphaFade(b: Boolean): ValueAnimator? {
    val start = this.alpha
    val end = if (b) 1F else 0F
    if (this.alpha != end){
        val valueAnimator = ValueAnimator.ofFloat(start, end)
        valueAnimator.addUpdateListener {
            val value = it.animatedValue as Float
            this.alpha = value
        }
        valueAnimator.apply {
            duration = 500L
            start()
        }
        return valueAnimator
    }
    return null
}