 package ntutifm.game.google.ui.camera

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
import ntutifm.game.google.databinding.FragmentCameraBinding
import ntutifm.game.google.entity.adaptor.CameraAdaptor
import ntutifm.game.google.entity.contract.CameraContract
import ntutifm.game.google.global.MyLog

 class CameraFragment:Fragment() {
    private var _binding: FragmentCameraBinding? = null
    private val binding get() = _binding!!
    private var adapter: CameraAdaptor? = null
    private val viewModel: CameraViewModel by lazy {
        ViewModelProvider(this)[CameraViewModel::class.java]
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentCameraBinding.inflate(inflater, container, false)
        return binding.root
    }

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        cameraListInit()
        initObservers()
        if (viewModel.currentState.postsState is CameraContract.CameraState.Idle)
            viewModel.setEvent(CameraContract.Event.OnFetchCameras)
    }
    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    @RequiresApi(Build.VERSION_CODES.O)
    private fun cameraListInit() {
        binding.recycleIncidentView.setHasFixedSize(true)
        binding.recycleIncidentView.layoutManager = LinearLayoutManager(MyActivity().context)
        adapter = CameraAdaptor(listOf(), roadcameraBtnListener)
        binding.recycleIncidentView.adapter = adapter
    }
    private val roadcameraBtnListener = View.OnClickListener() {
        val data = it.tag as Incident
        //切換
    }

    private fun initObservers() {
        viewLifecycleOwner.lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.uiState.collect {
                    when (val state = it.postsState) {
                        is CameraContract.CameraState.Idle -> {
                        }
                        is CameraContract.CameraState.Loading -> {
                        }
                        is CameraContract.CameraState.Success -> {
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
                        is CameraContract.Effect.ShowError -> {
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