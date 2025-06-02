package abhiket.skycond.presentation.model;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Objects;

public class CityWeatherManage {
    @NotNull
    private final City city;

    @NotNull
    private final Weather weather;

    private boolean isSelected;

    public CityWeatherManage(@NotNull City city, @NotNull Weather weather) {
        this.city = city;
        this.weather = weather;
        isSelected = false;
    }

    public CityWeatherManage(@NotNull City city, @NotNull Weather weather,@NotNull boolean isSelected) {
        this.city = city;
        this.weather = weather;
        this.isSelected = isSelected;
    }

    public boolean isSelected() {
        return isSelected;
    }

    public void setSelected(boolean selected) {
        isSelected = selected;
    }

    public @NotNull City getCity() {
        return city;
    }

    public @NotNull Weather getWeather() {
        return weather;
    }

    @Override
    public boolean equals(@Nullable Object o) {
        if (o == null) return false;
        if (!(o instanceof CityWeatherManage that)) return false;
        return isSelected() == that.isSelected() && Objects.equals(getCity(), that.getCity()) && Objects.equals(getWeather(), that.getWeather());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getCity(), getWeather(), isSelected());
    }
}
