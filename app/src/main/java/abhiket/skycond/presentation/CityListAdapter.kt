package abhiket.skycond.presentation

import abhiket.skycond.R
import android.view.View
import android.widget.Button
import android.view.ViewGroup
import android.widget.TextView
import android.view.LayoutInflater
import android.annotation.SuppressLint
import abhiket.skycond.data.remote.model.City
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import abhiket.skycond.presentation.CityListAdapter.CityViewHolder


class CityListAdapter(
    private var cities: List<City> = emptyList(),
    private var onAddClicked: (City) -> Unit
) : RecyclerView.Adapter<CityViewHolder>() {

    @SuppressLint("NotifyDataSetChanged")
    fun addLocationList(cities: List<City>) {
        this.cities = cities
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CityViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.city_row, parent, false)
        return CityViewHolder(view, onAddClicked)
    }

    override fun onBindViewHolder(holder: CityViewHolder, position: Int) {
        val city: City = cities[position]
        holder.bind(city)
    }

    override fun getItemCount(): Int = cities.size

    inner class CityViewHolder(
        itemView: View,
        private val onAddClicked: (City) -> Unit
    ) : ViewHolder(itemView) {
        private var currentCity: City? = null
        private val addButton: Button = itemView.findViewById(R.id.row_btn_add_city)
        private val cityName: TextView = itemView.findViewById(R.id.row_tv_city_name)
        private val cityState: TextView = itemView.findViewById(R.id.row_tv_city_state)


        init {
            addButton.setOnClickListener { _ ->
                currentCity?.let {
                    onAddClicked(it)
                }
            }
        }

        fun bind(city: City) {
            currentCity = city
            val detail = buildString {
                append(city.state)
                append(',')
                append(' ')
                append(city.country)
            }
            cityName.text = city.name
            cityState.text = detail
        }
    }
}
