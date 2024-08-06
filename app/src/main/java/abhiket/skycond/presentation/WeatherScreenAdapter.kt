package abhiket.skycond.presentation

import abhiket.skycond.presentation.fragments.WeatherScreenFragment
import android.annotation.SuppressLint
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.viewpager2.adapter.FragmentStateAdapter

class WeatherScreenAdapter(
    private var citiesId: List<Long> = emptyList(),
    fragmentActivity: FragmentActivity
) : FragmentStateAdapter(fragmentActivity) {

    override fun getItemCount(): Int = citiesId.size

    @SuppressLint("NotifyDataSetChanged")
    fun addNewCitiesIds(citiesId: List<Long>) {
        this.citiesId = citiesId
        notifyDataSetChanged()
    }

    override fun createFragment(position: Int): Fragment {
        val value = citiesId[position]
        return WeatherScreenFragment.newInstance(value)
    }
}