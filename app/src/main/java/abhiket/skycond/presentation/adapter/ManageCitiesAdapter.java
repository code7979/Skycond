package abhiket.skycond.presentation.adapter;

import static abhiket.skycond.presentation.utils.Mapper.GTAG;

import abhiket.skycond.R;
import abhiket.skycond.databinding.ItemCityWeatherBinding;
import abhiket.skycond.presentation.model.City;
import abhiket.skycond.presentation.model.CityWeatherManage;
import abhiket.skycond.presentation.model.Weather;
import kotlin.collections.CollectionsKt;

import android.annotation.SuppressLint;
import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;

import androidx.recyclerview.widget.AsyncListDiffer;
import androidx.recyclerview.widget.RecyclerView;

import org.jetbrains.annotations.NotNull;

import java.util.List;


public final class ManageCitiesAdapter extends RecyclerView.Adapter<ManageCitiesAdapter.AddedCitiesViewHolder> {
    @NotNull
    private final AsyncListDiffer<CityWeatherManage> asyncListDiffer = new AsyncListDiffer<>(this, new ManageCitiesDiff());

    @NotNull
    private final OnItemLongClickedListener onItemLongClickedListener;
    @NotNull
    private final OnItemClickedListener onItemClickedListener;
    @NotNull
    private final LayoutInflater layoutInflater;

    private boolean inActionMode = false;

    public ManageCitiesAdapter(
            @NotNull Context context,
            @NotNull OnItemClickedListener onItemClickedListener,
            @NotNull OnItemLongClickedListener onItemLongClickedListener) {

        this.onItemClickedListener = onItemClickedListener;
        this.onItemLongClickedListener = onItemLongClickedListener;
        this.layoutInflater = LayoutInflater.from(context);
        this.asyncListDiffer.submitList(CollectionsKt.emptyList());
    }

    @NotNull
    public List<CityWeatherManage> getCityWeathers() {
        return asyncListDiffer.getCurrentList();
    }

    public void setCityWeathers(@NotNull List<CityWeatherManage> value) {
        asyncListDiffer.submitList(value);
    }

    @NotNull
    public CityWeatherManage getCityWeather(int position) {
        List<CityWeatherManage> currentList = asyncListDiffer.getCurrentList();
        return currentList.get(position);
    }

    @SuppressLint("NotifyDataSetChanged")
    public void setInActionMode(boolean inActionMode, int skipAtPosition) {
        this.inActionMode = inActionMode;
        for (int i = getItemCount() - 1; i >= 0; i--) {
            if (i == skipAtPosition) continue;
            notifyItemChanged(i);
        }
    }

    @NotNull
    @Override
    public AddedCitiesViewHolder onCreateViewHolder(@NotNull ViewGroup parent, int viewType) {
        ItemCityWeatherBinding binding = ItemCityWeatherBinding.inflate(layoutInflater, parent, false);
        return new AddedCitiesViewHolder(binding, onItemClickedListener, onItemLongClickedListener);
    }

    @Override
    public void onBindViewHolder(@NotNull AddedCitiesViewHolder holder, int position) {
        List<CityWeatherManage> currentList = asyncListDiffer.getCurrentList();
        CityWeatherManage cityWeatherManage = currentList.get(position);
        holder.bind(cityWeatherManage);
        holder.showCheckBoxVisibility(inActionMode ? View.VISIBLE : View.GONE);
    }

    @Override
    public int getItemCount() {
        return asyncListDiffer.getCurrentList().size();
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
            this.itemCityWeatherBinding.itemCityWeatherManage.setOnClickListener(this);
            this.itemCityWeatherBinding.itemCityWeatherManage.setOnLongClickListener(this);

        }

        public void bind(@NotNull CityWeatherManage cityWeatherManage) {
            ItemCityWeatherBinding binding = this.itemCityWeatherBinding;

            Weather weather = cityWeatherManage.getWeather();
            StringBuilder minMaxTemperature = new StringBuilder();
            minMaxTemperature.append(weather.getMaximumTemperatureInCelsius());
            minMaxTemperature.append(' ');
            minMaxTemperature.append('|');
            minMaxTemperature.append(' ');
            minMaxTemperature.append(weather.getMinimumTemperatureInCelsius());

            City city = cityWeatherManage.getCity();
            StringBuilder stateCountryName = new StringBuilder();
            stateCountryName.append(city.getState());
            stateCountryName.append(',');
            stateCountryName.append(' ');
            stateCountryName.append(city.getCountry());

            binding.tvManageCitiesMinMax.setText(minMaxTemperature);
            binding.tvManageCitiesName.setText(city.getName());
            binding.tvManageCitiesStateCountry.setText(stateCountryName);
        }

        public void showCheckBoxVisibility(int visibility) {
            ImageView checkbox = this.itemCityWeatherBinding.ivManageCitiesCheckbox;
            checkbox.setVisibility(visibility);
        }

        public void onClick(@NotNull View view) {
            this.onItemClickedListener.onItemClicked(view, this.getAbsoluteAdapterPosition());
            view.startAnimation(AnimationUtils.loadAnimation(view.getContext(), R.anim.zoom_in_out));
        }

        @Override
        public boolean onLongClick(View view) {
            view.startAnimation(AnimationUtils.loadAnimation(view.getContext(), R.anim.zoom_in_out));
            return onItemLongClickedListener.onItemLongClick(view, this.getAbsoluteAdapterPosition());
        }

    }
}
