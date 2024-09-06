package abhiket.skycond.presentation

import abhiket.skycond.presentation.fragments.WeatherFragment
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListUpdateCallback
import androidx.viewpager2.adapter.FragmentStateAdapter

class WeatherScreenAdapter(
    fragmentActivity: FragmentActivity
) : FragmentStateAdapter(fragmentActivity) {

    private var oldCityIdList: List<Long> = emptyList()

    override fun getItemCount(): Int = oldCityIdList.size


    fun setData(newCityIdList: List<Long>) {
        val diffUtil = DiffUtilLong(oldCityIdList, newCityIdList)
        val diffResult = DiffUtil.calculateDiff(diffUtil)
        oldCityIdList = newCityIdList
        diffResult.dispatchUpdatesTo(this)

    }

    override fun createFragment(position: Int): Fragment {
        val value = oldCityIdList[position]
        return WeatherFragment.newInstance(value)
    }

}