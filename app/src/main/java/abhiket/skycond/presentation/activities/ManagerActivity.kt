package abhiket.skycond.presentation.activities

import abhiket.skycond.R
import abhiket.skycond.databinding.ActivityManagerBinding
import abhiket.skycond.di.Singleton
import abhiket.skycond.presentation.adapter.OnItemClickedListener
import abhiket.skycond.presentation.adapter.SearchedCityAdapter
import abhiket.skycond.presentation.utils.setUpActionBar
import abhiket.skycond.presentation.viewmodels.ManagerViewModel
import abhiket.skycond.presentation.utils.UiState
import android.content.ActivityNotFoundException
import android.content.Intent
import android.os.Bundle
import android.view.KeyEvent
import android.view.View
import android.widget.TextView
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.net.toUri
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import androidx.recyclerview.widget.DefaultItemAnimator
import androidx.recyclerview.widget.LinearLayoutManager
import java.util.Locale

class ManagerActivity : AppCompatActivity(),
    TextView.OnEditorActionListener,
    OnItemClickedListener {

    private lateinit var binding: ActivityManagerBinding

    private val searchedCityAdapter: SearchedCityAdapter by lazy {
        SearchedCityAdapter(this)
    }

    private val viewModel by viewModels<ManagerViewModel> {
        viewModelFactory {
            initializer {
                val singleton = Singleton.getInstance(applicationContext)
                val geocodingRepository = singleton.appModule.geocodingRepository
                val weatherRepository = singleton.appModule.cityWeatherRepository
                ManagerViewModel(geocodingRepository, weatherRepository)
            }
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = ActivityManagerBinding.inflate(layoutInflater)
        setContentView(binding.root)
        ViewCompat.setOnApplyWindowInsetsListener(binding.activityManagerMain) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
        setUpActionBar(binding.tbActivityManager, R.drawable.ic_back_long)
        binding.searchView.editText.setOnEditorActionListener(this)

        val recyclerView = binding.resultsRecyclerView
        recyclerView.layoutManager = LinearLayoutManager(this)
        recyclerView.itemAnimator = DefaultItemAnimator()
        recyclerView.setHasFixedSize(true)
        recyclerView.adapter = searchedCityAdapter

        viewModel.searchedQueryState.observe(this) { state ->
            when (state) {
                is UiState.Loading -> {
                    binding.pbItemLoading.visibility = View.VISIBLE
                }

                is UiState.Success -> {
                    binding.pbItemLoading.visibility = View.GONE
                    val cityUis = state.data
                    searchedCityAdapter.setCityList(cityUis)
                }

                is UiState.Failure -> {
                    binding.pbItemLoading.visibility = View.GONE
                    val errorMsg = state.stringValue.asString(this)
                    Toast.makeText(this, errorMsg, Toast.LENGTH_SHORT).show()
                }
            }
        }
    }

    override fun onEditorAction(textView: TextView, actionId: Int, event: KeyEvent?): Boolean {
        val typedText = textView.text.toString()
        viewModel.onSearchCity(typedText)
        return true
    }

    override fun onItemClicked(view: View, position: Int) {
        when (view.id) {
            R.id.item_btn_location -> {
                val city = searchedCityAdapter.getCity(position)
                showLocationOnMap(city.latitude, city.longitude)
            }

            R.id.item_btn_add_city -> {
                val city = searchedCityAdapter.getCity(position)
                Toast.makeText(this, city.getFormattedCity(), Toast.LENGTH_SHORT).show()
                viewModel.onAddCity(city)
            }
        }
    }

    private fun showLocationOnMap(latitude: Double, longitude: Double) {
        val uriString = String.format(Locale.getDefault(), "geo:%f,%f", latitude, longitude)
        val gmmIntentUri = uriString.toUri()
        val mapIntent = Intent(Intent.ACTION_VIEW, gmmIntentUri)
        mapIntent.setPackage("com.google.android.apps.maps")
        try {
            startActivity(mapIntent)
        } catch (e: ActivityNotFoundException) {
            e.printStackTrace()
        }
    }
}