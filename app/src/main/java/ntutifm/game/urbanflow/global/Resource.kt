package ntutifm.game.urbanflow.global

sealed class Resource<out T> {
    class Success<T>(val data: T) : Resource<T>()
    class Error(val exception: Throwable) : Resource<Nothing>()
    object Loading : Resource<Nothing>()
    object Empty : Resource<Nothing>()
}