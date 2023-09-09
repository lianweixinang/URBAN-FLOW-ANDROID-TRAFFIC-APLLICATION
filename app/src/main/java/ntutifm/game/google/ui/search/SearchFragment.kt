package ntutifm.game.google.ui.search

import android.os.Build
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.RequiresApi
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import ntutifm.game.google.MyActivity
import ntutifm.game.google.databinding.FragmentSearchBinding
import ntutifm.game.google.entity.adaptor.SearchAdaptor
import ntutifm.game.google.entity.SyncRoad
import ntutifm.game.google.entity.SyncSpeed
import ntutifm.game.google.entity.dbAddHistory
import ntutifm.game.google.entity.dbDeleteHistory
import ntutifm.game.google.entity.dbDisplayHistory
import ntutifm.game.google.global.AppUtil
import ntutifm.game.google.global.MyLog
import ntutifm.game.google.net.ApiCallBack
import ntutifm.game.google.net.ApiClass.CityRoad

class SearchFragment : Fragment(), ApiCallBack {

    private var _binding : FragmentSearchBinding? = null
    private val binding get() = _binding!!
    private var recycleView : RecyclerView? = null
    private var adaptor : SearchAdaptor? = null

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        _binding = FragmentSearchBinding.inflate(inflater, container, false)
        return binding.root
    }

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        searchViewInit()
        searchListInit()
    }

    private fun searchViewInit(){
        binding.searchView.apply {
            this.requestFocus()
            this.onActionViewExpanded()
            this.setOnQueryTextListener(queryTextListener)
        }
    }

    @RequiresApi(Build.VERSION_CODES.O)
    private fun searchListInit(){
        recycleView = binding.recycleView
        recycleView?.setHasFixedSize(true)
        recycleView?.layoutManager = LinearLayoutManager(MyActivity().context)
        adaptor = SearchAdaptor(dbDisplayHistory(requireActivity()), itemOnClickListener, deleteListener)
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
        if(newText!= "" && newText!= null) {
            SyncRoad.filterSearch(this, newText, this)
        }else{
            adaptor?.setFilteredList(dbDisplayHistory(requireActivity()))
        }

    }

    @RequiresApi(Build.VERSION_CODES.O)
    private val itemOnClickListener = View.OnClickListener {
        try {
            val searchData = it.tag as CityRoad
            dbAddHistory(searchData, requireActivity())
            MyLog.d(searchData.roadName)
            SyncSpeed.getCityRoadSpeed(this,searchData.roadId,this)
            binding.searchView.setQuery("", false)
            AppUtil.popBackStack(parentFragmentManager)
            AppUtil.showTopToast(requireActivity(),"搜尋中...")


        } catch (e: Exception) {
            MyLog.e(e.toString())
        }
    }
    private val deleteListener = View.OnClickListener {
        try {
            val searchData = it.tag as CityRoad
            dbDeleteHistory(searchData.roadName, requireActivity())
            adaptor?.setFilteredList(dbDisplayHistory(requireActivity()))
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