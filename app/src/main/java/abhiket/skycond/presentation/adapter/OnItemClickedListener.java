package abhiket.skycond.presentation.adapter;

import android.view.View;

import androidx.annotation.NonNull;

import org.jetbrains.annotations.NotNull;

public interface OnItemClickedListener {
    void onItemClicked(@NonNull View view, int position);
    boolean onItemLongClick(@NotNull View view, @NotNull int position);
}
