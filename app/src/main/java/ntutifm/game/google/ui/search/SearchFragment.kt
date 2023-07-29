package ntutifm.game.google.ui.search

import android.content.ClipData.Item
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Adapter
import android.widget.SearchView
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.ui.AppBarConfiguration
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import ntutifm.game.google.MainActivity
import ntutifm.game.google.MyActivity
import ntutifm.game.google.R
import ntutifm.game.google.databinding.FragmentSearchBinding
import ntutifm.game.google.entity.SearchAdaptor
import ntutifm.game.google.entity.SearchData
import java.util.*
import kotlin.collections.ArrayList


class SearchFragment : Fragment() {

    private var _binding: FragmentSearchBinding? = null

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!
    private lateinit var recyleview: RecyclerView
    private var itemList =  ArrayList<SearchData>()
    private lateinit var adapter: SearchAdaptor
    private lateinit var searchview: androidx.appcompat.widget.SearchView
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        val galleryViewModel =
            ViewModelProvider(this).get(SearchViewModel::class.java)

        _binding = FragmentSearchBinding.inflate(inflater, container, false)
        val root: View = binding.root
        searchview = binding.searchView
        searchview.clearFocus()
        searchview.setOnQueryTextListener(object : androidx.appcompat.widget.SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                return false
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                filterList(newText)
                return true
            }
        })
        recyleview = binding.recycleView
        recyleview.setHasFixedSize(true)
        recyleview.layoutManager = LinearLayoutManager(MyActivity().context)
        itemList.add(SearchData("Java", R.drawable.rcc))
        itemList.add(SearchData("Java1", R.drawable.rcc))
        itemList.add(SearchData("Java2", R.drawable.rcc))
        itemList.add(SearchData("Java3", R.drawable.rcc))
        adapter = SearchAdaptor(itemList)
        recyleview.adapter = adapter
//        val btn8: ImageView = binding.cancel
//        btn8.setOnClickListener {
//            val fragment2 = HomeFragment()
//            val transaction = getFragmentManager()?.beginTransaction()
//            transaction?.replace(R.id.nav_host_fragment_content_main, fragment2)
//            transaction?.commit()
//        }
        return root
    }

    private fun filterList(newText: String?) {
        val filteredList: List<SearchData> =ArrayList<SearchData>()
        if (newText != null) {
            val filteredList = ArrayList<SearchData>()
            for(i in itemList){
                if (i.title.lowercase(Locale.ROOT).contains(newText)) {
                    filteredList.add(i)
                }
            }

            if (filteredList.isEmpty()) {
                Toast.makeText(MyActivity().context, "No Data found", Toast.LENGTH_SHORT).show()
            } else {
                adapter.setFilteredList(filteredList)
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
