package abhiket.skycond.presentation.activities

import abhiket.skycond.R
import abhiket.skycond.data.remote.model.City
import abhiket.skycond.di.Singleton
import abhiket.skycond.presentation.CityListAdapter
import abhiket.skycond.presentation.viewmodels.AddViewModel
import abhiket.skycond.uitls.UiState
import android.os.Bundle
import android.view.View
import android.view.inputmethod.EditorInfo
import android.widget.Button
import android.widget.EditText
import android.widget.ProgressBar
import android.widget.TextView
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.isVisible
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView

class AddCityActivity : AppCompatActivity() {
    private val viewModel: AddViewModel by viewModels {
        AddViewModel.createFactory(Singleton.getInstance(application).appModule.addRepository)
    }

    private lateinit var cityRecyclerView: RecyclerView
    private lateinit var cityListAdapter: CityListAdapter
    private lateinit var loading: ProgressBar
    private lateinit var search: EditText
    private lateinit var searchButton: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        setContentView(R.layout.activity_add_city)
        super.onCreate(savedInstanceState)

        init()

        cityRecyclerView.layoutManager = LinearLayoutManager(this)
        cityRecyclerView.setHasFixedSize(true)
        cityListAdapter = CityListAdapter(onAddClicked = this::onCityItemClicked)
        cityRecyclerView.adapter = cityListAdapter

        searchButton.setOnClickListener {
            val string = search.text.toString()
            if (string.isNotBlank())
                viewModel.onQuery(string)
            else Toast.makeText(
                applicationContext,
                "Type your location to search",
                Toast.LENGTH_SHORT
            ).show()
        }

        viewModel.locations.observe(this) { uiState ->
            when (uiState) {
                is UiState.Failure -> {
                    if (loading.isVisible) loading.visibility = View.GONE
                    val message = getString(uiState.stringRes)
                    Toast.makeText(applicationContext, message, Toast.LENGTH_SHORT).show()
                }

                is UiState.Loading -> {
                    loading.visibility = View.VISIBLE
                }

                is UiState.Success -> {
                    if (loading.isVisible) loading.visibility = View.GONE
                    cityListAdapter.addLocationList(uiState.data)
                }
            }
        }

        viewModel.isAdded.observe(this) { isAdded ->
            when (isAdded) {
                is UiState.Failure -> {
                    Toast.makeText(
                        applicationContext,
                        R.string.failure_added,
                        Toast.LENGTH_SHORT
                    ).show()
                }

                is UiState.Loading -> {}
                is UiState.Success -> {
                    Toast.makeText(
                        applicationContext,
                        R.string.successfully_added,
                        Toast.LENGTH_SHORT
                    ).show()
                }
            }
        }

    }

    private fun init() {
        loading = findViewById(R.id.loading)
        search = findViewById(R.id.ed_search)
        searchButton = findViewById(R.id.btn_search)
        cityRecyclerView = findViewById(R.id.city_recycler_view)
    }

    private fun onCityItemClicked(city: City) {
        viewModel.onAddCity(city)
        Toast.makeText(
            applicationContext,
            "Requesting Weather Data For ${city.name}",
            Toast.LENGTH_SHORT
        ).show()
    }
}