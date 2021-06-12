package com.example.basiccomponents.ui.dailyimage

import android.content.res.Configuration
import android.os.Bundle
import android.text.method.ScrollingMovementMethod
import android.util.Log
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.view.View
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate
import coil.load
import com.example.basiccomponents.R
import com.example.basiccomponents.network.repo.NasaRepository
import com.example.basiccomponents.ui.models.NasaDailyImage
import com.google.android.material.snackbar.Snackbar
import kotlinx.coroutines.*
import org.koin.android.ext.android.inject
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.ArrayList


class MainActivity : AppCompatActivity() {
    private val TAG = "MainActivity2"
    /**
     * Don't touch these 3 fields
     */
    private val nasaRepository by inject<NasaRepository>()
    private val coroutineScope = CoroutineScope(Dispatchers.IO)
    private val exceptionHandler = CoroutineExceptionHandler { _, throwable -> handleError(throwable) }


    private var nasaImage: ImageView? = null
    private lateinit var btLoad: Button
    private lateinit var tvExplanation: TextView
    private var menu: Menu? = null

    private var dates: List<Date> = DateUtils.loadDates(4)
    private var date: Date? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        Log.d(TAG, "onCreate")
        setContentView(R.layout.main_activity)
        setSupportActionBar(findViewById(R.id.my_toolbar))


        nasaImage = findViewById(R.id.iv_photo)
        tvExplanation = findViewById(R.id.tv_explanation)
        tvExplanation.movementMethod = ScrollingMovementMethod()

        btLoad = findViewById(R.id.bt_load_photo)
        btLoad.setOnClickListener {
            Toast.makeText(this, getString(R.string.load), Toast.LENGTH_SHORT).show()
            fetchDailyImage(date)
        }
    }

    private fun initViews(data: NasaDailyImage) {
        nasaImage?.load(data.imageUrl)
        tvExplanation.text = data.explanation
        supportActionBar?.title = data.title
        supportActionBar?.subtitle = data.date
        //TODO: apply data to your views
    }

    private fun handleError(t: Throwable) {
        Log.e(TAG, "exception!", t)
        showSnackbar(getString(R.string.network_error))
        //TODO: handle request's errors here
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        val inflater: MenuInflater = menuInflater
        inflater.inflate(R.menu.main_menu, menu)

        if (isNightModeEnabled()) {
            menu?.findItem(R.id.action_change_theme)?.setIcon(R.drawable.sharp_light_mode_white_48)
        }
        else{
            menu?.findItem(R.id.action_change_theme)?.setIcon(R.drawable.outline_nightlight_black_48)
        }

        dates.let{
            var title = DateUtils.toSimpleString(it[1])
            menu?.findItem(R.id.action_date_3)?.title = title
            title = DateUtils.toSimpleString(it[2])
            menu?.findItem(R.id.action_date_2)?.title = title
            title = DateUtils.toSimpleString(it[3])
            menu?.findItem(R.id.action_date_1)?.title = title
        }

        this.menu = menu
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        // Handle item selection
        return when (item.itemId) {
            R.id.action_change_theme -> {
                changeTheme()
                true
            }
            R.id.action_date_today ->
            {
                date = dates[0]
                true
            }
            R.id.action_date_3 ->
            {
                date = dates[1]
                true
            }
            R.id.action_date_2 ->{
                date = dates[2]
                true
            }
            R.id.action_date_1 ->
            {
                date = dates[3]
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }

    private fun isNightModeEnabled(): Boolean{
        val nightModeFlags: Int = resources.configuration.uiMode and
                Configuration.UI_MODE_NIGHT_MASK
        when (nightModeFlags) {
            Configuration.UI_MODE_NIGHT_YES ->  return true
            Configuration.UI_MODE_NIGHT_NO -> return false
            Configuration.UI_MODE_NIGHT_UNDEFINED -> return false
        }
        return false
    }

    private fun showSnackbar(message: String){
        val snackbar = Snackbar.make(
            findViewById(R.id.parent_layout),
            message,
            Snackbar.LENGTH_SHORT
        )

        class MyUndoListener : View.OnClickListener {
            override fun onClick(v: View) {
                snackbar.dismiss()
            }
        }

        snackbar.setAction(getString(R.string.ok), MyUndoListener())
            .show()
    }

    private class DateUtils {
        companion object {
            fun loadDates(count: Int): List<Date>{
                val result = ArrayList<Date>()
                for (i in 0 until count){
                    val date = dateBeforeToday(i)
                    result.add(date!!)
                }
                return result
            }

            private fun dateBeforeToday(shift: Int): Date? {
                val c = Calendar.getInstance()
                c.time = Date()
                c.add(Calendar.DATE, -shift)
                return c.time
            }

            fun toSimpleString(date: Date) : String {
                val format = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
                return format.format(date)
            }
        }
    }

    /**
     * Don't edit this function. Use only for fetching data.
     */
    private fun fetchDailyImage(date: Date? = null) {
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