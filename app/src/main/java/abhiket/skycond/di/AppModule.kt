package abhiket.skycond.di

import abhiket.Database
import abhiket.skycond.R
import abhiket.skycond.data.CityWeatherRepositoryImpl
import abhiket.skycond.data.GeocodingRepositoryImpl
import abhiket.skycond.data.local.CityWeatherLocalDataSource
import abhiket.skycond.data.local.CityWeatherLocalDataSourceImpl
import abhiket.skycond.data.local.GeocodingLocalDataSource
import abhiket.skycond.data.local.GeocodingLocalDataSourceImpl
import abhiket.skycond.data.local.WeatherLocalDataSource
import abhiket.skycond.data.local.WeatherLocalDataSourceImpl
import abhiket.skycond.data.remote.GeocodingApi
import abhiket.skycond.data.remote.GeocodingRemoteDataSource
import abhiket.skycond.data.remote.GeocodingRemoteDataSourceImpl
import abhiket.skycond.data.remote.WeatherApi
import abhiket.skycond.data.remote.WeatherRemoteDataSource
import abhiket.skycond.data.remote.WeatherRemoteDataSourceImpl
import abhiket.skycond.domain.CityWeatherRepository
import abhiket.skycond.domain.GeocodingRepository
import android.content.Context
import androidx.sqlite.db.SupportSQLiteDatabase
import app.cash.sqldelight.driver.android.AndroidSqliteDriver
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

internal class AppModule(context: Context) {
    private val apiKey: String = context.getString(R.string.api_key)

    private val retrofit: Retrofit by lazy {
        Retrofit.Builder()
            .baseUrl(WeatherApi.BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }

    private val weatherApi: WeatherApi by lazy {
        retrofit.create(WeatherApi::class.java)
    }

    private val geocodingApi: GeocodingApi by lazy {
        retrofit.create(GeocodingApi::class.java)
    }

    private val database: Database by lazy {
        Database(
            AndroidSqliteDriver(
                schema = Database.Schema,
                context = context,
                name = "skycond_datastore",
                callback = object : AndroidSqliteDriver.Callback(Database.Schema) {
                    override fun onOpen(db: SupportSQLiteDatabase) {
                        db.setForeignKeyConstraintsEnabled(true)
                    }
                }
            )
        )
    }


    private val geocodingRemoteDataSource: GeocodingRemoteDataSource by lazy {
        GeocodingRemoteDataSourceImpl(geocodingApi, apiKey)
    }

    private val geocodingLocalDataSource: GeocodingLocalDataSource by lazy {
        GeocodingLocalDataSourceImpl(database.cityQueries)
    }

    private val weatherLocalDataSource: WeatherLocalDataSource by lazy {
        WeatherLocalDataSourceImpl(database.weatherQueries)
    }

    private val cityWeatherLocalDataSource: CityWeatherLocalDataSource by lazy {
        CityWeatherLocalDataSourceImpl(database.cityWeatherQueries)
    }

    private val weatherRemoteDataSource: WeatherRemoteDataSource by lazy {
        WeatherRemoteDataSourceImpl(weatherApi, apiKey)
    }

    /*********************************************************************************************
     *                                 PUBLIC METHOD                                             *
     *********************************************************************************************/
    val geocodingRepository: GeocodingRepository by lazy {
        GeocodingRepositoryImpl(geocodingLocalDataSource, geocodingRemoteDataSource)
    }

    val cityWeatherRepository: CityWeatherRepository by lazy {
        CityWeatherRepositoryImpl(
            cityWeatherLocalDataSource,
            weatherLocalDataSource,
            weatherRemoteDataSource
        )
    }

}