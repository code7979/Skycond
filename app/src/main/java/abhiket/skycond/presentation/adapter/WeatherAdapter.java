package abhiket.skycond.presentation.adapter;

import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.animation.ValueAnimator;
import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.view.animation.LinearInterpolator;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

import abhiket.skycond.databinding.ItemWeatherBinding;
import abhiket.skycond.presentation.model.City;
import abhiket.skycond.presentation.model.CityWeatherMain;
import abhiket.skycond.presentation.model.Weather;

public final class WeatherAdapter extends RecyclerView.Adapter<WeatherAdapter.WeatherViewHolder> {
    public static final String TAG = "WeatherAdapter";
    private List<CityWeatherMain> cityWeatherMains = new ArrayList<>();
    private final LayoutInflater layoutInflater;

    public WeatherAdapter(Context context) {
        this.layoutInflater = LayoutInflater.from(context);
    }

    public LayoutInflater getLayoutInflater() {
        return layoutInflater;
    }

    @SuppressLint("NotifyDataSetChanged")
    public void setCityWeathers(List<CityWeatherMain> cityWeatherMains) {
        this.cityWeatherMains = cityWeatherMains;
        notifyDataSetChanged();
    }

    public String getCityName(int position) {
        return cityWeatherMains.get(position).getCity().getName();
    }

    public City getCity(int position) {
        return cityWeatherMains.get(position).getCity();
    }

    public String getFormattedLastUpdate(int position) {
        return cityWeatherMains.get(position).getWeather().getFormattedLastUpdate();
    }

    public void setUpdating(int position, boolean isUpdating) {
        CityWeatherMain cityWeatherMain = cityWeatherMains.get(position);
        cityWeatherMain.setUpdating(isUpdating);
        notifyItemChanged(position);
    }

    public void setUpdating(int position, CityWeatherMain newCityWeatherMain) {
        cityWeatherMains.set(position, newCityWeatherMain);
        notifyItemChanged(position);
    }

    @NonNull
    @Override
    public WeatherViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        ItemWeatherBinding binding = ItemWeatherBinding.inflate(layoutInflater, parent, false);
        return new WeatherViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull WeatherViewHolder holder, int position) {
        CityWeatherMain cityWeatherMain = cityWeatherMains.get(position);
        holder.bind(cityWeatherMain);
        //holder.animateCloud();
    }

    @Override
    public int getItemCount() {
        return cityWeatherMains.size();
    }

    /*************************[WeatherViewHolder]**********************/
    public static class WeatherViewHolder extends RecyclerView.ViewHolder {
        private final ItemWeatherBinding binding;

        public WeatherViewHolder(@NonNull ItemWeatherBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }

        public void bind(CityWeatherMain cityWeatherMain) {
            if (cityWeatherMain.isUpdating()) {
                binding.pbWeatherUpdating.setVisibility(View.VISIBLE);
            } else {
                binding.pbWeatherUpdating.setVisibility(View.GONE);
            }

            Weather weather = cityWeatherMain.getWeather();
            binding.weatherDescription.setText(weather.getMain());
            binding.temperatureCard.tvMainTemp.setText(weather.getCurrentTemperatureInCelsius());
            binding.temperatureCard.tvTempMin.setText(weather.getMinimumTemperatureInCelsius());
            binding.temperatureCard.tvTempMax.setText(weather.getMaximumTemperatureInCelsius());

            binding.detailCard1.tvFeelLike.setText(weather.getFeelsLikeTemperatureInCelsius());
            binding.detailCard1.tvPressure.setText(weather.getPressureInKPa());
            binding.detailCard1.tvHumidity.setText(weather.getHumidity());

            binding.detailCard2.tvVisibility.setText(weather.getVisibility());
            binding.detailCard2.tvClouds.setText(weather.getClouds());
            binding.detailCard2.tvWind.setText(weather.getWindSpeed());
        }

        private void animateCloud(ImageView cloud, float from, float to, long duration) {
            ObjectAnimator translationX = ObjectAnimator.ofFloat(cloud, "translationX", from, to);
            translationX.setDuration(duration);
            translationX.setRepeatCount(ObjectAnimator.INFINITE);
            translationX.setRepeatMode(ObjectAnimator.RESTART);
            translationX.setInterpolator(new LinearInterpolator());
            translationX.start();
        }

        public void shrinkView(View view, int currentHeight, int newHeight) {
            ValueAnimator slideAnimator = ValueAnimator.ofInt(currentHeight, newHeight).setDuration(500);

            /* *
             * We use an update listener which listens to each tick
             * and manually updates the height of the view
             * */

            slideAnimator.addUpdateListener(animation1 -> {
                Integer value = (Integer) animation1.getAnimatedValue();
                view.getLayoutParams().height = value.intValue();
                view.requestLayout();
            });

            /*  We use an animationSet to play the animation  */
            AnimatorSet animationSet = new AnimatorSet();
            animationSet.setInterpolator(new AccelerateDecelerateInterpolator());
            animationSet.play(slideAnimator);
            animationSet.start();
        }
    }

}
