package abhiket.skycond.presentation.adapter;

import android.view.View;

import org.jetbrains.annotations.NotNull;

public interface OnItemLongClickedListener {
    boolean onItemLongClick(@NotNull View view, @NotNull int position);
}
