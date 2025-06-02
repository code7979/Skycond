package abhiket.skycond.presentation.adapter;

import abhiket.skycond.R;
import abhiket.skycond.databinding.ItemCityWeatherBinding;
import abhiket.skycond.presentation.model.City;
import abhiket.skycond.presentation.model.CityWeatherManage;
import abhiket.skycond.presentation.model.Weather;
import kotlin.collections.CollectionsKt;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.AsyncListDiffer;
import androidx.recyclerview.widget.RecyclerView;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;


public final class ManageCitiesAdapter extends RecyclerView.Adapter<ManageCitiesAdapter.AddedCitiesViewHolder> {
    @NotNull
    private final AsyncListDiffer<CityWeatherManage> asyncListDiffer = new AsyncListDiffer<>(this, new ManageCitiesDiff());
//    @NotNull
//    private final OnItemLongClickedListener onItemLongClickedListener;
//    @NotNull
//    private final OnItemClickedListener onItemClickedListener;

    @NotNull
    private final ManageCitiesAdapterListener listener;

    @NotNull
    private final LayoutInflater layoutInflater;
    @Nullable
    private List<Integer> selectedItems = null;

    private boolean inActionMode = false;

//    public ManageCitiesAdapter(
//            @NotNull Context context,
//            @NotNull OnItemClickedListener onItemClickedListener,
//            @NotNull OnItemLongClickedListener onItemLongClickedListener) {
//
//        this.onItemClickedListener = onItemClickedListener;
//        this.onItemLongClickedListener = onItemLongClickedListener;
//        this.layoutInflater = LayoutInflater.from(context);
//        this.asyncListDiffer.submitList(CollectionsKt.emptyList());
//    }

    public ManageCitiesAdapter(
            @NotNull Context context,
            @NotNull ManageCitiesAdapterListener listener) {

        this.listener = listener;
        this.layoutInflater = LayoutInflater.from(context);
        this.asyncListDiffer.submitList(CollectionsKt.emptyList());
    }

    public void setCityWeathers(@NotNull List<CityWeatherManage> value) {
        asyncListDiffer.submitList(value);
    }

    @NotNull
    public List<CityWeatherManage> getCityWeathers() {
        return asyncListDiffer.getCurrentList();
    }

    public void setCityWeather(int position, @NotNull CityWeatherManage element) {
        List<CityWeatherManage> currentList = asyncListDiffer.getCurrentList();
        currentList.set(position, element);
    }

    @NotNull
    public CityWeatherManage getCityWeather(int position) {
        List<CityWeatherManage> currentList = asyncListDiffer.getCurrentList();
        return currentList.get(position);
    }


    public boolean isSelectedAt(int position) {
        List<CityWeatherManage> currentList = asyncListDiffer.getCurrentList();
        CityWeatherManage element = currentList.get(position);
        return element.isSelected();
    }

    public void setSelectedAt(int position, boolean selected) {
        List<CityWeatherManage> currentList = asyncListDiffer.getCurrentList();
        CityWeatherManage element = currentList.get(position);
        element.setSelected(selected);
    }

    public void addToSelectedList(int position) {
        if (selectedItems != null) {
            selectedItems.add(position);
            listener.onSizeChange(selectedItems.size());
        }
    }

    public void removeFromSelectedList(int position) {
        if (selectedItems != null) {
            selectedItems.remove((Integer) position);
            listener.onSizeChange(selectedItems.size());
        }
    }

    /**************************************[ RECYCLER VIEW METHODS ]*******************************/

    @NotNull
    @Override
    public AddedCitiesViewHolder onCreateViewHolder(@NotNull ViewGroup parent, int viewType) {
        ItemCityWeatherBinding binding = ItemCityWeatherBinding.inflate(layoutInflater, parent, false);
        return new AddedCitiesViewHolder(binding, listener);
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

    /********************************************[ END ]*******************************************/


    /********************************* Method used for action mode ********************************/
    @SuppressLint("NotifyDataSetChanged")
    public void onCreateActionMode() {
        inActionMode = true;
        selectedItems = new ArrayList<>();
        notifyDataSetChanged();
    }

    @SuppressLint("NotifyDataSetChanged")
    public void onDestroyActionMode() {
        inActionMode = false;
        if (selectedItems != null) {
            unSelectAllItems(selectedItems);
            selectedItems.clear();
            selectedItems = null;
        }
        notifyDataSetChanged();
    }

    private void unSelectAllItems(@NonNull List<Integer> selectedItems) {
        List<CityWeatherManage> currentList = asyncListDiffer.getCurrentList();
        for (int index = 0; index < selectedItems.size(); index++) {
            CityWeatherManage cityWeatherManage = currentList.get(selectedItems.get(index));
            cityWeatherManage.setSelected(false);
        }
    }

    @SuppressLint("NotifyDataSetChanged")
    public void onAllSelectCalled() {
        List<Integer> selectedItems = this.selectedItems;
        if (selectedItems == null) return;
        List<CityWeatherManage> currentList = asyncListDiffer.getCurrentList();
        if (selectedItems.size() >= getItemCount()) {
            selectedItems.clear();
            for (int i = 0; i < getItemCount(); i++) {
                CityWeatherManage cityWeather = currentList.get(i);
                cityWeather.setSelected(false);
                notifyItemChanged(i);
            }
        } else {
            for (int i = 0; i < getItemCount(); i++) {
                // If the selected item is already added, then we will continue
                // otherwise, we will add it.
                int indexOfItem = selectedItems.indexOf(i);
                //indexofItem is  -1 if selectedItems list does not contain the element
                if (indexOfItem != -1) continue;
                selectedItems.add(i);
                CityWeatherManage cityWeather = currentList.get(i);
                cityWeather.setSelected(true);
                notifyItemChanged(i);
            }
        }
        listener.onSizeChange(selectedItems.size());
    }

    /*********************************************************************************************/

    @NonNull
    public List<Long> getSelectedCitiesId() {
        if (selectedItems == null) return Collections.emptyList();
        List<Integer> selectedItems = this.selectedItems;
        List<Long> selectedCityWeatherIds = new ArrayList<>();
        for (int i = 0; i < selectedItems.size(); i++) {
            CityWeatherManage cityWeather = getCityWeather(selectedItems.get(i));
            selectedCityWeatherIds.add(cityWeather.getCity().getId());
        }
        return selectedCityWeatherIds;
    }

    public static final class AddedCitiesViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener, View.OnLongClickListener {
        @NotNull
        private final ItemCityWeatherBinding itemCityWeatherBinding;
        @NotNull
        private final OnItemClickedListener clickedListener;

//        @NotNull
//        private final OnItemLongClickedListener onItemLongClickedListener;

        public AddedCitiesViewHolder(
                @NotNull ItemCityWeatherBinding binding,
                @NotNull OnItemClickedListener clickedListener) {

            super(binding.getRoot());
            this.itemCityWeatherBinding = binding;
            this.clickedListener = clickedListener;
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

            int reid = cityWeatherManage.isSelected() ? R.drawable.ic_checkbox : 0;
            binding.ivManageCitiesCheckbox.setImageResource(reid);

        }

        public void showCheckBoxVisibility(int visibility) {
            ImageView checkbox = this.itemCityWeatherBinding.ivManageCitiesCheckbox;
            checkbox.setVisibility(visibility);
        }

        public void onClick(@NotNull View view) {
            this.clickedListener.onItemClicked(view, this.getAbsoluteAdapterPosition());
            view.startAnimation(AnimationUtils.loadAnimation(view.getContext(), R.anim.zoom_in_out));
        }

        @Override
        public boolean onLongClick(View view) {
            return clickedListener.onItemLongClick(view, this.getAbsoluteAdapterPosition());
        }

    }
}
