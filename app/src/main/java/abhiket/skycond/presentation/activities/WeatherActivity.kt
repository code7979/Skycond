package abhiket.skycond.presentation.activities

import abhiket.skycond.R
import abhiket.skycond.data.CityWeatherRepositoryImpl
import abhiket.skycond.data.local.CityWeatherLocalDataSourceImpl
import abhiket.skycond.databinding.ActivityWeatherBinding
import abhiket.skycond.di.Singleton
import abhiket.skycond.presentation.adapter.WeatherAdapter
import abhiket.skycond.presentation.model.CityWeather
import abhiket.skycond.presentation.utils.ItemState
import abhiket.skycond.presentation.utils.UiState
import abhiket.skycond.presentation.viewmodels.WeatherViewModel
import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.view.View
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import androidx.viewpager2.widget.ViewPager2
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayoutMediator

class WeatherActivity : AppCompatActivity(), TabLayoutMediator.TabConfigurationStrategy,
    View.OnClickListener {
    private lateinit var binding: ActivityWeatherBinding
    private lateinit var weatherAdapter: WeatherAdapter

    //This will updated by viewpage and provide current page position
    private var currentPosition: Int = -1

    private val viewModel by viewModels<WeatherViewModel> {
        viewModelFactory {
            initializer {
                val singleton = Singleton.getInstance(applicationContext)
                val repository = singleton.appModule.cityWeatherRepository
                WeatherViewModel(repository)
            }
        }
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
        setSupportActionBar(binding.tbActivityWeather)
        binding.tbActivityWeather.setNavigationOnClickListener(this)

        val viewPager = binding.weatherViewPager
        weatherAdapter = WeatherAdapter()
        viewPager.adapter = weatherAdapter

        TabLayoutMediator(binding.indicatorTabLayout, viewPager, this).attach()
        viewPager.registerOnPageChangeCallback(object : ViewPager2.OnPageChangeCallback() {
            override fun onPageSelected(position: Int) {
                currentPosition = position
                supportActionBar?.let {
                    it.title = weatherAdapter.getCityName(position)
                    it.subtitle = weatherAdapter.getFormattedLastUpdate(position)
                }
            }
        })

        viewModel.cityWeatherState.observe(this) { state ->
            when (state) {
                is UiState.Loading -> {
                    binding.pbWeatherLoading.visibility = View.VISIBLE
                }

                is UiState.Success -> {
                    binding.pbWeatherLoading.visibility = View.GONE

                    val cityWeatherList: List<CityWeather> = state.data
                    weatherAdapter.setCityWeathers(cityWeatherList)
                }

                is UiState.Failure -> {
                    binding.pbWeatherLoading.visibility = View.GONE
                }
            }
        }

        viewModel.isUpdating.observe(this) { state ->
            when (state) {
                is ItemState.Loading -> {
                    val position = state.data
                    weatherAdapter.setUpdating(position, true)
                }

                is ItemState.Success -> {
                    val position = state.data
                    weatherAdapter.setUpdating(position, true)
                }

                is ItemState.Failure -> {
                    val position = state.data
                    val error = state.message.asString(this)
                    weatherAdapter.setUpdating(position, true)
                    Toast.makeText(this, error, Toast.LENGTH_SHORT).show()
                }
            }
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
            R.id.refresh -> {
                Toast.makeText(this, "Refreshing", Toast.LENGTH_SHORT).show()
                viewModel.onRefresh(currentPosition, weatherAdapter.getCity(currentPosition))
                true
            }

            else -> super.onOptionsItemSelected(item)
        }
    }

    override fun onConfigureTab(tab: TabLayout.Tab, position: Int) {
        // Do nothing
    }

    override fun onClick(view: View) {
        val activityIntent = Intent(this, ManagerActivity::class.java)
        startActivity(activityIntent)
    }

}