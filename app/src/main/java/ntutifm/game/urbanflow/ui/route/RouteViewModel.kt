package ntutifm.game.urbanflow.ui.weather

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class RouteViewModel : ViewModel() {

    private val _text = MutableLiveData<String>().apply {
        value = "This is route Fragment"
    }
    val text: LiveData<String> = _text
}