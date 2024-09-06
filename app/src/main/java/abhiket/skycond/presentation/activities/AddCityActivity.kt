package abhiket.skycond.presentation.activities

import abhiket.skycond.R
import abhiket.skycond.data.remote.model.City
import abhiket.skycond.di.Singleton
import abhiket.skycond.presentation.AddedCitiesAdapter
import abhiket.skycond.presentation.CityListAdapter
import abhiket.skycond.presentation.viewmodels.AddViewModel
import abhiket.skycond.uitls.UiState
import android.os.Bundle
import android.view.View
import android.widget.ProgressBar
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.isVisible
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.appbar.MaterialToolbar
import com.google.android.material.search.SearchView

class AddCityActivity : AppCompatActivity() {
    private val viewModel: AddViewModel by viewModels {
        AddViewModel.createFactory(Singleton.getInstance(application).appModule.addRepository)
    }
    private lateinit var toolbar: MaterialToolbar

    private lateinit var cityRecyclerView: RecyclerView
    private lateinit var addedCitiesRecyclerView: RecyclerView

    private lateinit var cityListAdapter: CityListAdapter
    private lateinit var addedCitiesListAdapter: AddedCitiesAdapter
    private lateinit var loading: ProgressBar
    private lateinit var searchView: SearchView

    override fun onCreate(savedInstanceState: Bundle?) {
        setContentView(R.layout.activity_add_city)
        super.onCreate(savedInstanceState)

        toolbar = findViewById(R.id.manage_cities_toolbar)
        setSupportActionBar(toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        toolbar.setTitle(R.string.manage_cities)
        loading = findViewById(R.id.loading)
        searchView = findViewById(R.id.search_view)

        cityRecyclerView = findViewById(R.id.city_recycler_view)
        addedCitiesRecyclerView = findViewById(R.id.manage_cities_recycler_view)

        cityRecyclerView.layoutManager = LinearLayoutManager(this)
        cityRecyclerView.setHasFixedSize(true)
        cityListAdapter = CityListAdapter(onAddClicked = this::onAddClicked)
        cityRecyclerView.adapter = cityListAdapter

        addedCitiesRecyclerView.layoutManager = LinearLayoutManager(this)
        addedCitiesRecyclerView.setHasFixedSize(true)
        addedCitiesListAdapter = AddedCitiesAdapter(onRemoveClicked = this::onRemoveClicked)
        addedCitiesRecyclerView.adapter = addedCitiesListAdapter

        searchView.editText.setOnEditorActionListener { textView, _, _ ->
            val text = textView.text.toString()
            if (text.isNotBlank())
                viewModel.onQuery(text)
            else Toast.makeText(
                applicationContext,
                "Type your location to search",
                Toast.LENGTH_SHORT
            ).show()
            true
        }

        viewModel.cities.observe(this) { citiesList ->
            addedCitiesListAdapter.addCitiesList(citiesList)
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

    private fun onRemoveClicked(cityId: Long) {
        viewModel.removeCityById(cityId)
    }

    private fun onAddClicked(city: City) {
        viewModel.onAddCity(city)
        Toast.makeText(
            applicationContext,
            "Requesting Weather Data For ${city.name}",
            Toast.LENGTH_SHORT
        ).show()
    }
}