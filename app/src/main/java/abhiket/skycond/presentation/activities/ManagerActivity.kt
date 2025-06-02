package abhiket.skycond.presentation.activities

import abhiket.skycond.R
import abhiket.skycond.databinding.ActivityManagerBinding
import abhiket.skycond.di.Singleton
import abhiket.skycond.presentation.adapter.ManageCitiesAdapter
import abhiket.skycond.presentation.adapter.ManageCitiesAdapterListener
import abhiket.skycond.presentation.adapter.SearchedCityAdapter
import abhiket.skycond.presentation.utils.UiState
import abhiket.skycond.presentation.utils.setUpActionBar
import abhiket.skycond.presentation.viewmodels.ManagerViewModel
import android.app.Activity
import android.content.ActivityNotFoundException
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.KeyEvent
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.activity.OnBackPressedCallback
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.view.ActionMode
import androidx.core.net.toUri
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import androidx.recyclerview.widget.DefaultItemAnimator
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import java.util.Locale

class ManagerActivity : AppCompatActivity(),
    ActionMode.Callback,
    ManageCitiesAdapterListener,
    TextView.OnEditorActionListener {

    private lateinit var binding: ActivityManagerBinding
    private var actionMode: ActionMode? = null

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

    private val searchedCityAdapter: SearchedCityAdapter by lazy {
        SearchedCityAdapter(this, this)
    }

    private val addedCityAdapter: ManageCitiesAdapter by lazy {
        ManageCitiesAdapter(
            this as Context,
            this as ManageCitiesAdapterListener,
        )
    }

    private val onBackPressedCallback = object : OnBackPressedCallback(true) {
        override fun handleOnBackPressed() {
            val isReload = viewModel.isReload
            val resultIntent = Intent()
            resultIntent.putExtra(ACTION_RESULT_RELOAD, isReload)
            setResult(Activity.RESULT_OK, resultIntent)
            finish()
        }
    }

    /**************************** [ACTIVITY LIFECYCLE METHODS ] ******************************/
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

        onBackPressedDispatcher.addCallback(this, onBackPressedCallback)

        binding.searchView.editText.setOnEditorActionListener(this)

        setUpRecyclerView(
            recyclerView = binding.resultsRecyclerView,
            adapter = searchedCityAdapter
        )

        setUpRecyclerView(
            recyclerView = binding.manageCitiesRecyclerView,
            adapter = addedCityAdapter
        )

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

        viewModel.cityWeatherManageState.observe(this) { state ->
            when (state) {
                is UiState.Loading -> {

                }

                is UiState.Success -> {
                    val cityWeathers = state.data
                    addedCityAdapter.cityWeathers = cityWeathers
                }

                is UiState.Failure -> {

                }
            }
        }

        viewModel.selectedItemCount.observe(this) { count ->
            actionMode?.let {
                it.title = resources.getQuantityString(R.plurals.items_selected, count, count)
            }
        }
    }

    override fun onDestroy() {
        actionMode?.finish()
        super.onDestroy()
    }

    /****************************************************************************************/

    private fun <VH : RecyclerView.ViewHolder> setUpRecyclerView(
        recyclerView: RecyclerView,
        adapter: RecyclerView.Adapter<VH>
    ) {
        recyclerView.layoutManager = LinearLayoutManager(this)
        recyclerView.itemAnimator = DefaultItemAnimator()
        recyclerView.setHasFixedSize(true)
        recyclerView.adapter = adapter
    }

    override fun onEditorAction(textView: TextView, actionId: Int, event: KeyEvent?): Boolean {
        val typedText = textView.text.toString()
        viewModel.onSearchCity(typedText)
        return true
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


    /**************************[ ManageCitiesAdapterListener's Methods ] **********************/

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

            R.id.item_city_weather_manage -> {
                if (actionMode != null) {
                    val imageView = view.findViewById<ImageView>(R.id.iv_manage_cities_checkbox)
                    val cityWeather = addedCityAdapter.getCityWeather(position)
                    if (cityWeather.isSelected) {
                        imageView.setImageResource(0)
                        cityWeather.isSelected = false
                        addedCityAdapter.removeFromSelectedList(position)
                    } else {
                        imageView.setImageResource(R.drawable.ic_checkbox)
                        cityWeather.isSelected = true
                        addedCityAdapter.addToSelectedList(position)
                    }
                }

            }

        }
    }

    override fun onItemLongClick(view: View, position: Int): Boolean {
        return when (view.id) {
            R.id.item_city_weather_manage -> {
                // Called when the user performs a touch & hold on ManageCityItemView.
                if (actionMode != null) {
                    return false
                }
                // Start the CAB using the ActionMode.Callback defined earlier.
                actionMode = startSupportActionMode(this)
                val imageView = view.findViewById<ImageView>(R.id.iv_manage_cities_checkbox)
                val cityWeather = addedCityAdapter.getCityWeather(position)
                if (cityWeather.isSelected) {
                    imageView.setImageResource(0)
                    cityWeather.isSelected = false
                    addedCityAdapter.removeFromSelectedList(position)
                } else {
                    imageView.setImageResource(R.drawable.ic_checkbox)
                    cityWeather.isSelected = true
                    addedCityAdapter.addToSelectedList(position)
                }
                return true
            }

            else -> false

        }
    }

    override fun onSizeChange(size: Int) {
        viewModel.setSelectedItemCount(size)
    }

    /******************************************************************************************/


    /**************************** [ACTION MODE CALLBACK METHOD] ******************************/

    override fun onCreateActionMode(mode: ActionMode, menu: Menu): Boolean {
        addedCityAdapter.onCreateActionMode()
        mode.menuInflater.inflate(R.menu.contextual_munu_manager_city, menu)
        mode.title = getString(R.string.select_item)
        return true
    }

    override fun onPrepareActionMode(mode: ActionMode?, menu: Menu): Boolean {
        return false
    }

    override fun onActionItemClicked(mode: ActionMode, item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.contextual_menu_delete -> {
                val citiesId = addedCityAdapter.selectedCitiesId
                viewModel.onDeleteCities(citiesId)
                mode.finish()
                return true
            }

            R.id.contextual_menu_select_all -> {
                addedCityAdapter.onAllSelectCalled()
                return true
            }

            else -> {
                false
            }
        }
    }

    override fun onDestroyActionMode(mode: ActionMode?) {
        addedCityAdapter.onDestroyActionMode()
        actionMode = null
    }

    /****************************************************************************************/

    companion object {
        const val ACTION_RESULT_RELOAD = "result_reload"
    }
}