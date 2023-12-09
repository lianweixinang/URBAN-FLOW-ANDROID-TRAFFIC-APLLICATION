package ntutifm.game.urbanflow.ui

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.Polyline
import ntutifm.game.urbanflow.apiClass.CCTV
import ntutifm.game.urbanflow.apiClass.SearchHistory

class ShareViewModel:ViewModel() {
    var roadFavorite = MutableLiveData<SearchHistory>(null)
    var destination = MutableLiveData<LatLng>(null)
    var cctv = MutableLiveData<CCTV>(null)
    var isUiInitialized = MutableLiveData<Boolean>(false)
    var polyline = MutableLiveData<Polyline>(null)
}