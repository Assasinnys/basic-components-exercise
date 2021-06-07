package com.example.basiccomponents.ui.viewModels

import android.util.Log
import androidx.lifecycle.ViewModel
import com.example.basiccomponents.ui.models.NasaDailyImage

class MainActivityViewModel: ViewModel() {
    private val TAG = "MainActivityViewModel"

    var dailyImage: NasaDailyImage? = null
    var isNightMode = false

    init {
        Log.d(TAG, "Init viewModel")
    }

}