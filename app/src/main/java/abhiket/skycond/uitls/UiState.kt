package abhiket.skycond.uitls

import androidx.annotation.StringRes

sealed interface UiState<out T> {
    data object Loading : UiState<Nothing>
    data class Success<out T>(val data: T) : UiState<T>
    data class Failure(@StringRes val stringRes: Int) : UiState<Nothing>
}