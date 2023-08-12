package ntutifm.game.google.ui.search

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import ntutifm.game.google.MyActivity
import ntutifm.game.google.R
import ntutifm.game.google.databinding.FragmentSearchBinding
import ntutifm.game.google.entity.SearchAdaptor
import ntutifm.game.google.entity.SearchData
import ntutifm.game.google.net.ApiCallBack
import ntutifm.game.google.net.ApiManager
import ntutifm.game.google.net.ApiProcessor
import java.util.*
import kotlin.collections.ArrayList

val filteredList: MutableList<SearchData> = mutableListOf()
class SearchFragment : Fragment(), ApiCallBack {

    private var _binding : FragmentSearchBinding? = null
    private val binding get() = _binding!!
    private var recycleView : RecyclerView? = null
    private var itemList =  ArrayList<SearchData>()
    private var adapter : SearchAdaptor? = null
    private val viewModel : SearchViewModel by lazy {
        ViewModelProvider(this)[SearchViewModel::class.java]
    }

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
        adapter = SearchAdaptor(filteredList)
        recycleView?.adapter = adapter
    }

    /** 當文字變化 */
    private val queryTextListener = object : androidx.appcompat.widget.SearchView.OnQueryTextListener {
        override fun onQueryTextSubmit(query: String?): Boolean {
            return false
        }

        override fun onQueryTextChange(newText: String?): Boolean {
            filterList(newText)
            return true
        }
    }

    private fun filterList(newText: String?) {
        if(newText!="" && newText!= null) {
            ApiManager(this, newText).execute(this, ApiProcessor().getCityRoadId)
            adapter?.setFilteredList(filteredList)
        }else{
            filteredList.removeAll(filteredList)
        }
    }

    override fun onSuccess(successData: java.util.ArrayList<String>) {
        TODO("Not yet implemented")
    }

    override fun onError(errorCode: Int, errorData: java.util.ArrayList<String>) {
        TODO("Not yet implemented")
    }

    override fun doInBackground(result: Int, successData: java.util.ArrayList<String>) {
        TODO("Not yet implemented")
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
