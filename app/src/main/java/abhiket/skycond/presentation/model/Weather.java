package abhiket.skycond.presentation.model;

import android.icu.text.SimpleDateFormat;

import androidx.annotation.DrawableRes;

import abhiket.skycond.presentation.utils.Mapper;

import android.icu.util.Calendar;
import android.text.format.DateUtils;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Date;
import java.util.Locale;
import java.util.Objects;

public class Weather {
    private final long conditionId;
    private final String main;
    private final String description;
    private final String icon;
    private final double temp;
    private final double feelsLike;
    private final double tempMin;
    private final double tempMax;

    private final long pressure;
    private final long humidity;
    private final long seaLevel;
    private final long groundLevel;

    private final long visibility;
    private final double windSpeed;
    private final long windDeg;
    private final long clouds;

    private final long sunrise;
    private final long sunset;
    private final long timeZone;
    private final long lastUpdate;

    public Weather(long conditionId, String main, String description, String icon, double temp, double feelsLike, double tempMin, double tempMax, long pressure, long humidity, long seaLevel, long groundLevel, long visibility, double windSpeed, long windDeg, long clouds, long sunrise, long sunset, long timeZone, long lastUpdate) {
        this.conditionId = conditionId;
        this.main = main;
        this.description = description;
        this.icon = icon;
        this.temp = temp;
        this.feelsLike = feelsLike;
        this.tempMin = tempMin;
        this.tempMax = tempMax;
        this.pressure = pressure;
        this.humidity = humidity;
        this.seaLevel = seaLevel;
        this.groundLevel = groundLevel;
        this.visibility = visibility;
        this.windSpeed = windSpeed;
        this.windDeg = windDeg;
        this.clouds = clouds;
        this.sunrise = sunrise;
        this.sunset = sunset;
        this.timeZone = timeZone;
        this.lastUpdate = lastUpdate;
    }

    public String getMain() {
        return main;
    }

    public String getDescription() {
        return description;
    }


    public String getCurrentTemperatureInKelvin() {
        return String.format(Locale.getDefault(), "%.2f", temp);
    }

    public String getFeelsLikeTemperatureInKelvin() {
        return String.format(Locale.getDefault(), "%.2f", feelsLike);
    }

    public String getMinimumTemperatureInKelvin() {
        return String.format(Locale.getDefault(), "%.2f", tempMin);
    }

    public String getMaximumTemperatureInKelvin() {
        return String.format(Locale.getDefault(), "%.2f", tempMax);
    }

    public String getCurrentTemperatureInCelsius() {
        return String.format(Locale.getDefault(), "%d°", Math.round(temp - 273.15));
    }

    public String getFeelsLikeTemperatureInCelsius() {
        return String.format(Locale.getDefault(), "%d°", Math.round(feelsLike - 273.15));
    }

    public String getMinimumTemperatureInCelsius() {
        return String.format(Locale.getDefault(), "%d°", Math.round(tempMin - 273.15));
    }

    public String getMaximumTemperatureInCelsius() {
        return String.format(Locale.getDefault(), "%d°", Math.round(tempMax - 273.15));
    }

    public String getHumidity() {
        return String.format(Locale.getDefault(), "%d %%", this.humidity);
    }

    public String getClouds() {
        return String.format(Locale.getDefault(), "%d %%", this.clouds);
    }

    public String getPressureInKPa() {
        if (pressure > 1000) {
            return String.format(Locale.getDefault(), "%d kPa", pressure / 10);
        } else {
            return String.format(Locale.getDefault(), "%d hPa", pressure);
        }
    }

    public String getVisibility() {
        return String.format(Locale.getDefault(), "%d Km", visibility / 1000);
    }

    public String getWindSpeed() {
        return String.format(Locale.getDefault(), "%d Km/h", Math.round(windSpeed * 3.6));
    }

    public String getWindDeg() {
        return String.format(Locale.getDefault(), "%d Deg", this.windDeg);
    }

    public long getLastUpdate() {
        return lastUpdate;
    }

    public String getFormattedLastUpdate() {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("EEE, h:mm a", Locale.getDefault());
        return simpleDateFormat.format(new Date(lastUpdate * 1000));
    }

    public CharSequence getRelativeTimeSpanForUpdate() {
        return DateUtils.getRelativeTimeSpanString(
                lastUpdate * 1000,
                Calendar.getInstance().getTimeInMillis(),
                DateUtils.MINUTE_IN_MILLIS
        );
    }

    @DrawableRes
    public int getIcon() {
        return Mapper.toDrawableRes(icon);
    }

    @Override
    public boolean equals(@Nullable Object o) {
        if (o == null) return false;
        if (!(o instanceof Weather weather)) return false;
        return conditionId == weather.conditionId
                && Double.compare(temp, weather.temp) == 0
                && Double.compare(feelsLike, weather.feelsLike) == 0
                && Double.compare(tempMin, weather.tempMin) == 0
                && Double.compare(tempMax, weather.tempMax) == 0
                && pressure == weather.pressure
                && humidity == weather.humidity
                && seaLevel == weather.seaLevel
                && groundLevel == weather.groundLevel
                && visibility == weather.visibility
                && Double.compare(windSpeed, weather.windSpeed) == 0
                && windDeg == weather.windDeg
                && clouds == weather.clouds
                && sunrise == weather.sunrise
                && sunset == weather.sunset
                && timeZone == weather.timeZone
                && getLastUpdate() == weather.getLastUpdate()
                && main.equals(weather.main)
                && description.equals(weather.description)
                && icon.equals(weather.icon);
    }

    @Override
    public int hashCode() {
        return Objects.hash(conditionId, getMain(), getDescription(), getIcon(), temp, feelsLike, tempMin, tempMax, pressure, getHumidity(), seaLevel, groundLevel, getVisibility(), getWindSpeed(), getWindDeg(), getClouds(), sunrise, sunset, timeZone, getLastUpdate());
    }
}
