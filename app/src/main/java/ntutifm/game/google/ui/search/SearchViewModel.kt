package ntutifm.game.google.ui.search

import androidx.annotation.MainThread
import androidx.fragment.app.Fragment
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import ntutifm.game.google.R
import ntutifm.game.google.entity.SearchData
import ntutifm.game.google.global.MyLog
import ntutifm.game.google.net.ApiCallBack
import ntutifm.game.google.net.ApiClass.CityRoad
import ntutifm.game.google.net.ApiManager
import ntutifm.game.google.net.ApiProcessor

class SearchViewModel : ViewModel() {


    private val _searchLists = MutableLiveData<List<CityRoad>>()
    val searchLists: LiveData<List<CityRoad>> = _searchLists
    private val _search = MutableLiveData<String>("xxx")
    val search: LiveData<String> = _search
    fun filterSearch(callBack: ApiCallBack?, data: String, fragment:Fragment){
        ApiManager(callBack, data).execute(fragment, ApiProcessor.getCityRoadId)
    }
    fun clearSearch(){
        _searchLists.postValue(listOf<CityRoad>())
    }

    fun updateSearch(data:List<CityRoad>){
        _searchLists.postValue(data)
    }
    fun update(){
        viewModelScope.launch(Dispatchers.Main) {
            _search.postValue("xxx2")
        }

    }

}