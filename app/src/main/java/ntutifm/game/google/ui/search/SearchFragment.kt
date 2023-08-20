package ntutifm.game.google.ui.search

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.MainScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import ntutifm.game.google.MyActivity
import ntutifm.game.google.databinding.FragmentSearchBinding
import ntutifm.game.google.entity.SearchAdaptor
import ntutifm.game.google.entity.SearchData
import ntutifm.game.google.entity.SyncBottomBar
import ntutifm.game.google.entity.SyncRoad
import ntutifm.game.google.entity.SyncSpeed
import ntutifm.game.google.global.AppUtil
import ntutifm.game.google.global.MyLog
import ntutifm.game.google.net.ApiCallBack
import ntutifm.game.google.net.ApiClass.CityRoad
import ntutifm.game.google.net.ApiClass.CitySpeed
import ntutifm.game.google.net.ApiManager
import ntutifm.game.google.net.ApiProcessor
import ntutifm.game.google.ui.map.MapViewModel

class SearchFragment : Fragment(), ApiCallBack {

    private var _binding : FragmentSearchBinding? = null
    private val binding get() = _binding!!
    private var recycleView : RecyclerView? = null
    private var adaptor : SearchAdaptor? = null

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        _binding = FragmentSearchBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        searchViewInit()
        searchListInit()
    }

    private fun searchViewInit(){
        binding.searchView.apply {
            //this.requestFocus()
            this.onActionViewExpanded()
            this.setOnQueryTextListener(queryTextListener)
        }
    }

    private fun searchListInit(){
        recycleView = binding.recycleView
        recycleView?.setHasFixedSize(true)
        recycleView?.layoutManager = LinearLayoutManager(MyActivity().context)
        adaptor = SearchAdaptor(SyncRoad.searchLists.value, itemOnClickListener)
        recycleView?.adapter = adaptor
        SyncRoad.searchLists.observe(viewLifecycleOwner){
            adaptor?.setFilteredList(it)
        }
    }

    /** 當文字變化 */
    private val queryTextListener = object : androidx.appcompat.widget.SearchView.OnQueryTextListener {
        override fun onQueryTextSubmit(query: String?): Boolean {
            filterList(query)
            return false
        }

        override fun onQueryTextChange(newText: String?): Boolean {
            filterList(newText)
            return false
        }
    }

    private fun filterList(newText: String?) {
        if(newText!= null) {
            SyncRoad.filterSearch(this, newText, this)
        }
    }

    private val itemOnClickListener = View.OnClickListener {
        try {
            val searchData = it.tag as CityRoad
            MyLog.d(searchData.roadName)
            SyncSpeed.getCityRoadSpeed(this,searchData.roadId,this)
            binding.searchView.setQuery("", false)
            AppUtil.popBackStack(parentFragmentManager)
            AppUtil.showTopToast(requireActivity(),"搜尋中...")


        } catch (e: Exception) {
            MyLog.e(e.toString())
        }
    }


    override fun onSuccess(successData: java.util.ArrayList<String>) {
        MyLog.e("onSuccess")
    }

    override fun onError(errorCode: Int, errorData: java.util.ArrayList<String>) {
        MyLog.e("onError")
    }

    override fun doInBackground(result: Int, successData: java.util.ArrayList<String>) {
        MyLog.e("doInBackground")
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}