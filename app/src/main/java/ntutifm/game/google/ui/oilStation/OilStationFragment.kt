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
import androidx.recyclerview.widget.LinearLayoutManager
import kotlinx.coroutines.launch
import ntutifm.game.google.MyActivity
import ntutifm.game.google.apiClass.Incident
import ntutifm.game.google.databinding.FragmentNotificationBinding
import ntutifm.game.google.entity.adaptor.OilStationAdaptor
import ntutifm.game.google.entity.contract.OilStationContract
import ntutifm.game.google.global.MyLog

class OilStationFragment:Fragment() {
    private var _binding: FragmentNotificationBinding? = null
    private val binding get() = _binding!!
    private var adapter: OilStationAdaptor? = null
    private val viewModel: OilStationViewModel by lazy {
        ViewModelProvider(this)[OilStationViewModel::class.java]
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentNotificationBinding.inflate(inflater, container, false)
        return binding.root
    }

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
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
        binding.recycleIncidentView.setHasFixedSize(true)
        binding.recycleIncidentView.layoutManager = LinearLayoutManager(MyActivity().context)
        adapter = OilStationAdaptor(listOf(), parkingBtnListener)
        binding.recycleIncidentView.adapter = adapter
    }
    private val parkingBtnListener = View.OnClickListener() {
        val data = it.tag as Incident
        //切換
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
                            adapter?.submitList(data)
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
                                MyLog.e(it) }

                        }
                    }
                }
            }
        }
    }
}