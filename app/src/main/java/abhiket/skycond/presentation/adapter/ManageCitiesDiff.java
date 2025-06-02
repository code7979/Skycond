package abhiket.skycond.presentation.adapter;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.DiffUtil;

import java.util.Objects;

import abhiket.skycond.presentation.model.CityWeatherManage;

public final class ManageCitiesDiff extends DiffUtil.ItemCallback<CityWeatherManage> {
    @Override
    public boolean areItemsTheSame(@NonNull CityWeatherManage oldItem, @NonNull CityWeatherManage newItem) {
        return newItem.getCity().getId() == oldItem.getCity().getId();
    }

    @Override
    public boolean areContentsTheSame(@NonNull CityWeatherManage oldItem, @NonNull CityWeatherManage newItem) {
        return Objects.equals(oldItem, newItem);
    }
}
