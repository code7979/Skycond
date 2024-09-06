package abhiket.skycond.uitls

import androidx.annotation.StringRes

sealed interface ScreenState {
    data object Loading : ScreenState
    data object Success : ScreenState
    data class Failure(@StringRes val stringRes: Int) : ScreenState
}