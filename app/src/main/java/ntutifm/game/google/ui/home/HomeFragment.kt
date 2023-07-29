package ntutifm.game.google.ui.home

import android.R
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import ntutifm.game.google.databinding.FragmentHomeBinding
import ntutifm.game.google.isOpen
import ntutifm.game.google.ui.search.SearchFragment


class HomeFragment : Fragment() {

    private var _binding: FragmentHomeBinding? = null

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        val homeViewModel =
            ViewModelProvider(this).get(HomeViewModel::class.java)
        var idx = 0
        _binding = FragmentHomeBinding.inflate(inflater, container, false)

        val root: View = binding.root
        val search: ImageView = binding.searchBtn
        search.setOnClickListener {
            isOpen.value = true
            Log.e("mmm",isOpen.value.toString())
            val fragment = SearchFragment()
            val transaction = parentFragmentManager?.beginTransaction()
            transaction?.replace(ntutifm.game.google.R.id.fragment_home, fragment)
            transaction?.commit()
        }
        val favorite: ImageView = binding.favoriteBtn
        favorite.setOnClickListener {
            if(idx ==0){
                favorite.setImageResource(R.drawable.star_big_on)
                idx = 1
            }else{
                favorite.setImageResource(R.drawable.star_big_off)
                idx = 0
            }
        }
        return root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}