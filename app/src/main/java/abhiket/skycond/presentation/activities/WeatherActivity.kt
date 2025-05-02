package abhiket.skycond.presentation.activities

import abhiket.skycond.R
import abhiket.skycond.databinding.ActivityWeatherBinding
import abhiket.skycond.presentation.viewmodels.WeatherAdapter
import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat

class WeatherActivity : AppCompatActivity() {
    private lateinit var binding: ActivityWeatherBinding

    private val cities by lazy {
        listOf("Delhi", "Ahmedabad", "Mumbai", "Pune")
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = ActivityWeatherBinding.inflate(layoutInflater)
        setContentView(binding.root)
        ViewCompat.setOnApplyWindowInsetsListener(binding.activityWeatherMain) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        WeatherAdapter().apply {
            binding.pager.adapter = this
            setCities(cities)
        }

    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        val inflater: MenuInflater = menuInflater
        inflater.inflate(R.menu.menu_weather, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        // Handle item selection.
        return when (item.itemId) {
            R.id.manage_cities -> {
                true
            }

            R.id.add_new_ciy -> {
                true
            }

            else -> super.onOptionsItemSelected(item)
        }
    }

    private fun gotoAddCityActivity() {
        val intent = Intent(applicationContext, AddCityActivity::class.java)
        startActivity(intent)
    }

}