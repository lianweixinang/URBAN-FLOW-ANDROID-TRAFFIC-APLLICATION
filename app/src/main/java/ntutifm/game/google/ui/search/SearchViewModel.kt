package ntutifm.game.google.ui.search

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import ntutifm.game.google.R
import ntutifm.game.google.entity.SearchData

class SearchViewModel : ViewModel() {

    private val _text = listOf(
        MutableLiveData("新生北路"),
        MutableLiveData("忠孝東路三段"),
        MutableLiveData("長興街"),
        MutableLiveData("敦化南路一段")
    )

    val text: List<LiveData<String>> = _text
}