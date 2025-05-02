package abhiket.skycond.presentation.viewmodels;

import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.animation.ValueAnimator;
import android.annotation.SuppressLint;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.view.animation.LinearInterpolator;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

import abhiket.skycond.databinding.FragmentWeatherBinding;

public class WeatherAdapter extends RecyclerView.Adapter<WeatherAdapter.WeatherViewHolder> {
    private List<String> cities = new ArrayList<>();

    @SuppressLint("NotifyDataSetChanged")
    public void setCities(List<String> cities) {
        this.cities = cities;
        notifyDataSetChanged();
    }

    public String getCity(int position) {
        return cities.get(position);
    }

    @NonNull
    @Override
    public WeatherViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        FragmentWeatherBinding binding = FragmentWeatherBinding.inflate(layoutInflater, parent, false);
        return new WeatherViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull WeatherViewHolder holder, int position) {
        String city = cities.get(position);
        holder.bind(city);
        //holder.animateCloud();
    }

    @Override
    public int getItemCount() {
        return cities.size();
    }

    /*************************[WeatherViewHolder]**********************/
    public static class WeatherViewHolder extends RecyclerView.ViewHolder implements
            View.OnScrollChangeListener, View.OnClickListener {
        private final FragmentWeatherBinding binding;

        private boolean isShrank = false;

        private int ogHeaderLayout;
        private LinearLayout headerLayout;

        public WeatherViewHolder(@NonNull FragmentWeatherBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
            headerLayout = binding.headerLayout;

            this.binding.btnHeaderHeightChanger.setOnClickListener(this);

            //binding.scrollView.setOnScrollChangeListener(this);
        }

        public void bind(String city) {
            binding.tbCityName.setTitle(city);
        }

        //        public void animateCloud() {
//            animateCloud(binding.cloud1, -200f,  1000f,  30000L);
//            animateCloud(binding.cloud2, 00f, -300f, 40000L);
//        }
//
        private void animateCloud(ImageView cloud, float from, float to, long duration) {
            ObjectAnimator translationX = ObjectAnimator.ofFloat(cloud, "translationX", from, to);
            translationX.setDuration(duration);
            translationX.setRepeatCount(ObjectAnimator.INFINITE);
            translationX.setRepeatMode(ObjectAnimator.RESTART);
            translationX.setInterpolator(new LinearInterpolator());
            translationX.start();
        }

        @Override
        public void onScrollChange(View v, int scrollX, int scrollY, int oldScrollX, int oldScrollY) {
            // LinearLayout headerLayout = binding.headerLayout;
//            if (scrollY > lastScrollY) {
//                // Scrolling down
//                shrinkView(headerLayout, 1, originalHeaderHeight);
//            } else if (scrollY < lastScrollY) {
//                // Scrolling up
//                shrinkView(headerLayout, originalHeaderHeight, 1);
//            }
//            lastScrollY = scrollY;
        }

        public void shrinkView(View view, int currentHeight, int newHeight) {

            ValueAnimator slideAnimator = ValueAnimator.ofInt(currentHeight, newHeight).setDuration(500);
            /* We use an update listener which listens to each tick
             * and manually updates the height of the view  */

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

        @Override
        public void onClick(View v) {
            if (!isShrank) {
                isShrank = true;
                ogHeaderLayout = headerLayout.getHeight();
                shrinkView(headerLayout, ogHeaderLayout, 1);
                Toast.makeText(v.getContext(), "Shrinking " + ogHeaderLayout, Toast.LENGTH_SHORT).show();
            } else {
                isShrank = false;
                Toast.makeText(v.getContext(), "Expanding " + ogHeaderLayout, Toast.LENGTH_SHORT).show();
                shrinkView(headerLayout, headerLayout.getHeight(), ogHeaderLayout);
            }

        }
    }

}
