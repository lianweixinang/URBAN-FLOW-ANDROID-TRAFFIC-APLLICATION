package ntutifm.game.google.ui.road

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
import ntutifm.game.google.databinding.FragmentRoadBinding
import ntutifm.game.google.entity.adaptor.RoadAdaptor
import ntutifm.game.google.entity.adaptor.RoadFavoriteAdaptor
import ntutifm.game.google.entity.contract.RoadContract
import ntutifm.game.google.global.MyLog

class RoadFragment:Fragment() {
    private var _binding: FragmentRoadBinding? = null
    private val binding get() = _binding!!
    private var adapter: RoadFavoriteAdaptor? = null
    private val viewModel: RoadViewModel by lazy {
        ViewModelProvider(this)[RoadViewModel::class.java]
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
        binding.recycleIncidentView.setHasFixedSize(true)
        binding.recycleIncidentView.layoutManager = LinearLayoutManager(MyActivity().context)
        adapter = RoadFavoriteAdaptor(listOf(), roadBtnListener)
        binding.recycleIncidentView.adapter = adapter
    }
    private val roadBtnListener = View.OnClickListener() {
        val data = it.tag as Incident
        //切換
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
                        is RoadContract.Effect.ShowError -> {
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