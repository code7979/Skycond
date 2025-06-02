package abhiket.skycond.presentation.adapter;

import static abhiket.skycond.presentation.utils.Mapper.GTAG;

import abhiket.skycond.R;
import abhiket.skycond.databinding.ItemCityWeatherBinding;
import abhiket.skycond.presentation.model.City;
import abhiket.skycond.presentation.model.CityWeather;
import abhiket.skycond.presentation.model.Weather;
import kotlin.collections.CollectionsKt;

import android.annotation.SuppressLint;
import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AnimationUtils;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import org.jetbrains.annotations.NotNull;

import java.util.List;


public final class AddedCitiesAdapter extends RecyclerView.Adapter<AddedCitiesAdapter.AddedCitiesViewHolder> {
    @NotNull
    private final Context context;
    @NotNull
    private final OnItemClickedListener onItemClickedListener;
    @NotNull
    private final OnItemLongClickedListener onItemLongClickedListener;

    @NotNull
    private List<CityWeather> cityWeathers;
    @NotNull
    private final LayoutInflater layoutInflater;

    private boolean isActionModeActive = false;

    public AddedCitiesAdapter(
            @NotNull Context context,
            @NotNull OnItemClickedListener onItemClickedListener,
            @NotNull OnItemLongClickedListener onItemLongClickedListener) {

        this.context = context;
        this.onItemClickedListener = onItemClickedListener;
        this.onItemLongClickedListener = onItemLongClickedListener;
        this.cityWeathers = CollectionsKt.emptyList();
        this.layoutInflater = LayoutInflater.from(this.context);

    }

    @NotNull
    public List<CityWeather> getCityWeathers() {
        return this.cityWeathers;
    }

    @SuppressLint({"NotifyDataSetChanged"})
    public void setCityWeathers(@NotNull List<CityWeather> value) {
        this.cityWeathers = value;
        this.notifyDataSetChanged();
    }

    @NotNull
    public CityWeather getCityWeather(int position) {
        return this.cityWeathers.get(position);
    }

    @SuppressLint("NotifyDataSetChanged")
    public void setActionModeActive(boolean actionModeActive) {
        isActionModeActive = actionModeActive;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public AddedCitiesViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        ItemCityWeatherBinding binding = ItemCityWeatherBinding.inflate(layoutInflater, parent, false);
        return new AddedCitiesViewHolder(binding, onItemClickedListener, onItemLongClickedListener);
    }

    @Override
    public void onBindViewHolder(@NonNull AddedCitiesViewHolder holder, int position) {
        CityWeather cityWeather = this.cityWeathers.get(position);
        if (isActionModeActive) {
            holder.setCheckBoxVisibility(isActionModeActive);
        } else {
            holder.bind(cityWeather);
        }
    }

    @Override
    public int getItemCount() {
        return this.cityWeathers.size();
    }

    public static final class AddedCitiesViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener, View.OnLongClickListener {
        @NotNull
        private final ItemCityWeatherBinding itemCityWeatherBinding;
        @NotNull
        private final OnItemClickedListener onItemClickedListener;

        @NotNull
        private final OnItemLongClickedListener onItemLongClickedListener;

        public AddedCitiesViewHolder(
                @NotNull ItemCityWeatherBinding binding,
                @NotNull OnItemClickedListener onItemClickedListener,
                @NotNull OnItemLongClickedListener onItemLongClickedListener) {

            super(binding.getRoot());
            this.itemCityWeatherBinding = binding;
            this.onItemClickedListener = onItemClickedListener;
            this.onItemLongClickedListener = onItemLongClickedListener;
            this.itemCityWeatherBinding.itemCityWeatherMain.setOnClickListener(this);
            this.itemCityWeatherBinding.itemCityWeatherMain.setOnLongClickListener(this);

        }

        public void bind(@NotNull CityWeather cityWeather) {
            ItemCityWeatherBinding binding = this.itemCityWeatherBinding;

            Weather weather = cityWeather.getWeather();
            StringBuilder minMaxTemperature = new StringBuilder();
            minMaxTemperature.append(weather.getMaximumTemperatureInCelsius());
            minMaxTemperature.append(' ');
            minMaxTemperature.append('|');
            minMaxTemperature.append(' ');
            minMaxTemperature.append(weather.getMinimumTemperatureInCelsius());

            City city = cityWeather.getCity();
            StringBuilder stateCountryName = new StringBuilder();
            stateCountryName.append(city.getState());
            stateCountryName.append(',');
            stateCountryName.append(' ');
            stateCountryName.append(city.getCountry());

            binding.tvManageCitiesMinMax.setText(minMaxTemperature);
            binding.tvManageCitiesName.setText(city.getName());
            binding.tvManageCitiesStateCountry.setText(stateCountryName);
            itemCityWeatherBinding.ivManageCitiesCheckbox.setVisibility(View.GONE);

            if (cityWeather.isUpdating()) {
                binding.ivManageCitiesCheckbox.setImageResource(R.drawable.ic_checkbox);
            } else {
                binding.ivManageCitiesCheckbox.setImageResource(0);
            }

        }

        public void onClick(@NotNull View view) {
            this.onItemClickedListener.onItemClicked(view, this.getAbsoluteAdapterPosition());
            view.startAnimation(AnimationUtils.loadAnimation(view.getContext(), R.anim.zoom_in_out));
        }

        @Override
        public boolean onLongClick(View view) {
            return onItemLongClickedListener.onItemLongClick(view, this.getAbsoluteAdapterPosition());
        }

        public void setCheckBoxVisibility(boolean isVisible) {
            if (isVisible) {
                itemCityWeatherBinding.ivManageCitiesCheckbox.setVisibility(View.VISIBLE);
            } else {
                itemCityWeatherBinding.ivManageCitiesCheckbox.setVisibility(View.GONE);
            }

        }
    }
}
