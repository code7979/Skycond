package abhiket.skycond.presentation.activities

import abhiket.skycond.R
import abhiket.skycond.di.Singleton
import abhiket.skycond.presentation.viewmodels.MainViewModel
import abhiket.skycond.presentation.WeatherScreenAdapter
import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.View
import android.widget.Button
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.core.view.isVisible
import androidx.viewpager2.widget.ViewPager2

class MainActivity : AppCompatActivity() {
    private lateinit var viewPage2: ViewPager2
    private lateinit var toolbar: Toolbar
    private lateinit var addCity: Button

    private val viewModel: MainViewModel by viewModels {
        MainViewModel.createFactory(Singleton.getInstance(application).appModule.repository)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        setContentView(R.layout.activity_main)
        super.onCreate(savedInstanceState)
        addCity = findViewById(R.id.add_city)
        viewPage2 = findViewById(R.id.pager)
        toolbar = findViewById<Toolbar>(R.id.main_activity_toolbar).also {
            setSupportActionBar(it)
        }


        val pagerAdapter = WeatherScreenAdapter(fragmentActivity = this)
        viewPage2.adapter = pagerAdapter

        viewModel.citiesIds.observe(this) { citiesIds ->
            if (citiesIds.isNullOrEmpty()) {
                addCity.visibility = View.VISIBLE
            } else {
                if (addCity.isVisible) {
                    addCity.visibility = View.GONE
                }
                pagerAdapter.addNewCitiesIds(citiesIds)
            }
        }

        addCity.setOnClickListener {
            viewModel.addDummyCity()
        }

    }

    override fun onResume() {
        super.onResume()

        toolbar.setNavigationOnClickListener {
            // Handle navigation icon press
            Toast.makeText(applicationContext, "Not Implemented yet", Toast.LENGTH_SHORT).show()
        }

        toolbar.setOnMenuItemClickListener { menuItem ->
            when (menuItem.itemId) {
                R.id.search -> {
                    val intent = Intent(this, AddCityActivity::class.java)
                    startActivity(intent)
                    true
                }

                else -> false
            }
        }
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.top_app_bar, menu)
        return true
    }

}