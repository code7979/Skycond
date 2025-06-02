package abhiket.skycond.presentation.model;

public final class CityWeather {
    private final City city;
    private final Weather weather;

    private boolean isUpdating;

    public CityWeather(City city, Weather weather, boolean isUpdating) {
        this.city = city;
        this.weather = weather;
        this.isUpdating = isUpdating;
    }

    public CityWeather(City city, Weather weather) {
        this.city = city;
        this.weather = weather;
        this.isUpdating = false;
    }

    public boolean isUpdating() {
        return isUpdating;
    }

    public void setUpdating(boolean updating) {
        isUpdating = updating;
    }

    public City getCity() {
        return city;
    }

    public Weather getWeather() {
        return weather;
    }

}