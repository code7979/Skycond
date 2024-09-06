package abhiket.skycond.presentation.activities

import abhiket.skycond.R
import abhiket.skycond.data.local.LocalDataSourceImpl
import abhiket.skycond.di.Singleton
import abhiket.skycond.presentation.WeatherScreenAdapter
import abhiket.skycond.presentation.viewmodels.MainViewModel
import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuInflater
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.viewpager2.widget.ViewPager2
import com.google.android.material.appbar.MaterialToolbar
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayoutMediator

class WeatherActivity : AppCompatActivity() {
    private lateinit var viewPage2: ViewPager2
    private lateinit var toolbar: MaterialToolbar
    private lateinit var tabLayout: TabLayout

    private val viewModel: MainViewModel by viewModels {
        val appModule = Singleton.getInstance(applicationContext).appModule
        MainViewModel.createFactory(LocalDataSourceImpl(appModule.database.weatherDataQueries))
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        setContentView(R.layout.activity_weather)
        super.onCreate(savedInstanceState)
        // Initialize views
        tabLayout = findViewById(R.id.indicator_tab_layout)
        viewPage2 = findViewById(R.id.pager)
        toolbar = findViewById(R.id.main_activity_toolbar)

        //setup Toolbar
        setSupportActionBar(toolbar)
        supportActionBar?.title = ""

        val pagerAdapter = WeatherScreenAdapter(fragmentActivity = this)
        viewPage2.adapter = pagerAdapter

        TabLayoutMediator(tabLayout, viewPage2) { tab, position -> }.attach()

        viewModel.citiesIds.observe(this) { citiesIds ->
            if (citiesIds.isNullOrEmpty()) {
                gotoAddCityActivity()
            } else {
                pagerAdapter.setData(citiesIds)
            }
        }

    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        val inflater: MenuInflater = menuInflater
        inflater.inflate(R.menu.top_app_bar, menu)
        return true
    }

    override fun onResume() {
        super.onResume()

        toolbar.setNavigationOnClickListener {
            // Handle navigation icon press
            gotoAddCityActivity()
        }

        toolbar.setOnMenuItemClickListener { menuItem ->
            when (menuItem.itemId) {
                R.id.manage_cities -> {
                    val intent = Intent(this, AddCityActivity::class.java)
                    startActivity(intent)
                    true
                }

                else -> false
            }
        }
    }

    private fun gotoAddCityActivity() {
        val intent = Intent(applicationContext, AddCityActivity::class.java)
        startActivity(intent)
    }

//   fun onTitleChange(cityName: String, lastUpdate: String) {
//        toolbar.setTitle(cityName)
//        toolbar.setSubtitle(lastUpdate)
//    }

}