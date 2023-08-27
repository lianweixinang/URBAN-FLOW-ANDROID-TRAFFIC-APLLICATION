package ntutifm.game.google.ui.oil

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class OilViewModel : ViewModel() {

    private val _text = MutableLiveData<String>().apply {
        value = "This is oil Fragment"
    }
    val text: LiveData<String> = _text
}