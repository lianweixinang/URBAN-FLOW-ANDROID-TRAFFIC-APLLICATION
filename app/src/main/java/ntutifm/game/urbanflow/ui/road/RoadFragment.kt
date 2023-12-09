package ntutifm.game.urbanflow.ui.road

import android.os.Build
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.RequiresApi
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.NavOptions
import androidx.navigation.Navigation
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.gms.maps.model.LatLng
import kotlinx.coroutines.launch
import ntutifm.game.urbanflow.MyActivity
import ntutifm.game.urbanflow.R
import ntutifm.game.urbanflow.apiClass.RoadFavorite
import ntutifm.game.urbanflow.apiClass.SearchHistory
import ntutifm.game.urbanflow.databinding.FragmentRoadBinding
import ntutifm.game.urbanflow.entity.adaptor.RoadFavoriteAdaptor
import ntutifm.game.urbanflow.entity.contract.RoadContract
import ntutifm.game.urbanflow.global.MyLog
import ntutifm.game.urbanflow.ui.ShareViewModel

class RoadFragment:Fragment() {
    private var _binding: FragmentRoadBinding? = null
    private val binding get() = _binding!!
    private var adapter: RoadFavoriteAdaptor? = null
    private val shareData: ShareViewModel by activityViewModels()
    private val viewModel: RoadViewModel by lazy {
        ViewModelProvider(
            this,
            RoadViewModel.RoadViewModelFactory(requireActivity().application)
        )[RoadViewModel::class.java]
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentRoadBinding.inflate(inflater, container, false)
        return binding.root
    }

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        roadListInit()
        initObservers()
        if (viewModel.currentState.postsState is RoadContract.RoadState.Idle)
            viewModel.setEvent(RoadContract.Event.OnFetchRoads)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    @RequiresApi(Build.VERSION_CODES.O)
    private fun roadListInit() {
        binding.recycleView.setHasFixedSize(true)
        binding.recycleView.layoutManager = LinearLayoutManager(MyActivity().context)
        adapter = RoadFavoriteAdaptor(listOf(), roadBtnListener,roadDeleteListener)
        binding.recycleView.adapter = adapter
    }

    private val roadBtnListener = View.OnClickListener() {
        val data = it.tag as RoadFavorite
        val navController = Navigation.findNavController(binding.root)
        val navOptions = NavOptions.Builder()
            .setPopUpTo(R.id.mapFragment, true)
            .build()
        val bundle = Bundle()
        shareData.roadFavorite.value = SearchHistory(null,data.roadId,data.roadName,null)
        navController.navigate(R.id.mapFragment, bundle, navOptions)
    }
    private val roadDeleteListener = View.OnClickListener() {
        val data = it.tag as RoadFavorite
        viewModel.setEvent(RoadContract.Event.OnDeleteItem(data.roadName))
    }

    private fun initObservers() {
        viewLifecycleOwner.lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.uiState.collect {
                    when (val state = it.postsState) {
                        is RoadContract.RoadState.Idle -> {
                        }
                        is RoadContract.RoadState.Loading -> {
                        }
                        is RoadContract.RoadState.Success -> {
                            val data = state.posts
                            if (state.posts.isEmpty()) {
                                binding.recycleView.visibility = View.GONE
                                binding.nodata.visibility = View.VISIBLE
                            } else {
                                adapter?.submitList(data)
                                binding.nodata.visibility = View.GONE
                            }
                        }
                    }
                }
            }

            viewLifecycleOwner.lifecycleScope.launch {
                repeatOnLifecycle(Lifecycle.State.STARTED) {
                    viewModel.effect.collect { effect ->
                        when (effect) {
                            is RoadContract.Effect.ShowError -> {
                                val msg = effect.message
                                msg?.let {
                                    MyLog.e(it)
                                }

                            }
                        }
                    }
                }
            }
        }
    }
}