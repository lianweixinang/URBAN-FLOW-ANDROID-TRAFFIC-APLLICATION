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
import androidx.navigation.NavOptions
import androidx.navigation.Navigation
import androidx.recyclerview.widget.LinearLayoutManager
import kotlinx.coroutines.launch
import ntutifm.game.google.MyActivity
import ntutifm.game.google.R
import ntutifm.game.google.apiClass.Camera
import ntutifm.game.google.apiClass.Incident
import ntutifm.game.google.apiClass.Parking
import ntutifm.game.google.dataBase.CameraRepository
import ntutifm.game.google.databinding.FragmentCameraBinding
import ntutifm.game.google.entity.adaptor.CameraAdaptor
import ntutifm.game.google.entity.contract.CameraContract
import ntutifm.game.google.entity.contract.ParkingContract
import ntutifm.game.google.global.MyLog
import ntutifm.game.google.ui.map.MapViewModel

 class CameraFragment:Fragment() {
     private var _binding: FragmentCameraBinding? = null
     private val binding get() = _binding!!
     private var adapter: CameraAdaptor? = null
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
         val bundle = Bundle()
         bundle.putBoolean("notReset", true)
         bundle.putDouble("latitude", data.latitude)
         bundle.putDouble("longitude", data.longitude)
         navController.navigate(R.id.mapFragment, bundle, navOptions)
     }
     private val cameraDeleteListener = View.OnClickListener() {
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