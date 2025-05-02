package abhiket.skycond.presentation;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;

import androidx.coordinatorlayout.widget.CoordinatorLayout;
import androidx.core.view.ViewCompat;

import org.jspecify.annotations.NonNull;
import org.jspecify.annotations.Nullable;

public class HideOnScrollBehavior extends CoordinatorLayout.Behavior<View> {
    public static final float FADE_DISTANCE = 300f;
    private float totalDy = 0f;


    public HideOnScrollBehavior(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    public boolean onStartNestedScroll(@NonNull CoordinatorLayout coordinatorLayout, @NonNull View child, @NonNull View directTargetChild, @NonNull View target, int axes, int type) {
        return axes == ViewCompat.SCROLL_AXIS_VERTICAL;
    }

    @Override
    public void onNestedScroll(@NonNull CoordinatorLayout coordinatorLayout, @NonNull View child, @NonNull View target, int dxConsumed, int dyConsumed, int dxUnconsumed, int dyUnconsumed, int type, int @NonNull [] consumed) {
        //        if (dyConsumed > 0) {
//            // Scrolling up
//            float v = (float) (0.0 - child.getHeight());
//            child.animate().translationY(v).setDuration(200).start();
//        } else if (dyConsumed < 0) {
//            // Scrolling down
//            child.animate().translationY(0f).setDuration(200).start();
//        }
        fadeUpward(child, dyConsumed);
    }

    private void fadeUpward(@androidx.annotation.NonNull View child, int dyConsumed) {
        totalDy += dyConsumed;
        // Clamp the value between 0 and fadeDistance
        float clampedDy = Math.max(0f, Math.min(totalDy, FADE_DISTANCE));
        float progress = clampedDy / FADE_DISTANCE;
        // Apply translation and alpha
        child.setTranslationY(-clampedDy);
        child.setAlpha(1f - progress);
    }


}
