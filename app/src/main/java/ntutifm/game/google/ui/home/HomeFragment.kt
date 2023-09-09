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
import ntutifm.game.google.global.AppUtil
import ntutifm.game.google.global.MyLog
import ntutifm.game.google.isOpen
import ntutifm.game.google.ui.map.favoriteFlag
import ntutifm.game.google.ui.search.SearchFragment


class HomeFragment : Fragment() {

    private var _binding : FragmentHomeBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        _binding = FragmentHomeBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.searchBtn.setOnClickListener(searchBtnListener)
        binding.favoriteBtn.setOnClickListener(favoriteBtnListener)
    }

    private val favoriteBtnListener = View.OnClickListener {
        binding.favoriteBtn.apply {
            if(favoriteFlag?.value == true){
                this.setImageResource(ntutifm.game.google.R.drawable.ic_baseline_star_24)
            }else{
                this.setImageResource(ntutifm.game.google.R.drawable.ic_baseline_star_25)
            }
            (favoriteFlag?.value).also { favoriteFlag?.value = it?.not() }
        }

    }

    /** 開啟搜尋欄 */
    private val searchBtnListener = View.OnClickListener {
        isOpen.value = true
        MyLog.e(isOpen.value.toString())
        AppUtil.startFragment(parentFragmentManager, ntutifm.game.google.R.id.fragment_home, SearchFragment())
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}