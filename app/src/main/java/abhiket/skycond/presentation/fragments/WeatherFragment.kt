package abhiket.skycond.presentation.fragments

import abhiket.skycond.R
import abhiket.skycond.presentation.model.WeatherDataUi
import abhiket.skycond.presentation.viewmodels.WeatherViewModel
import abhiket.skycond.uitls.ScreenState
import abhiket.skycond.uitls.toWeatherDataUi
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.ActionBar
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment

class WeatherFragment : Fragment() {
    private val viewmodel: WeatherViewModel by viewModels<WeatherViewModel> {
        WeatherViewModel.createViewModelFactory(requireContext().applicationContext)
    }

    private lateinit var progress:            LinearLayout
    private lateinit var weatherIcon:         ImageView
    private lateinit var weatherDescription:  TextView
    private lateinit var temperatureFeelLike: TextView
    private lateinit var temperatureMax:      TextView
    private lateinit var temperatureMin:      TextView
    private lateinit var visibility:          TextView
    private lateinit var temperature:         TextView
    private lateinit var pressure:            TextView
    private lateinit var humidity:            TextView
    private lateinit var cityName:            TextView
    private lateinit var clouds:              TextView
    private lateinit var wind:                TextView
    private lateinit var date:                TextView



    /*********** lifecycle states and callback s***********/
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View = inflater.inflate(R.layout.fragment_weather, container, false).also {
        progress            = it.findViewById(R.id.progress_circular_indicator)
        weatherIcon         = it.findViewById(R.id.imageView)
        weatherDescription  = it.findViewById(R.id.weather_description)
        temperatureFeelLike = it.findViewById(R.id.tv_feel_like)
        temperatureMax      = it.findViewById(R.id.tv_temp_max)
        temperatureMin      = it.findViewById(R.id.tv_temp_min)
        temperature         = it.findViewById(R.id.tv_main_temp)
        visibility          = it.findViewById(R.id.tv_visibility)
        cityName            = it.findViewById(R.id.tv_city_name)
        pressure            = it.findViewById(R.id.tv_pressure)
        humidity            = it.findViewById(R.id.tv_humidity)
        clouds              = it.findViewById(R.id.tv_clouds)
        wind                = it.findViewById(R.id.tv_wind)
        date                = it.findViewById(R.id.tv_date)



    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewmodel.isLoading.observe(viewLifecycleOwner) { state ->
            when (state) {
                is ScreenState.Failure -> {
                    showProgress(false)
                    val context = requireContext().applicationContext
                    Toast.makeText(
                        context,
                        context.getString(state.stringRes),
                        Toast.LENGTH_SHORT
                    ).show()
                }

                is ScreenState.Loading -> {
                    showProgress()
                }

                is ScreenState.Success -> {
                    showProgress(false)
                }
            }
        }

        viewmodel.weatherDataEntity.observe(viewLifecycleOwner) { entity ->
            if (entity != null) {
                setDataToComponents(entity.toWeatherDataUi())
                viewmodel.updateWeatherData()
            } else {
                Toast.makeText(
                    requireContext().applicationContext,
                    R.string.entity_found_null,
                    Toast.LENGTH_SHORT
                ).show()
            }
        }
    }


    /************************** END ******************************/

    private fun setDataToComponents(weatherData: WeatherDataUi) {
        weatherIcon.setImageResource(weatherData.icon)
        temperatureFeelLike.text                       = weatherData.feelsLike
        weatherDescription.text                        = weatherData.description
        visibility.text                                = weatherData.visibility
        temperatureMin.text                            = weatherData.tempMin
        temperatureMax.text                            = weatherData.tempMax
        pressure.text                                  = weatherData.pressure
        humidity.text                                  = weatherData.humidity
        temperature.text                               = weatherData.temp
        clouds.text                                    = weatherData.clouds
        wind.text                                      = weatherData.winSpeed
        cityName.text                                  = weatherData.name
        date.text                                      = weatherData.date

    }

    private fun showProgress(show: Boolean = true) {
        progress.visibility = if (show) View.VISIBLE else View.GONE
    }

    companion object {
        const val ARG_CITY_ID = "cityId"
        private const val TAG = "WeatherFragment"
        fun newInstance(cityId: Long): WeatherFragment {
            val fragment = WeatherFragment()
            val args = Bundle()
            args.putLong(ARG_CITY_ID, cityId)
            fragment.arguments = args
            return fragment
        }
    }

}
