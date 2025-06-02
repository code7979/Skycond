package abhiket.skycond.presentation.activities

import abhiket.skycond.R
import abhiket.skycond.databinding.ActivityWeatherBinding
import abhiket.skycond.di.Singleton
import abhiket.skycond.presentation.adapter.WeatherAdapter
import abhiket.skycond.presentation.model.CityWeatherMain
import abhiket.skycond.presentation.utils.ItemState
import abhiket.skycond.presentation.utils.UiState
import abhiket.skycond.presentation.viewmodels.WeatherViewModel
import android.app.Activity
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
import androidx.activity.result.contract.ActivityResultContracts.StartActivityForResult

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

    private val resultLauncher = registerForActivityResult(StartActivityForResult()) { result ->
        if (result.resultCode == Activity.RESULT_OK) {
            val intent = result.data
            if (intent != null) {
                val isReload = intent.getBooleanExtra(ManagerActivity.ACTION_RESULT_RELOAD, false)
                if (isReload) viewModel.loadCityWeatherData()
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
        weatherAdapter = WeatherAdapter(this)
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

        viewModel.cityWeatherMainState.observe(this) { state ->
            when (state) {
                is UiState.Loading -> {
                    binding.pbWeatherLoading.visibility = View.VISIBLE
                }

                is UiState.Success -> {
                    binding.pbWeatherLoading.visibility = View.GONE
                    val cityWeatherMainList: List<CityWeatherMain> = state.data
                    weatherAdapter.setCityWeathers(cityWeatherMainList)
                }

                is UiState.Failure -> {
                    binding.pbWeatherLoading.visibility = View.GONE
                    Toast.makeText(this, state.stringValue.asString(this), Toast.LENGTH_SHORT)
                        .show()
                }
            }
        }

        viewModel.isUpdating.observe(this) { state ->
            when (state) {
                is ItemState.Loading -> {
                    val position = state.position
                    weatherAdapter.setUpdating(position, true)
                }

                is ItemState.Success -> {
                    val position = state.position
                    val cityWeather = state.data
                    weatherAdapter.setUpdating(position, cityWeather)
                    if (currentPosition == position) {
                        supportActionBar?.let {
                            it.subtitle = weatherAdapter.getFormattedLastUpdate(position)
                        }
                    }
                }

                is ItemState.Failure -> {
                    val position = state.position
                    weatherAdapter.setUpdating(position, false)
                    Toast.makeText(this, state.stringValue.asString(this), Toast.LENGTH_SHORT)
                        .show()
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
        resultLauncher.launch(activityIntent)
    }

}