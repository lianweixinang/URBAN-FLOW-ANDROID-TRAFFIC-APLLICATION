package ntutifm.game.urbanflow.ui.oilStation

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
import ntutifm.game.urbanflow.apiClass.Oil
import ntutifm.game.urbanflow.apiClass.OilStation
import ntutifm.game.urbanflow.databinding.FragmentOilStationBinding
import ntutifm.game.urbanflow.entity.adaptor.OilStationAdaptor
import ntutifm.game.urbanflow.entity.contract.OilStationContract
import ntutifm.game.urbanflow.global.MyLog
import ntutifm.game.urbanflow.ui.ShareViewModel

class OilStationFragment:Fragment() {
    private var _binding: FragmentOilStationBinding? = null
    private val binding get() = _binding!!
    private var adapter: OilStationAdaptor? = null
    private val shareData: ShareViewModel by activityViewModels()
    private val viewModel: OilStationViewModel by lazy {
        ViewModelProvider(
            this,
            OilStationViewModel.OilStationViewModelFactory(requireActivity().application)
        )[OilStationViewModel::class.java]
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentOilStationBinding.inflate(inflater, container, false)
        return binding.root
    }

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        parkingListInit()
        initObservers()
        if (viewModel.currentState.postsState is OilStationContract.OilStationState.Idle)
            viewModel.setEvent(OilStationContract.Event.OnFetchOilStations)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    @RequiresApi(Build.VERSION_CODES.O)
    private fun parkingListInit() {
        binding.recycleView.setHasFixedSize(true)
        binding.recycleView.layoutManager = LinearLayoutManager(MyActivity().context)
        adapter = OilStationAdaptor(listOf(), parkingBtnListener, parkingDeleteListener)
        binding.recycleView.adapter = adapter
    }

    private val parkingBtnListener = View.OnClickListener() {
        val data = it.tag as OilStation
        val navController = Navigation.findNavController(binding.root)
        val navOptions = NavOptions.Builder()
            .setPopUpTo(R.id.mapFragment, true)
            .build()
        shareData.destination.value = LatLng(data.latitude, data.longitude)
        navController.navigate(R.id.mapFragment, null, navOptions)
    }

    private val parkingDeleteListener = View.OnClickListener() {
        val data = it.tag as OilStation
        viewModel.setEvent(OilStationContract.Event.OnDeleteItem(data.station))
    }

    private fun initObservers() {
        viewLifecycleOwner.lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.uiState.collect {
                    when (val state = it.postsState) {
                        is OilStationContract.OilStationState.Idle -> {
                        }
                        is OilStationContract.OilStationState.Loading -> {
                        }
                        is OilStationContract.OilStationState.Success -> {
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
                            is OilStationContract.Effect.ShowError -> {
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