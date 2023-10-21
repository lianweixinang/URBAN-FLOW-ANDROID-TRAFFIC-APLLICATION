package ntutifm.game.google.ui.oilStation

import android.os.Build
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.RequiresApi
import androidx.fragment.app.Fragment
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.NavOptions
import androidx.navigation.Navigation
import androidx.recyclerview.widget.LinearLayoutManager
import kotlinx.coroutines.launch
import ntutifm.game.google.MyActivity
import ntutifm.game.google.R
import ntutifm.game.google.apiClass.Incident
import ntutifm.game.google.apiClass.OilStation
import ntutifm.game.google.apiClass.Parking
import ntutifm.game.google.databinding.FragmentNotificationBinding
import ntutifm.game.google.databinding.FragmentOilStationBinding
import ntutifm.game.google.entity.adaptor.OilStationAdaptor
import ntutifm.game.google.entity.contract.OilStationContract
import ntutifm.game.google.global.MyLog
import ntutifm.game.google.ui.camera.CameraViewModel

class OilStationFragment:Fragment() {
    private var _binding: FragmentOilStationBinding? = null
    private val binding get() = _binding!!
    private var adapter: OilStationAdaptor? = null
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
        val bundle = Bundle()
        bundle.putBoolean("notReset", true)
        bundle.putDouble("latitude", data.latitude)
        bundle.putDouble("longitude", data.longitude)
        navController.navigate(R.id.mapFragment, bundle, navOptions)
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