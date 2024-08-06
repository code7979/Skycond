package abhiket.skycond.uitls

import abhiket.CityEntity
import abhiket.skycond.data.remote.model.City

fun CityEntity.isSame(city: City): Boolean {
    return this.latitude == city.lat
            && this.longitude == city.lon
            && this.name == city.name
}