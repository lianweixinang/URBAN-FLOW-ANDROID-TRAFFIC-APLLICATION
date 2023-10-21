package ntutifm.game.google.ui.parking

import android.os.Build
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
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
import ntutifm.game.google.apiClass.CCTV
import ntutifm.game.google.apiClass.Parking
import ntutifm.game.google.databinding.FragmentCctvBinding
import ntutifm.game.google.entity.adaptor.CCTVAdaptor
import ntutifm.game.google.entity.adaptor.ParkingAdaptor
import ntutifm.game.google.entity.contract.CCTVContract
import ntutifm.game.google.entity.contract.ParkingContract
import ntutifm.game.google.global.MyLog
import ntutifm.game.google.ui.camera.CameraViewModel
import ntutifm.game.google.ui.cctv.CCTVViewModel


class CCTVFragment:Fragment() {
    private var _binding: FragmentCctvBinding? = null
    private val binding get() = _binding!!
    private var adapter: CCTVAdaptor? = null
    private val viewModel: CCTVViewModel by lazy {
        ViewModelProvider(this, CCTVViewModel.CCTVViewModelFactory(requireActivity().application))[CCTVViewModel::class.java]
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentCctvBinding.inflate(inflater, container, false)
        return binding.root
    }

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        cctvListInit()
        initObservers()
        if (viewModel.currentState.postsState is CCTVContract.CCTVState.Idle)
            viewModel.setEvent(CCTVContract.Event.OnFetchCCTVs)
    }
    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    @RequiresApi(Build.VERSION_CODES.O)
    private fun cctvListInit() {
        binding.recycleView.setHasFixedSize(true)
        binding.recycleView.layoutManager = LinearLayoutManager(MyActivity().context)
        adapter = CCTVAdaptor(listOf(), cctvBtnListener, cctvDeleteListener)
        binding.recycleView.adapter = adapter
    }
    private val cctvBtnListener = View.OnClickListener() {
        val data = it.tag as CCTV
        val navController = Navigation.findNavController(binding.root)
        val navOptions = NavOptions.Builder()
            .setPopUpTo(R.id.mapFragment, true)
            .build()
        val bundle = Bundle()
        bundle.putBoolean("notReset", true)
        bundle.putString("cctvName", data.name)
        bundle.putString("cctvUrl", data.url)
        navController.navigate(R.id.mapFragment, bundle, navOptions)
    }
    private val cctvDeleteListener = View.OnClickListener() {
        val data = it.tag as CCTV
        viewModel.setEvent(CCTVContract.Event.OnDeleteItem(data.name))
    }

    private fun initObservers() {
        viewLifecycleOwner.lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.uiState.collect {
                    when (val state = it.postsState) {
                        is CCTVContract.CCTVState.Idle -> {
                        }
                        is CCTVContract.CCTVState.Loading -> {
                        }
                        is CCTVContract.CCTVState.Success -> {
                            val data = state.posts
                            if(state.posts.isEmpty()){
                                binding.recycleView.visibility = View.GONE
                                binding.nodata.visibility = View.VISIBLE
                            }else {
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
                        is CCTVContract.Effect.ShowError -> {
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