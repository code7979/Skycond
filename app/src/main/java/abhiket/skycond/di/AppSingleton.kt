package abhiket.skycond.di

import abhiket.Database
import abhiket.skycond.R
import abhiket.skycond.data.remote.WeatherApi
import android.content.Context
import androidx.sqlite.db.SupportSQLiteDatabase
import app.cash.sqldelight.driver.android.AndroidSqliteDriver
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import kotlin.concurrent.Volatile

internal object UNINITIALIZED_VALUE

class AppSingleton private constructor(context: Context) {
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

    companion object {
        @Volatile
        private var instance: Any = UNINITIALIZED_VALUE
        fun getInstance(context: Context): AppSingleton {
            var result = instance
            if (result === UNINITIALIZED_VALUE) {
                synchronized(Singleton::class.java) {
                    result = instance
                    if (result === UNINITIALIZED_VALUE) {
                        result = AppSingleton(context)
                        instance = result
                    }
                }
            }
            return result as AppSingleton
        }
    }
}

