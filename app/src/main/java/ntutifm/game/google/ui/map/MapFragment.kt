package ntutifm.game.google.ui.map

import android.Manifest
import android.annotation.SuppressLint
import android.content.pm.PackageManager
import android.location.Location
import android.os.Build
import android.os.Bundle
import android.os.Looper
import android.util.Log
import android.view.LayoutInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import android.widget.RelativeLayout
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.core.view.GravityCompat
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.lifecycle.MutableLiveData
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
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
import com.google.android.material.navigation.NavigationView
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
import ntutifm.game.google.entity.SyncRoad
import ntutifm.game.google.entity.SyncSpeed
import ntutifm.game.google.entity.adaptor.SearchAdaptor
import ntutifm.game.google.entity.dbAddHistory
import ntutifm.game.google.entity.dbDeleteHistory
import ntutifm.game.google.entity.dbDisplayHistory
import ntutifm.game.google.global.AppUtil
import ntutifm.game.google.global.MyLog
import ntutifm.game.google.net.*
import ntutifm.game.google.net.ApiClass.CityRoad
import ntutifm.game.google.ui.notification.NotificationFragment
import ntutifm.game.google.ui.oil.OilFragment
import ntutifm.game.google.ui.route.RouteFragment
import ntutifm.game.google.ui.weather.WeatherFragment
import kotlin.math.roundToInt


var mClusterManager:ClusterManager<MyItem>? = null
var favoriteFlag:MutableLiveData<Boolean>? = null
var behavior:BottomSheetBehavior<View>? = null
class MapFragment : Fragment() , GoogleMap.OnMyLocationButtonClickListener,
    GoogleMap.OnMyLocationClickListener, OnMapReadyCallback,
    ActivityCompat.OnRequestPermissionsResultCallback, ApiCallBack,
    NavigationView.OnNavigationItemSelectedListener {

    internal var mCurrLocationMarker : Marker? = null
    internal var mLastLocation : Location? = null
    private var _binding : ActivityMainBinding? = null
    private val binding get() = _binding!!
    private var mFusedLocationClient : FusedLocationProviderClient? = null
    private var permissionDenied = false
    private lateinit var map : GoogleMap
    private lateinit var mLocationRequest : LocationRequest
    private var latLng : LatLng? = null
    private var recycleView : RecyclerView? = null
    private var adaptor : SearchAdaptor? = null
    private var moveState:Boolean = false

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?,
    ): View {
        _binding = ActivityMainBinding.inflate(inflater, container, false)
        return binding.root
    }

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.fragmentMap.cover.visibility = View.GONE
        val mapFragment = childFragmentManager.findFragmentById(R.id.map) as SupportMapFragment?
        mapFragment?.getMapAsync(this)
        favoriteFlag = MutableLiveData(false) //到時候用viewModel給值
        binding.fragmentMap.fragmentHome.searchBtn.setOnClickListener(searchBtnListener)
        binding.fragmentMap.bg.setOnClickListener(backBtnListener)
        binding.fragmentMap.fragmentHome.favoriteBtn.setOnClickListener(favoriteBtnListener)
        binding.fragmentMap.menuButton.setOnClickListener(menuButtonListener)
        binding.fragmentMap.weatherButton.setOnClickListener(weatherButtonListener)
        binding.fragmentMap.fragmentHome.textContainer.setOnClickListener(searchBtnListener)
        binding.fragmentMap.fragmentHome.searchBtn.setOnClickListener(searchBtnListener)
        binding.fragmentMap.fragmentHome.favoriteBtn.setOnClickListener(favoriteBtnListener)
        bottomSheetInit()
        setNavigationViewListener()
        searchViewInit()
        searchListInit()
        webViewInit()
//        AppUtil.showTopToast(context, "HI")
//        AppUtil.showDialog("Hello", activity)
        

    }
    /** 監視器初始化 */
    private fun webViewInit(){
        binding.fragmentMap.webView.getSettings().setJavaScriptEnabled(true);
        binding.fragmentMap.webView.loadUrl("https://cctvatis4.ntpc.gov.tw/C000232")
    }

    /** 底部抽屜初始化 */
    private fun bottomSheetInit(){
        val bottomSheet: View = binding.fragmentMap.bg
        behavior = BottomSheetBehavior.from(bottomSheet)
        behavior?.state = BottomSheetBehavior.STATE_HALF_EXPANDED
        behavior?.addBottomSheetCallback(object : BottomSheetCallback() {
            override fun onStateChanged(bottomSheet: View, newState: Int) {
                if(binding.fragmentMap.fragmentSearch.root.visibility == View.GONE) {
                    binding.fragmentMap.webView.isVisible = true
                    binding.fragmentMap.trafficFlow.isVisible =
                        newState == BottomSheetBehavior.STATE_EXPANDED
                    binding.fragmentMap.imageView3.isVisible =
                        newState == BottomSheetBehavior.STATE_EXPANDED
                }else{
                    binding.fragmentMap.webView.isVisible = false
                    binding.fragmentMap.trafficFlow.isVisible = false
                    binding.fragmentMap.imageView3.isVisible = false
                }
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
    }
    private val menuButtonListener = View.OnClickListener {
        MyLog.d("openDrawer")
        binding.drawerLayout1.openDrawer(GravityCompat.START)
        }
    private val weatherButtonListener = View.OnClickListener {
        AppUtil.startFragment(parentFragmentManager, R.id.fragmentMap, WeatherFragment())
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
            binding.fragmentMap.fragmentSearch.root.visibility = View.GONE
            binding.fragmentMap.fragmentHome.root.visibility = View.VISIBLE
            binding.fragmentMap.webView.visibility = View.VISIBLE
            binding.fragmentMap.carDirection.visibility = View.VISIBLE
            binding.fragmentMap.trafficFlow.visibility = View.VISIBLE
        }
        isOpen.value = false

    }

    /** 開啟搜尋欄 */
    private val searchBtnListener = View.OnClickListener {
        isOpen.value = true
        MyLog.e(isOpen.value.toString())
        binding.fragmentMap.fragmentSearch.root.visibility = View.VISIBLE
        binding.fragmentMap.fragmentHome.root.visibility = View.GONE
        binding.fragmentMap.webView.visibility = View.GONE
        binding.fragmentMap.carDirection.visibility = View.GONE
        binding.fragmentMap.trafficFlow.visibility = View.GONE
        binding.fragmentMap.fragmentSearch.searchView.apply {
            this.requestFocus()
            this.onActionViewExpanded()
        }
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

    /** 初始化搜尋*/
    private fun searchViewInit(){
        binding.fragmentMap.fragmentSearch.searchView.setOnQueryTextListener(queryTextListener)
    }

    /** 初始化搜尋清單 */
    @RequiresApi(Build.VERSION_CODES.O)
    private fun searchListInit(){
        recycleView = binding.fragmentMap.fragmentSearch.recycleView
        recycleView?.setHasFixedSize(true)
        recycleView?.layoutManager = LinearLayoutManager(MyActivity().context)
        adaptor = SearchAdaptor(dbDisplayHistory(requireActivity()), itemOnClickListener, deleteListener)
        recycleView?.adapter = adaptor
        SyncRoad.searchLists.observe(viewLifecycleOwner){
            adaptor?.setFilteredList(it)
        }
    }

    /** 當文字變化 */
    private val queryTextListener = object : androidx.appcompat.widget.SearchView.OnQueryTextListener {
        override fun onQueryTextSubmit(query: String?): Boolean {
            filterList(query)
            return false
        }

        override fun onQueryTextChange(newText: String?): Boolean {
            filterList(newText)
            return false
        }
    }

    /** 文字變化 */
    private fun filterList(newText: String?) {
        MyLog.e("textChange")
        if(newText!= "" && newText!= null) {
            SyncRoad.filterSearch(this, newText, this)
        }else{
            adaptor?.setFilteredList(dbDisplayHistory(requireActivity()))
        }

    }

    /** 搜尋清單點擊 */
    @RequiresApi(Build.VERSION_CODES.O)
    private val itemOnClickListener = View.OnClickListener {
        try {
            val searchData = it.tag as CityRoad
            dbAddHistory(searchData, requireActivity())
            MyLog.d(searchData.roadName)
            MyLog.d(searchData.roadId)
            SyncSpeed.getCityRoadSpeed(this,searchData.roadId,this)
            binding.fragmentMap.fragmentSearch.searchView.setQuery("", false)
            binding.fragmentMap.fragmentSearch.root.visibility = View.GONE
            AppUtil.showTopToast(requireActivity(),"搜尋中...")

        } catch (e: Exception) {
            MyLog.e(e.toString())
        }
    }
    /** 刪除歷史紀錄 */
    private val deleteListener = View.OnClickListener {
        try {
            val searchData = it.tag as CityRoad
            dbDeleteHistory(searchData.roadName, requireActivity())
            adaptor?.setFilteredList(dbDisplayHistory(requireActivity()))
        } catch (e: Exception) {
            MyLog.e(e.toString())
        }
    }

    /** 定位按鈕位置 */
    @SuppressLint("UseRequireInsteadOfGet")
    private fun setLocationInitBtn(){
        if (childFragmentManager
                .findFragmentById(R.id.map)!=null && parentFragmentManager
                .findFragmentById(R.id.map)?.view?.findViewById<View>("1".toInt()) != null) {
            val locationButton : View =
                (childFragmentManager
                    .findFragmentById(R.id.map)?.view?.findViewById<View>("1".toInt())?.parent as View).findViewById<View>("2".toInt())
            val rlp : RelativeLayout.LayoutParams = locationButton.layoutParams as RelativeLayout.LayoutParams

            rlp.addRule(RelativeLayout.ALIGN_PARENT_TOP, 0)
            rlp.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM, RelativeLayout.TRUE)
            rlp.setMargins(0, 0, 30, 1000)
        }
    }

    /** 打點 */
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
    /** 要求定位 */
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
                    it,
                    Manifest.permission.ACCESS_COARSE_LOCATION
                )
            } == true
        ) {

            PermissionUtils.RationaleDialog.newInstance(
                MainActivity.LOCATION_PERMISSION_REQUEST_CODE, true
            ).show(parentFragmentManager, "dialog")
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
    /** 選單 */
    override fun onNavigationItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.nav_map -> {
                AppUtil.startFragment(parentFragmentManager, R.id.fragmentMap, MapFragment())
            }
            R.id.nav_oil -> {
                AppUtil.startFragment(parentFragmentManager, R.id.fragmentMap, OilFragment())
            }
            R.id.nav_weather -> {
                AppUtil.startFragment(parentFragmentManager, R.id.fragmentMap, WeatherFragment())
            }
            R.id.nav_route -> {
                AppUtil.startFragment(parentFragmentManager, R.id.fragmentMap, RouteFragment())
            }
            R.id.nav_notification -> {
                AppUtil.startFragment(parentFragmentManager, R.id.fragmentMap, NotificationFragment())
            }
        }

        binding.drawerLayout1.closeDrawer(GravityCompat.START)
        return true
    }

    private fun setNavigationViewListener() {
        binding.navView.setNavigationItemSelectedListener(this)
    }

    /** 當位置改變 */
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
            if(moveState && latLng != null) {
                map.moveCamera(CameraUpdateFactory.newLatLngZoom(latLng!!, 15f))
            }
        }
    }

    /** 點擊定位 */
    override fun onMyLocationButtonClick(): Boolean {
        moveToCurrentLocation()
        return false
    }

    /** 移動到現在位置 */
    @SuppressLint("MissingPermission")
    private fun moveToCurrentLocation(){
        mLocationRequest = LocationRequest()
        mLocationRequest.interval = 120000 // two minute interval
        mLocationRequest.fastestInterval = 120000
        mLocationRequest.priority = LocationRequest.PRIORITY_BALANCED_POWER_ACCURACY
        if(!permissionDenied){
            moveState = true
            mFusedLocationClient?.requestLocationUpdates(mLocationRequest, mLocationCallback, Looper.myLooper())
        }else{
            enableMyLocation()
        }
    }

    /** 點擊mark動作 */
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
        PermissionUtils.PermissionDeniedDialog.newInstance(true).show(parentFragmentManager, "dialog")
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

    /** 批量生成mark */
    private fun initMark() {
        mClusterManager = ClusterManager(context, map)
        map.setOnCameraIdleListener(mClusterManager)

        ApiManager(this).execute(this, ApiProcessor.getParking)
        mClusterManager?.setOnClusterItemClickListener { item ->
            false
        }
        //要拔除全域改成object
    }

    override fun onSuccess(successData: ArrayList<String>){}

    override fun onError(errorCode: Int, errorData: ArrayList<String>){}

    override fun doInBackground(result: Int, successData: ArrayList<String>){}

}