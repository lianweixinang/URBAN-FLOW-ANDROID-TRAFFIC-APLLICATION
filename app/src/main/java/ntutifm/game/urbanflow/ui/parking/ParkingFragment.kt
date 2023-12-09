package ntutifm.game.urbanflow.ui.parking

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
import ntutifm.game.urbanflow.apiClass.Parking
import ntutifm.game.urbanflow.databinding.FragmentParkingBinding
import ntutifm.game.urbanflow.entity.adaptor.ParkingAdaptor
import ntutifm.game.urbanflow.entity.contract.ParkingContract
import ntutifm.game.urbanflow.global.MyLog
import ntutifm.game.urbanflow.ui.ShareViewModel

class ParkingFragment : Fragment() {
    private var _binding: FragmentParkingBinding? = null
    private val binding get() = _binding!!
    private var adapter: ParkingAdaptor? = null
    private val shareData: ShareViewModel by activityViewModels()
    private val viewModel: ParkingViewModel by lazy {
        ViewModelProvider(
            this,
            ParkingViewModel.ParkingViewModelFactory(requireActivity().application)
        )[ParkingViewModel::class.java]
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentParkingBinding.inflate(inflater, container, false)
        return binding.root
    }

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        parkingListInit()
        initObservers()
        if (viewModel.currentState.postsState is ParkingContract.ParkingState.Idle)
            viewModel.setEvent(ParkingContract.Event.OnFetchParkings)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    @RequiresApi(Build.VERSION_CODES.O)
    private fun parkingListInit() {
        binding.recycleView.setHasFixedSize(true)
        binding.recycleView.layoutManager = LinearLayoutManager(MyActivity().context)
        adapter = ParkingAdaptor(listOf(), parkingBtnListener, parkingDeleteListener)
        binding.recycleView.adapter = adapter
    }

    private val parkingBtnListener = View.OnClickListener() {
        val data = it.tag as Parking
        val navController = Navigation.findNavController(binding.root)
        val navOptions = NavOptions.Builder()
            .setPopUpTo(R.id.mapFragment, true)
            .build()
        shareData.destination.value = LatLng(data.latitude, data.longitude)
        navController.navigate(R.id.mapFragment, null, navOptions)
    }
    private val parkingDeleteListener = View.OnClickListener() {
        val data = it.tag as Parking
        viewModel.setEvent(ParkingContract.Event.OnDeleteItem(data.parkingName))
    }

    private fun initObservers() {
        viewLifecycleOwner.lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.uiState.collect {
                    when (val state = it.postsState) {
                        is ParkingContract.ParkingState.Idle -> {
                        }

                        is ParkingContract.ParkingState.Loading -> {
                        }

                        is ParkingContract.ParkingState.Success -> {
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
        }

        viewLifecycleOwner.lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.effect.collect { effect ->
                    when (effect) {
                        is ParkingContract.Effect.ShowError -> {
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