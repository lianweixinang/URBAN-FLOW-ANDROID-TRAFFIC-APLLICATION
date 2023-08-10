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

    private var _binding : FragmentHomeBinding? = null
    private val binding get() = _binding!!
    private val viewModel : HomeViewModel by lazy{
        ViewModelProvider(this)[HomeViewModel::class.java]
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        _binding = FragmentHomeBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val search: ImageView = binding.searchBtn
        search.setOnClickListener {
            isOpen.value = true
            Log.e("mmm", isOpen.value.toString())
            val fragment = SearchFragment()
            val transaction = parentFragmentManager?.beginTransaction()
            transaction?.replace(ntutifm.game.google.R.id.fragment_home, fragment)
            transaction?.commit()
        }
        val favorite: ImageView = binding.favoriteBtn
        favorite.setOnClickListener {

        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}