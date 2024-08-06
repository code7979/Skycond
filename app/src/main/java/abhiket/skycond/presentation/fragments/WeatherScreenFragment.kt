package abhiket.skycond.presentation.fragments

import abhiket.skycond.R
import abhiket.skycond.data.local.LocalDataSourceImpl
import abhiket.skycond.data.remote.RemoteDataSourceImpl
import abhiket.skycond.di.Singleton
import abhiket.skycond.domain.Repository
import abhiket.skycond.domain.RepositoryImpl
import abhiket.skycond.presentation.model.WeatherDataUi
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.flowWithLifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import kotlinx.coroutines.launch
import kotlin.math.log

class WeatherScreenFragment : Fragment() {
    private val viewmodel: WeatherViewModel by viewModels<WeatherViewModel> {
        viewModelFactory {
            initializer {
                val appModule = Singleton.getInstance(requireContext().applicationContext).appModule
                val repository: Repository = RepositoryImpl(
                    remoteDataSource = RemoteDataSourceImpl(appModule.weatherApi, appModule.apiKey),
                    localDataSource = LocalDataSourceImpl(appModule.database.weatherDataQueries)
                )
                WeatherViewModel(repository)
            }
        }
    }
    private lateinit var weatherIcon: ImageView
    private lateinit var progress: LinearLayout
    private lateinit var cityName: TextView
    private lateinit var date: TextView
    private lateinit var temperature: TextView
    private lateinit var weatherDescription: TextView
    private lateinit var temperatureFeelLike: TextView
    private lateinit var temperatureMax: TextView
    private lateinit var temperatureMin: TextView
    private lateinit var pressure: TextView
    private lateinit var humidity: TextView
    private lateinit var visibility: TextView
    private lateinit var clouds: TextView
    private lateinit var wind: TextView
    private lateinit var windDir: TextView

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View = inflater.inflate(R.layout.weather_screen_page, container, false).also {
        weatherIcon = it.findViewById(R.id.imageView)
        progress = it.findViewById(R.id.progress_circular_indicator)
        cityName = it.findViewById(R.id.tv_city_name)
        date = it.findViewById(R.id.tv_day_date)
        temperature = it.findViewById(R.id.tv_main_temp)
        weatherDescription = it.findViewById(R.id.weather_description)
        temperatureFeelLike = it.findViewById(R.id.tv_feel_like)
        temperatureMax = it.findViewById(R.id.tv_temp_max)
        temperatureMin = it.findViewById(R.id.tv_temp_min)
        pressure = it.findViewById(R.id.tv_pressure)
        humidity = it.findViewById(R.id.tv_humidity)
        visibility = it.findViewById(R.id.tv_visibility)
        clouds = it.findViewById(R.id.tv_clouds)
        wind = it.findViewById(R.id.tv_wind)
        windDir = it.findViewById(R.id.tv_wind_deg)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val cityId: Long = requireArguments().getLong(ARG_CITY_ID)
        viewmodel.getWeatherData(cityId)

        lifecycleScope.launch {
            viewmodel.uiState.flowWithLifecycle(lifecycle).collect { state ->
                state.weatherDataUi?.let { setDataToComponents(it) }
                showProgress(state.isLoading)
                state.message?.let {
                    val context = requireContext().applicationContext
                    Toast.makeText(
                        context,
                        context.getString(it),
                        Toast.LENGTH_SHORT
                    ).show()
                }
            }
        }
    }

    private fun setDataToComponents(weatherData: WeatherDataUi) {
        weatherIcon.setImageResource(weatherData.icon)
        cityName.text = weatherData.name
        temperature.text = weatherData.temp
        temperatureFeelLike.text = weatherData.feelsLike
        temperatureMin.text = weatherData.tempMin
        temperatureMax.text = weatherData.tempMax
        weatherDescription.text = weatherData.description
        clouds.text = weatherData.clouds
        wind.text = weatherData.winSpeed
        windDir.text = weatherData.windDeg
        humidity.text = weatherData.humidity
        visibility.text = weatherData.visibility
        pressure.text = weatherData.pressure
        date.text = weatherData.date
    }

    private fun showProgress(show: Boolean) {
        progress.visibility = if (show) View.VISIBLE else View.GONE
    }

    companion object {
        private const val ARG_CITY_ID = "cityId"
        private const val TAG = "WeatherScreenFragment"
        fun newInstance(cityId: Long): WeatherScreenFragment {
            val fragment = WeatherScreenFragment()
            val args = Bundle()
            args.putLong(ARG_CITY_ID, cityId)
            fragment.arguments = args
            return fragment
        }
    }

}
