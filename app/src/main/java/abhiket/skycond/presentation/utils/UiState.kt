package abhiket.skycond.presentation.utils

sealed interface UiState<out T> {
    data class Success<out T>(val data: T) : UiState<T>
    data class Failure(val stringValue: StringValue) : UiState<Nothing>
    data object Loading : UiState<Nothing>
}