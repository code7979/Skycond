package abhiket.skycond.presentation

import abhiket.Cities
import abhiket.skycond.R
import abhiket.skycond.uitls.toCelsius
import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.ViewHolder


class AddedCitiesAdapter(
    private var addedCities: List<Cities> = emptyList(),
    private var onRemoveClicked: (Long) -> Unit
) : RecyclerView.Adapter<AddedCitiesAdapter.AddedCitiesViewHolder>() {

    @SuppressLint("NotifyDataSetChanged")
    fun addCitiesList(cities: List<Cities>) {
        this.addedCities = cities
        notifyDataSetChanged()
    }

    inner class AddedCitiesViewHolder(itemView: View, onRemoveClicked: (Long) -> Unit) : ViewHolder(itemView) {
        private var currentCity: Long? = null

        private val textViewMinMax: TextView = itemView.findViewById(R.id.tv_manage_cities_min_max)
        private val textViewMainTemp: TextView = itemView.findViewById(R.id.tv_manage_cities_main_temp)
        private val textViewStateCountry: TextView = itemView.findViewById(R.id.tv_manage_cities_state_country)
        private val textViewCityName: TextView = itemView.findViewById(R.id.tv_manage_cities_name)
        private val removeButton: Button = itemView.findViewById(R.id.btn_manage_cities_remove)


        init {
            removeButton.setOnClickListener { _ ->
                currentCity?.let {
                    onRemoveClicked(it)
                }
            }
        }

        fun bind(city: Cities) {
            currentCity = city.cityId
            val minMax = buildString {
                append(city.tempMin.toCelsius())
                append('/')
                append(' ')
                append(city.tempMax.toCelsius())
            }
            textViewMinMax.text = minMax
            textViewMainTemp.text = city.temp.toCelsius()
            textViewStateCountry.text = city.cityCountry
            textViewCityName.text = city.cityName
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AddedCitiesViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.manage_cities, parent, false)
        return AddedCitiesViewHolder(view, onRemoveClicked)
    }

    override fun onBindViewHolder(holder: AddedCitiesViewHolder, position: Int) {
        val city: Cities = addedCities[position]
        holder.bind(city)
    }

    override fun getItemCount(): Int = addedCities.size


}
