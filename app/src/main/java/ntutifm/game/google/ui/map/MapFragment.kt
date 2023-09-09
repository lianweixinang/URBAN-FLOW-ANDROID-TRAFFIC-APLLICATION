package ntutifm.game.google.ui.map

import android.Manifest
import android.annotation.SuppressLint
import android.content.pm.PackageManager
import android.location.Location
import android.os.Bundle
import android.os.Looper
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.RelativeLayout
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.core.view.GravityCompat
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.lifecycle.MutableLiveData
import com.google.android.gms.location.*
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.Marker
import com.google.android.gms.maps.model.MarkerOptions
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.bottomsheet.BottomSheetBehavior.BottomSheetCallback
import com.google.maps.android.clustering.ClusterManager
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.MainScope
import kotlinx.coroutines.launch
import ntutifm.game.google.*
import ntutifm.game.google.R
import ntutifm.game.google.databinding.ActivityMainBinding
import ntutifm.game.google.entity.MyItem
import ntutifm.game.google.entity.SyncBottomBar
import ntutifm.game.google.entity.SyncBottomBar.state
import ntutifm.game.google.entity.SyncSpeed
import ntutifm.game.google.global.AppUtil
import ntutifm.game.google.global.MyLog
import ntutifm.game.google.net.*
import ntutifm.game.google.ui.search.SearchFragment
import kotlin.math.roundToInt


var mClusterManager:ClusterManager<MyItem>? = null
var favoriteFlag:MutableLiveData<Boolean>? = null
var behavior:BottomSheetBehavior<View>? = null
class MapFragment : Fragment() , GoogleMap.OnMyLocationButtonClickListener,
    GoogleMap.OnMyLocationClickListener, OnMapReadyCallback,
    ActivityCompat.OnRequestPermissionsResultCallback, ApiCallBack {

    internal var mCurrLocationMarker : Marker? = null
    internal var mLastLocation : Location? = null
    private var _binding : ActivityMainBinding? = null
    private val binding get() = _binding!!
    private var mFusedLocationClient : FusedLocationProviderClient? = null
    private var permissionDenied = false
    private lateinit var map : GoogleMap
    private lateinit var mLocationRequest : LocationRequest
    private var latLng : LatLng? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?,
    ): View {
        _binding = ActivityMainBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val mapFragment =
            childFragmentManager.findFragmentById(R.id.map) as SupportMapFragment?
        mapFragment?.getMapAsync(this)
        favoriteFlag = MutableLiveData(false) //到時候用viewModel給值
        binding.fragmentMap.fragmentHome.searchBtn.setOnClickListener(searchBtnListener)
        binding.fragmentMap.bg.setOnClickListener(backBtnListener)
        binding.fragmentMap.fragmentHome.favoriteBtn.setOnClickListener(favoriteBtnListener)
        bottomSheetInit()
        SyncSpeed.speedLists.observe(viewLifecycleOwner){
            MyLog.e("updateSpeedEnd")
            if(it.isNotEmpty()) {
                MyLog.e("changeSpeed"+it[0].volume+" "+it[0].avgSpeed)
                binding.fragmentMap.cars.text = it[0].volume.toString() + " Cars"
                binding.fragmentMap.speed.text = it[0].avgSpeed.roundToInt().toString() + " km/h"
                if (it.size > 1) {
                    binding.fragmentMap.cars2.text = it[1].volume.toString() + " Cars"
                    binding.fragmentMap.speed2.text = it[1].avgSpeed.roundToInt().toString() + " km/h"
                }
            }else{
                binding.fragmentMap.cars.text = "無資料"
                binding.fragmentMap.speed.text = "無資料"
                binding.fragmentMap.cars2.text = "無資料"
                binding.fragmentMap.speed2.text = "無資料"
            }
            MainScope().launch(Dispatchers.Main){SyncBottomBar.updateState(SyncBottomBar.State.Open)}
        }

//        AppUtil.showTopToast(context, "HI")
//        AppUtil.showDialog("Hello", activity)

        binding.fragmentMap.webView.getSettings().setJavaScriptEnabled(true);
        binding.fragmentMap.webView.loadUrl("https://cctvatis4.ntpc.gov.tw/C000232")

    }
    fun openDrawer(){
        MyLog.d("openDrawer")
        binding.drawerLayout1.openDrawer(GravityCompat.START)
    }
    private fun bottomSheetInit(){
        val bottomSheet: View = binding.fragmentMap.bg
        behavior = BottomSheetBehavior.from(bottomSheet)
        behavior?.state = BottomSheetBehavior.STATE_HALF_EXPANDED
        behavior?.addBottomSheetCallback(object : BottomSheetCallback() {
            override fun onStateChanged(bottomSheet: View, newState: Int) {
                binding.fragmentMap.webView.isVisible = true
                binding.fragmentMap.trafficFlow.isVisible = newState == BottomSheetBehavior.STATE_EXPANDED
                binding.fragmentMap.imageView3.isVisible = newState == BottomSheetBehavior.STATE_EXPANDED
            }

            override fun onSlide(bottomSheet: View, slideOffset: Float) {
                // Do something when the bottom sheet is sliding
            }
        })
        state.observe(viewLifecycleOwner) { state: SyncBottomBar.State? ->
            when (state) {
                is SyncBottomBar.State.Open -> {
                    behavior?.state = BottomSheetBehavior.STATE_EXPANDED
                }
                is SyncBottomBar.State.Close -> {
                    behavior?.state = BottomSheetBehavior.STATE_HALF_EXPANDED
                }
            }
        }
    }

    /** 收藏切換 */
    private val favoriteBtnListener = View.OnClickListener {
        binding.fragmentMap.fragmentHome.favoriteBtn.apply {
            if(favoriteFlag?.value == true){
                this.setImageResource(R.drawable.ic_baseline_star_24)
            }else{
                this.setImageResource(R.drawable.ic_baseline_star_25)
            }
            (favoriteFlag?.value).also { favoriteFlag?.value = it?.not() }
        }

    }

    /** 關閉搜尋欄 */
    private val backBtnListener = View.OnClickListener {
        MyLog.e(isOpen.value.toString())
        if(isOpen.value == true){
            AppUtil.popBackStack(parentFragmentManager)
        }
        isOpen.value = false

    }

    /** 開啟搜尋欄 */
    private val searchBtnListener = View.OnClickListener {
        isOpen.value = true
        MyLog.e(isOpen.value.toString())
        AppUtil.startFragment(parentFragmentManager, R.id.fragmentHome, SearchFragment())
    }

    /** 設置地圖 */
    override fun onMapReady(googleMap: GoogleMap) {
        map = googleMap
        map.isTrafficEnabled = true
        mFusedLocationClient = LocationServices.getFusedLocationProviderClient(requireActivity())
        map.uiSettings.isMyLocationButtonEnabled = true
        map.setOnMyLocationButtonClickListener(this)
        map.setOnMyLocationClickListener(this)
        enableMyLocation()
        moveToCurrentLocation()
        initMark()
        setLocationInitBtn()
    }


    @SuppressLint("UseRequireInsteadOfGet")
    private fun setLocationInitBtn(){
        if (childFragmentManager
                .findFragmentById(R.id.map)!=null && childFragmentManager
                .findFragmentById(R.id.map)!!.view!!.findViewById<View>("1".toInt()) != null) {
            val locationButton : View =
                (childFragmentManager
                    .findFragmentById(R.id.map)!!.view!!.findViewById<View>("1".toInt()).parent as View).findViewById<View>("2".toInt())
            val rlp : RelativeLayout.LayoutParams = locationButton.layoutParams as RelativeLayout.LayoutParams

            rlp.addRule(RelativeLayout.ALIGN_PARENT_TOP, 0)
            rlp.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM, RelativeLayout.TRUE)
            rlp.setMargins(0, 0, 30, 1000)
        }
    }
    private fun drawMarker(map: GoogleMap, location: Location) {
        if (map != null) {
            map.clear()
            val gps = LatLng(location.latitude, location.longitude)
            map.addMarker(MarkerOptions()
                .position(gps)
                .title("Current Position"))
            map.animateCamera(CameraUpdateFactory.newLatLngZoom(gps, 12f))
        }
    }

    @SuppressLint("MissingPermission")
    private fun enableMyLocation() {

        if (activity?.let {
                ContextCompat.checkSelfPermission(
                    it,
                    Manifest.permission.ACCESS_FINE_LOCATION
                )
            } == PackageManager.PERMISSION_GRANTED || activity?.let {
                ContextCompat.checkSelfPermission(
                    it,
                    Manifest.permission.ACCESS_COARSE_LOCATION
                )
            } == PackageManager.PERMISSION_GRANTED
        ) {
            map.isMyLocationEnabled = true
            return
        }

        // 2. If if a permission rationale dialog should be shown
        if (activity?.let {
                ActivityCompat.shouldShowRequestPermissionRationale(
                    it,
                    Manifest.permission.ACCESS_FINE_LOCATION
                )
            } == true || activity?.let {
                ActivityCompat.shouldShowRequestPermissionRationale(
                    it!!,
                    Manifest.permission.ACCESS_COARSE_LOCATION
                )
            } == true
        ) {

            PermissionUtils.RationaleDialog.newInstance(
                MainActivity.LOCATION_PERMISSION_REQUEST_CODE, true
            ).show(childFragmentManager, "dialog")
            return
        }

        // 3. Otherwise, request permission
        activity?.let {
            ActivityCompat.requestPermissions(
                it,
                arrayOf(
                    Manifest.permission.ACCESS_FINE_LOCATION,
                    Manifest.permission.ACCESS_COARSE_LOCATION
                ),
                MainActivity.LOCATION_PERMISSION_REQUEST_CODE
            )
        }
    }
    override fun onPause() {
        super.onPause()
        mFusedLocationClient?.removeLocationUpdates(mLocationCallback)
    }


    private var mLocationCallback: LocationCallback = object : LocationCallback() {
        override fun onLocationResult(locationResult: LocationResult) {
            val locationList = locationResult.locations
            if (locationList.isNotEmpty()) {
                val location = locationList.last()
                mLastLocation = location
                if (mCurrLocationMarker != null) {
                    mCurrLocationMarker?.remove()
                }
                latLng = LatLng(location.latitude, location.longitude)
            }
            map.moveCamera(CameraUpdateFactory.newLatLngZoom(latLng!!, 15f))
        }
    }


    override fun onMyLocationButtonClick(): Boolean {
        moveToCurrentLocation()
        return false
    }

    @SuppressLint("MissingPermission")
    private fun moveToCurrentLocation(){
        mLocationRequest = LocationRequest()
        mLocationRequest.interval = 120000 // two minute interval
        mLocationRequest.fastestInterval = 120000
        mLocationRequest.priority = LocationRequest.PRIORITY_BALANCED_POWER_ACCURACY
        if(!permissionDenied){
            mFusedLocationClient?.requestLocationUpdates(mLocationRequest, mLocationCallback, Looper.myLooper())
        }else{
            enableMyLocation()
        }
        if(latLng != null) {
            map.moveCamera(CameraUpdateFactory.newLatLngZoom(latLng!!, 15f))
        }
    }

    override fun onMyLocationClick(location: Location) {
        Toast.makeText(activity, "Current location:\n$location", Toast.LENGTH_LONG)
            .show()
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<String>,
        grantResults: IntArray,
    ) {
        if (requestCode != MainActivity.LOCATION_PERMISSION_REQUEST_CODE) {
            super.onRequestPermissionsResult(
                requestCode,
                permissions,
                grantResults
            )
            return
        }

        if (PermissionUtils.isPermissionGranted(
                permissions,
                grantResults,
                Manifest.permission.ACCESS_FINE_LOCATION
            ) || PermissionUtils.isPermissionGranted(
                permissions,
                grantResults,
                Manifest.permission.ACCESS_COARSE_LOCATION
            )
        ) {

            enableMyLocation()
        } else {
            // Permission was denied. Display an error message
            // Display the missing permission error dialog when the fragments resume.
            permissionDenied = true
        }
    }
    /**
     * Displays a dialog with error message explaining that the location permission is missing.
     */
    private fun showMissingPermissionError() {
        PermissionUtils.PermissionDeniedDialog.newInstance(true).show(childFragmentManager, "dialog")
    }
    companion object {
        /**
         * Request code for location permission request.
         *
         * @see .onRequestPermissionsResult
         */
        private const val LOCATION_PERMISSION_REQUEST_CODE = 1
    }


    override fun onResume() {
        super.onResume()
        if (permissionDenied) {
            // Permission was not granted, display error dialog.
            showMissingPermissionError()
            permissionDenied = false
        }
    }
    override fun onDestroyView() {
        super.onDestroyView()
        Log.e("GGG","Ggg")
        _binding = null
    }

    //----------------------------------------------批量生成Marker点聚合
    private fun initMark() {
        mClusterManager = ClusterManager(context, map)
        map.setOnCameraIdleListener(mClusterManager)

        ApiManager(this).execute(this, ApiProcessor.getParking)
        mClusterManager?.setOnClusterItemClickListener { item ->
            false
        }
    }

    override fun onSuccess(successData: ArrayList<String>){}

    override fun onError(errorCode: Int, errorData: ArrayList<String>){}

    override fun doInBackground(result: Int, successData: ArrayList<String>){}

}