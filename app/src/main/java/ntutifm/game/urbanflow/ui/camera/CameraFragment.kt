 package ntutifm.game.urbanflow.ui.camera

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
import ntutifm.game.urbanflow.apiClass.Camera
import ntutifm.game.urbanflow.databinding.FragmentCameraBinding
import ntutifm.game.urbanflow.entity.adaptor.CameraAdaptor
import ntutifm.game.urbanflow.entity.contract.CameraContract
import ntutifm.game.urbanflow.global.MyLog
import ntutifm.game.urbanflow.ui.ShareViewModel

 class CameraFragment:Fragment() {
     private var _binding: FragmentCameraBinding? = null
     private val binding get() = _binding!!
     private var adapter: CameraAdaptor? = null
     private val shareData: ShareViewModel by activityViewModels()
     private val viewModel: CameraViewModel by lazy {
         ViewModelProvider(
             this,
             CameraViewModel.CameraViewModelFactory(requireActivity().application)
         )[CameraViewModel::class.java]
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
         binding.recycleView.setHasFixedSize(true)
         binding.recycleView.layoutManager = LinearLayoutManager(MyActivity().context)
         adapter = CameraAdaptor(listOf(), cameraBtnListener,cameraDeleteListener)
         binding.recycleView.adapter = adapter
     }

     private val cameraBtnListener = View.OnClickListener() {
         val data = it.tag as Camera
         val navController = Navigation.findNavController(binding.root)
         val navOptions = NavOptions.Builder()
             .setPopUpTo(R.id.mapFragment, true)
             .build()
         shareData.destination.value = LatLng(data.latitude, data.longitude)
         navController.navigate(R.id.mapFragment, null, navOptions)
     }
     private val cameraDeleteListener = View.OnClickListener {
         val data = it.tag as Camera
         viewModel.setEvent(CameraContract.Event.OnDeleteItem(data.cameraId))
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
                             is CameraContract.Effect.ShowError -> {
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