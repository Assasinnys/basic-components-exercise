package com.example.basiccomponents.ui.dailyimage

import android.app.DatePickerDialog
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate
import androidx.appcompat.widget.SwitchCompat
import androidx.core.view.isVisible
import coil.load
import com.example.basiccomponents.R
import com.example.basiccomponents.databinding.ActivityMainBinding
import com.example.basiccomponents.network.repo.NasaRepository
import com.example.basiccomponents.ui.models.NasaDailyImage
import kotlinx.coroutines.*
import org.koin.android.ext.android.inject
import java.util.*

class MainActivity : AppCompatActivity() {

    /**
     * Don't touch these 3 fields
     */
    private val nasaRepository by inject<NasaRepository>()
    private var coroutineScope = CoroutineScope(Dispatchers.IO)
    private val exceptionHandler = CoroutineExceptionHandler { _, throwable -> handleError(throwable) }

    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setSupportActionBar(binding.toolbar)
//        fetchDailyImage()
    }

    private fun initViews(data: NasaDailyImage) {
        with(binding) {
            animationView.cancelAnimation()
            animationView.isVisible = false
            toolbar.subtitle = data.title
            ivNasaImage.load(data.imageUrl) {
                placeholder(R.drawable.nasa_logo_web_rgb)
                crossfade(true)
            }
            tvDate.text = data.date
            tvDescription.text = data.explanation
        }
    }

    private fun pickDate() {
        val currentDateTime = Calendar.getInstance()
        val currentYear = currentDateTime.get(Calendar.YEAR)
        val currentMonth = currentDateTime.get(Calendar.MONTH)
        val currentDay = currentDateTime.get(Calendar.DAY_OF_MONTH)

        DatePickerDialog(
            this,
            { _, year, month, day ->
                val newDate = Calendar.getInstance()
                newDate.set(year, month, day)
                if (newDate > currentDateTime) {
                    Toast.makeText(this, "Incorrect date!", Toast.LENGTH_LONG).show()
                } else {
                    val newDateInMillis = newDate.timeInMillis
                    fetchDailyImage(Date(newDateInMillis))
                }

            },
            currentYear,
            currentMonth,
            currentDay
        ).show()
    }

    private fun handleError(t: Throwable) {
        Log.e(MainActivity::class.java.simpleName, "exception! CurrentThread: ${Thread.currentThread().name}", t)
        //TODO: handle request's errors here
        runOnUiThread {
            binding.animationView.isVisible = true
            binding.animationView.playAnimation()
        }
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        val inflater = menuInflater
        inflater.inflate(R.menu.toolbar_menu, menu)
        val item = menu.findItem(R.id.theme_switch)
        item.setActionView(R.layout.switch_item)
        val mySwitch: SwitchCompat = item.actionView.findViewById(R.id.switchCompat)
        val isNightMode = AppCompatDelegate.MODE_NIGHT_YES == AppCompatDelegate.getDefaultNightMode()
        mySwitch.isChecked = isNightMode

        mySwitch.setOnCheckedChangeListener { _, _ ->
            changeTheme()
        }
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {

        when (item.itemId) {
            R.id.action_refresh -> {
                fetchDailyImage()
                return true
            }

            R.id.action_search -> {
                pickDate()
                return true
            }
        }
        return super.onOptionsItemSelected(item)
    }

    /**
     * Don't edit this function. Use only for fetching data.
     */
    private fun fetchDailyImage(date: Date? = null) {
        if (!coroutineScope.isActive) coroutineScope = CoroutineScope(Dispatchers.IO)

        coroutineScope.launch(exceptionHandler) {
            val result = nasaRepository.getDailyImage(date)
            Log.i(MainActivity::class.java.simpleName, "result: $result")

            withContext(Dispatchers.Main) {
                initViews(result)
            }
        }
    }

    /**
     * Changes the app theme to light or dark
     */
    private fun changeTheme() {
        var theme = AppCompatDelegate.getDefaultNightMode()
        theme = if (AppCompatDelegate.MODE_NIGHT_YES != theme)
            AppCompatDelegate.MODE_NIGHT_YES
        else
            AppCompatDelegate.MODE_NIGHT_NO

        AppCompatDelegate.setDefaultNightMode(theme)
    }

    override fun onStop() {
        super.onStop()
        coroutineScope.cancel()
    }
}