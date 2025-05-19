package abhiket.skycond.presentation.adapter;

import android.annotation.SuppressLint;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AnimationUtils;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

import abhiket.skycond.R;
import abhiket.skycond.databinding.ItemCityBinding;
import abhiket.skycond.presentation.model.City;

public class SearchedCityAdapter extends RecyclerView.Adapter<SearchedCityAdapter.CityViewHolder> {
    private final OnItemClickedListener itemClickedListener;
    private List<City> cityList = new ArrayList<>();

    public SearchedCityAdapter(OnItemClickedListener itemClickedListener) {
        this.itemClickedListener = itemClickedListener;
    }

    @SuppressLint("NotifyDataSetChanged")
    public void setCityList(List<City> cityList) {
        this.cityList = cityList;
        notifyDataSetChanged();
    }

    public City getCity(int position) {
        return cityList.get(position);
    }


    @NonNull
    @Override
    public CityViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        ItemCityBinding binding = ItemCityBinding.inflate(layoutInflater, parent, false);
        return new CityViewHolder(binding, itemClickedListener);
    }

    @Override
    public void onBindViewHolder(@NonNull CityViewHolder holder, int position) {
        City city = cityList.get(position);
        holder.bind(city);
    }

    @Override
    public int getItemCount() {
        return cityList.size();
    }

    public static class CityViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        private final OnItemClickedListener itemClickedListener;
        private final ItemCityBinding itemCityBinding;

        public CityViewHolder(@NonNull ItemCityBinding binding, @NonNull OnItemClickedListener itemClickedListener) {
            super(binding.getRoot());
            this.itemCityBinding = binding;
            this.itemClickedListener = itemClickedListener;
            this.itemCityBinding.itemBtnAddCity.setOnClickListener(this);
            this.itemCityBinding.itemBtnLocation.setOnClickListener(this);
        }

        public void bind(@NonNull City city) {
            final ItemCityBinding binding = itemCityBinding;
            ConstraintLayout mainLayout = binding.itemCityMain;
            mainLayout.startAnimation(AnimationUtils.loadAnimation(mainLayout.getContext(), R.anim.zoon_in_out));
            binding.itemTvCityName.setText(city.getName());
            binding.itemTvCityState.setText(city.getFormattedStateAndCountry());
        }

        @Override
        public void onClick(View view) {
            itemClickedListener.onItemClicked(view, getAbsoluteAdapterPosition());
        }
    }
}
