package abhiket.skycond.presentation.utils

import androidx.annotation.AnyRes
import androidx.annotation.DrawableRes
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar

@AnyRes
const val RES_NULL = 0

fun AppCompatActivity.setUpActionBar(
    toolbar: Toolbar,
    @DrawableRes navIconRes: Int = RES_NULL
) {
    setSupportActionBar(toolbar)
    val actionBar = supportActionBar
    if (actionBar != null) {
        actionBar.setDisplayHomeAsUpEnabled(true)
        if (navIconRes != RES_NULL) {
            actionBar.setHomeAsUpIndicator(navIconRes)
        }

    }
}