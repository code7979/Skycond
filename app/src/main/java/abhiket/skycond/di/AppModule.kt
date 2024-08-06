package abhiket.skycond.di

import abhiket.Database
import abhiket.skycond.data.local.LocalDataSourceImpl
import abhiket.skycond.data.remote.RemoteDataSourceImpl
import abhiket.skycond.data.remote.WeatherApi
import abhiket.skycond.domain.AddCityRepository
import abhiket.skycond.domain.Repository
import abhiket.skycond.domain.RepositoryImpl
import android.content.Context
import androidx.sqlite.db.SupportSQLiteDatabase
import app.cash.sqldelight.driver.android.AndroidSqliteDriver
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import abhiket.skycond.R

internal class AppModule(private val context: Context) {
    val apiKey: String = context.getString(R.string.api_key)

    val weatherApi: WeatherApi by lazy {
        Retrofit.Builder().baseUrl(WeatherApi.BASE_URL)
            .addConverterFactory(GsonConverterFactory.create()).build()
            .create(WeatherApi::class.java)
    }

    val database: Database by lazy {
        Database(
            AndroidSqliteDriver(schema = Database.Schema,
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

    val repository: Repository by lazy(LazyThreadSafetyMode.SYNCHRONIZED) {
        RepositoryImpl(
            remoteDataSource = RemoteDataSourceImpl(weatherApi, apiKey),
            localDataSource = LocalDataSourceImpl(database.weatherDataQueries)
        )
    }

    val addRepository: AddCityRepository by lazy(LazyThreadSafetyMode.SYNCHRONIZED) {
        AddCityRepository(
            remoteDataSource = RemoteDataSourceImpl(weatherApi, apiKey),
            localDataSource = LocalDataSourceImpl(database.weatherDataQueries)
        )
    }
}