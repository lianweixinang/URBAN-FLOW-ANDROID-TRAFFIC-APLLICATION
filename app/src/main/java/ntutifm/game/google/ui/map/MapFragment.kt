package ntutifm.game.google.ui.map

import android.Manifest
import android.annotation.SuppressLint
import android.content.pm.PackageManager
import android.location.Location
import android.os.Build
import android.os.Bundle
import android.os.Looper
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
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.gms.location.*
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.bottomsheet.BottomSheetBehavior.BottomSheetCallback
import com.google.android.material.navigation.NavigationView
import com.google.maps.android.clustering.ClusterManager
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import ntutifm.game.google.*
import ntutifm.game.google.R
import ntutifm.game.google.apiClass.Incident
import ntutifm.game.google.apiClass.SearchHistory
import ntutifm.game.google.databinding.ActivityMainBinding
import ntutifm.game.google.entity.*
import ntutifm.game.google.entity.adaptor.RoadAdaptor
import ntutifm.game.google.entity.adaptor.SearchAdaptor
import ntutifm.game.google.entity.adaptor.SpnOnItemSelected
import ntutifm.game.google.entity.mark.MyItem
import ntutifm.game.google.entity.sync.SyncCamera
import ntutifm.game.google.entity.sync.SyncIncident
import ntutifm.game.google.entity.sync.SyncPosition
import ntutifm.game.google.entity.sync.SyncRoad
import ntutifm.game.google.entity.sync.SyncSpeed
import ntutifm.game.google.entity.sync.SyncWeather
import ntutifm.game.google.global.AppUtil
import ntutifm.game.google.global.MyLog
import ntutifm.game.google.net.*
import ntutifm.game.google.ui.notification.NotificationFragment
import ntutifm.game.google.ui.oil.OilFragment
import ntutifm.game.google.ui.route.RouteFragment
import ntutifm.game.google.ui.weather.WeatherFragment
import kotlin.math.atan2
import kotlin.math.cos
import kotlin.math.pow
import kotlin.math.roundToInt
import kotlin.math.sin
import kotlin.math.sqrt


class MapFragment : Fragment(), GoogleMap.OnMyLocationButtonClickListener,
    GoogleMap.OnMyLocationClickListener, OnMapReadyCallback,
    ActivityCompat.OnRequestPermissionsResultCallback, ApiCallBack,
    NavigationView.OnNavigationItemSelectedListener {
    private var _binding: ActivityMainBinding? = null
    private val binding get() = _binding!!
    private val viewModel: MapViewModel by lazy {
        ViewModelProvider(
            this,
            MapViewModel.MapViewModelFactory(requireActivity().application)
        )[MapViewModel::class.java]
    }

    private var mClusterManager: ClusterManager<MyItem>? = null
    private var behavior: BottomSheetBehavior<View>? = null
    private lateinit var map: GoogleMap

    private var isOpen: Boolean = false
    private var sitMode: Boolean = false
    private var favoriteFlag: Boolean = false
    private var weatherState: Boolean = true
    private var permissionDenied: Boolean = false

    private var searchData: SearchHistory? = null
    private var oldIncident: List<Incident>? = null
    private var mFusedLocationClient: FusedLocationProviderClient? = null
    private var lastLatLng: LatLng? = null

    private var recycleView: RecyclerView? = null
    private var adaptor: SearchAdaptor? = null
    private var adapter: RoadAdaptor? = null


    /** 根據layout建構畫面 */
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?,
    ): View {
        _binding = ActivityMainBinding.inflate(inflater, container, false)
        return binding.root
    }

    /** 邏輯功能初始化 */
    @RequiresApi(Build.VERSION_CODES.O)
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val mapFragment = childFragmentManager.findFragmentById(R.id.map) as SupportMapFragment?
        mapFragment?.getMapAsync(this)

        binding.fragmentMap.cover.visibility = View.GONE
        binding.fragmentMap.menuButton.setOnClickListener(menuButtonListener)

        titleInit()
        cameraInit()
        favoriteInit()
        bottomSheetInit()
        setNavigationViewListener()
        searchViewInit()
        searchListInit()
        webViewInit()
        incidentCheck()

    }

    override fun onResume() {
        super.onResume()
        if (permissionDenied) {
            showMissingPermissionError()
            permissionDenied = false
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    /** 標題初始化 */
    private fun titleInit() {
        binding.fragmentMap.fragmentHome.searchBtn.setOnClickListener(searchBtnListener)
        binding.fragmentMap.fragmentHome.textContainer.setOnClickListener(searchBtnListener)
        binding.fragmentMap.fragmentHome.searchBtn.setOnClickListener(searchBtnListener)
    }

    /** 天氣初始化 */
    private fun weatherInit() {
        binding.fragmentMap.weatherButton.setOnClickListener(weatherButtonListener)
        SyncPosition.weatherLocation.observe(viewLifecycleOwner) {
            SyncWeather.weatherDataApi(this, this)
        }
        SyncWeather.weatherLists.observe(viewLifecycleOwner) {
            when (it[SyncPosition.districtToIndex()].weatherDescription) {
                "晴天" -> binding.fragmentMap.weatherButton.setImageResource(R.drawable.sun)
                "雨天" -> binding.fragmentMap.weatherButton.setImageResource(R.drawable.heavy_rain)
                //還缺其他型態
            }
        }
    }

    /** 更新錄像清單 */
    private fun updateAdapterData(dataList: List<CCTV>) {
        MyLog.e("updateAdapterData")
        for (i in dataList) {
            MyLog.e("updateAdapterData" + i.name)
        }
        adapter?.clear()
        adapter?.addAll(dataList)
        adapter?.notifyDataSetChanged()
    }

    /** 監視器初始化 */
    @SuppressLint("SetJavaScriptEnabled")
    private fun webViewInit() {
        adapter = RoadAdaptor(
            requireActivity(),
            android.R.layout.simple_spinner_item
        )
        adapter?.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        binding.fragmentMap.spinner.adapter = adapter
        binding.fragmentMap.spinner.setSelection(0, false)
        binding.fragmentMap.spinner.onItemSelectedListener = SpnOnItemSelected(binding)

        binding.fragmentMap.webView.settings.javaScriptEnabled = true
        binding.fragmentMap.webView.loadUrl("https://cctvatis4.ntpc.gov.tw/C000232")
    }

    /** 事故觀察及定時 */
    private fun incidentCheck() {
        SyncIncident.incidentLists.observe(viewLifecycleOwner) {
            if (it != null && it.isNotEmpty() && it[0] != oldIncident?.get(0)) {
                AppUtil.showTopToast(requireActivity(), it[0].title)
                oldIncident = it
            }
        }
        viewLifecycleOwner.lifecycleScope.launch {
            while (true) {
                SyncIncident.getIncident(this@MapFragment, this@MapFragment)
                delay(60000)
            }
        }
    }

    /** 測速UI初始化 */
    private fun cameraInit() {
        binding.fragmentMap.slButton.setOnClickListener(slButtonListener)
        SyncCamera.camara.observe(viewLifecycleOwner) {
            if (it != null && it.distance < 500) {
                binding.fragmentMap.slButton.visibility = View.VISIBLE
                binding.fragmentMap.gvSL.text = it.limit
                AppUtil.showTopToast(
                    requireActivity(),
                    "前方限速:${it.limit}公里，距離:${it.distance}公尺"
                )
            } else {
                binding.fragmentMap.gvSL.visibility = View.GONE
                binding.fragmentMap.slButton.setImageResource(R.drawable.sldash)
            }
        }
    }

    /** 測速初始化 */
    private val slButtonListener = View.OnClickListener {
        sitMode = !sitMode
        if (sitMode) {
            opensl()
            startDistanceMeasurement()
        } else {
            mFusedLocationClient?.removeLocationUpdates(locationCallback)
            closeSl()
        }
    }

    /** 開啟測速模式 */
    private fun opensl() {
        binding.fragmentMap.currentSpeed.apply { //設定自身速度
            this.setImageResource(R.drawable.slnull)
            this.visibility = View.VISIBLE
            val layoutParams = this.layoutParams as ViewGroup.MarginLayoutParams
            layoutParams.setMargins(left, 50, right, bottom)  // 替換為你需要的值
            this.layoutParams = layoutParams
        }
        binding.fragmentMap.mySL.text = "0"
        binding.fragmentMap.mySL.visibility = View.VISIBLE
        binding.fragmentMap.slButton.apply {//設定限速
            this.setImageResource(R.drawable.sldash)
            val layoutParams = this.layoutParams as ViewGroup.MarginLayoutParams
            layoutParams.setMargins(left, 250, right, bottom)  // 替換為你需要的值
            this.layoutParams = layoutParams
        }
        binding.fragmentMap.gvSL.visibility = View.GONE
    }

    /** 關閉測速模式 */
    private fun closeSl() {
        binding.fragmentMap.slButton.apply {
            val layoutParams = this.layoutParams as ViewGroup.MarginLayoutParams
            layoutParams.setMargins(left, 5, right, bottom)  // 替換為你需要的值
            this.layoutParams = layoutParams
            this.setImageResource(R.drawable.sl)
        }
        binding.fragmentMap.currentSpeed.visibility = View.GONE
        binding.fragmentMap.mySL.visibility = View.GONE
    }

    /** 開啟定時測速 */
    @SuppressLint("MissingPermission")
    private fun startDistanceMeasurement() {
        val locationRequest = LocationRequest()
        locationRequest.interval = 2000 // two minute interval
        locationRequest.fastestInterval = 2000
        locationRequest.priority = LocationRequest.PRIORITY_BALANCED_POWER_ACCURACY
        mFusedLocationClient?.requestLocationUpdates(
            locationRequest,
            locationCallback,
            Looper.myLooper()
        )
    }

    /** 測速模式邏輯 */
    private val locationCallback = object : LocationCallback() {
        override fun onLocationResult(locationResult: LocationResult?) {
            super.onLocationResult(locationResult)
            locationResult?.lastLocation?.let { location ->
                val newLocation = LatLng(location.latitude, location.longitude)
                lastLatLng?.let {
                    val distance = calculateDistance(it, newLocation)
                    updateUIWithDistance(distance, newLocation)
                    SyncCamera.cameraFindCamera(this@MapFragment, this@MapFragment, newLocation)
                }
                lastLatLng = newLocation
            }
        }
    }

    /** 更新目前速度 */
    private fun updateUIWithDistance(distance: Int, newLocation: LatLng) {
        viewLifecycleOwner.lifecycleScope.launch {
            moveTo(newLocation)
            binding.fragmentMap.mySL.text = (distance * 18 / 10).toString()
        }
    }

    /** 測速模式移動到現在位置 */
    private fun moveTo(location: LatLng) {
        val targetLatLng = LatLng(location.latitude - 0.00006, location.longitude)
        val targetZoomLevel = 18f
        val cameraUpdate = CameraUpdateFactory.newLatLngZoom(targetLatLng, targetZoomLevel)
        map.animateCamera(cameraUpdate, 1000, null)
    }

    /** 計算距離 */
    fun calculateDistance(point1: LatLng, point2: LatLng): Int {
        val earthRadius = 6371.0 // 地球的半徑（以公里為單位）

        // 將經緯度轉換為弧度
        val lat1 = Math.toRadians(point1.latitude)
        val lon1 = Math.toRadians(point1.longitude)
        val lat2 = Math.toRadians(point2.latitude)
        val lon2 = Math.toRadians(point2.longitude)

        // Haversine 公式
        val dlon = lon2 - lon1
        val dlat = lat2 - lat1

        val a = sin(dlat / 2).pow(2) + cos(lat1) * cos(lat2) * sin(dlon / 2).pow(2)
        val c = 2 * atan2(sqrt(a), sqrt(1 - a))

        return (earthRadius * c * 1000).toInt()
    }

    /** 收藏初始化 */
    @RequiresApi(Build.VERSION_CODES.O)
    private fun favoriteInit() {
//        favoriteFlag = dbFavDisplay(Road("南京東路"), requireActivity())
        binding.fragmentMap.fragmentHome.favoriteBtn.apply {
            if (favoriteFlag) {
                this.setImageResource(R.drawable.ic_baseline_star_25)
            } else {
                this.setImageResource(R.drawable.ic_baseline_star_24)
            }
            binding.fragmentMap.fragmentHome.favoriteBtn.setOnClickListener(favoriteBtnListener)
        }
    }

    /** 收藏切換 */
    @RequiresApi(Build.VERSION_CODES.O)
    private val favoriteBtnListener = View.OnClickListener {
        binding.fragmentMap.fragmentHome.favoriteBtn.apply {
            MyLog.e(favoriteFlag.toString())
            if (favoriteFlag) {
                this.setImageResource(R.drawable.ic_baseline_star_24)
                if (searchData != null) {
//                    dbAddFavRoad(searchData!!, requireActivity())
                }
            } else {
                this.setImageResource(R.drawable.ic_baseline_star_25)
                if (searchData != null) {
                    val data = Road(searchData!!.roadName)
//                    dbFavCDelete(data, requireActivity())
                }
            }
            favoriteFlag = !favoriteFlag
        }
    }

    /** 抽屜初始化 */
    private fun bottomSheetInit() {
        binding.fragmentMap.bg.setOnClickListener(backBtnListener)
        behavior = BottomSheetBehavior.from(binding.fragmentMap.bg)
        behavior?.state = BottomSheetBehavior.STATE_HALF_EXPANDED
        behavior?.addBottomSheetCallback(bottomSheetCallback)

        SyncSpeed.speedLists.observe(viewLifecycleOwner) {
            MyLog.e("updateSpeedEnd")
            if (it.isNotEmpty()) {
                MyLog.e("changeSpeed" + it[0].volume + " " + it[0].avgSpeed)
                binding.fragmentMap.cars.text = it[0].volume.toString() + " Cars"
                binding.fragmentMap.speed.text = it[0].avgSpeed.roundToInt().toString() + " km/h"
                if (it.size > 1) {
                    binding.fragmentMap.cars2.text = it[1].volume.toString() + " Cars"
                    binding.fragmentMap.speed2.text =
                        it[1].avgSpeed.roundToInt().toString() + " km/h"
                }
            } else {
                binding.fragmentMap.cars.text = "無資料"
                binding.fragmentMap.speed.text = "無資料"
                binding.fragmentMap.cars2.text = "無資料"
                binding.fragmentMap.speed2.text = "無資料"
            }
            behavior?.state = BottomSheetBehavior.STATE_EXPANDED
            binding.fragmentMap.webView.visibility = View.VISIBLE
            binding.fragmentMap.carDirection.visibility = View.VISIBLE
            binding.fragmentMap.trafficFlow.visibility = View.VISIBLE
            binding.fragmentMap.imageView3.visibility = View.VISIBLE
            binding.fragmentMap.spinner.visibility = View.VISIBLE
        }
    }

    /** 抽屜邏輯 */
    private val bottomSheetCallback = object : BottomSheetCallback() {
        override fun onStateChanged(bottomSheet: View, newState: Int) {
            if (binding.fragmentMap.fragmentSearch.root.visibility == View.GONE) {
                binding.fragmentMap.webView.isVisible = true
                binding.fragmentMap.trafficFlow.isVisible =
                    newState == BottomSheetBehavior.STATE_EXPANDED
                binding.fragmentMap.imageView3.isVisible =
                    newState == BottomSheetBehavior.STATE_EXPANDED
            } else {
                binding.fragmentMap.webView.isVisible = false
                binding.fragmentMap.trafficFlow.isVisible = false
                binding.fragmentMap.imageView3.isVisible = false
            }
        }

        override fun onSlide(bottomSheet: View, slideOffset: Float) {
            // Do something when the bottom sheet is sliding
        }
    }

    /** 側欄按鈕初始化 */
    private val menuButtonListener = View.OnClickListener {
        MyLog.d("openDrawer")
        binding.drawerLayout1.openDrawer(GravityCompat.START)
    }

    /** 跳轉到天氣 */
    @SuppressLint("MissingPermission")
    private val weatherButtonListener = View.OnClickListener {
        if (!permissionDenied) {
            mFusedLocationClient?.lastLocation
                ?.addOnSuccessListener { location: Location? ->
                    location?.let {
                        val bundle = Bundle()
                        bundle.putDouble("lat", it.latitude)
                        bundle.putDouble("long", it.longitude)
                        AppUtil.startFragment(
                            parentFragmentManager,
                            R.id.fragmentMap,
                            WeatherFragment(),
                            bundle
                        )
                    }
                }
        } else {
            enableMyLocation()
        }
    }


    /** 關閉搜尋欄 */
    @SuppressLint("SwitchIntDef")
    private val backBtnListener = View.OnClickListener {
        MyLog.e(isOpen.toString())
        binding.fragmentMap.fragmentSearch.root.visibility = View.GONE
        binding.fragmentMap.fragmentHome.root.visibility = View.VISIBLE
        binding.fragmentMap.webView.visibility = View.VISIBLE
        binding.fragmentMap.spinner.visibility = View.VISIBLE
        when (behavior?.state) {
            //全開
            3 -> {
                if (isOpen) {
                    binding.fragmentMap.carDirection.visibility = View.VISIBLE
                    binding.fragmentMap.trafficFlow.visibility = View.VISIBLE
                }
            }
            //半開
            6 -> {
                if (isOpen) {
                    binding.fragmentMap.carDirection.visibility = View.GONE
                    binding.fragmentMap.trafficFlow.visibility = View.GONE
                }
            }
        }
        isOpen = false
    }

    /** 開啟搜尋欄 */
    private val searchBtnListener = View.OnClickListener {
        isOpen = true
        binding.fragmentMap.fragmentSearch.root.visibility = View.VISIBLE
        binding.fragmentMap.fragmentHome.root.visibility = View.GONE
        binding.fragmentMap.webView.visibility = View.GONE
        binding.fragmentMap.carDirection.visibility = View.GONE
        binding.fragmentMap.trafficFlow.visibility = View.GONE
        binding.fragmentMap.imageView3.visibility = View.GONE
        binding.fragmentMap.spinner.visibility = View.GONE
        binding.fragmentMap.fragmentSearch.searchView.apply {
            this.requestFocus()
            this.onActionViewExpanded()
        }
    }

    /** 搜尋初始化 */
    private fun searchViewInit() {
        binding.fragmentMap.fragmentSearch.searchView.setOnQueryTextListener(queryTextListener)
    }

    /** 初始化搜尋清單 */
    @RequiresApi(Build.VERSION_CODES.O)
    private fun searchListInit() {
        recycleView = binding.fragmentMap.fragmentSearch.recycleView
        recycleView?.setHasFixedSize(true)
        recycleView?.layoutManager = LinearLayoutManager(MyActivity().context)

        adaptor =
            SearchAdaptor(viewModel.searchHistory.value, itemOnClickListener, deleteListener)
        recycleView?.adapter = adaptor
        SyncRoad.searchLists.observe(viewLifecycleOwner) {
            adaptor?.setFilteredList(it)
        }
        viewModel.searchHistory.observe(viewLifecycleOwner) {
            adaptor?.setFilteredList(it)
        }
    }

    /** 當搜尋文字變化及提交 */
    private val queryTextListener =
        object : androidx.appcompat.widget.SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                filterList(query)
                return false
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                filterList(newText)
                return false
            }
        }

    /** 過濾搜尋文字變化 */
    private fun filterList(newText: String?) {
        MyLog.e("textChange")
        if (newText != "" && newText != null) {
            SyncRoad.filterSearch(this, newText, this)
        } else {
            viewModel.searchHistory.value?.let { adaptor?.setFilteredList(it) }
        }
    }

    /** 搜尋清單點擊 */
    @RequiresApi(Build.VERSION_CODES.O)
    private val itemOnClickListener = View.OnClickListener {
        try {
            searchData = it.tag as SearchHistory
            if (searchData != null) {
                searchData!!.searchTime = System.currentTimeMillis()
                viewModel.insertHistory(searchData!!)
                MyLog.d(searchData!!.roadName)
                MyLog.d(searchData!!.roadId)
                var l = 0
                val searchSet = mutableSetOf<CCTV>()
                while (l < searchData!!.roadName.length - 1) {
                    val s = searchData!!.roadName.substring(l, l + 2)
                    if (s.contains("段") || s.contains("路") || s.contains("巷") || s.contains("號") || s.contains(
                            "橋"
                        ) || s.contains("街")
                    ) {
                        l += 1
                        continue
                    }
                    MyLog.e("searchWord:$s")
                    val matchingKeys = mapData.entries.filter { roadData ->
                        roadData.key.contains(s)
                    }
                    if (matchingKeys.isNotEmpty()) {
                        val roadList = matchingKeys.map { (name, no) ->
                            MyLog.e("searchResult:$name")
                            CCTV(name, url = "https://cctvatis4.ntpc.gov.tw/C000$no")
                        }
                        searchSet.addAll(roadList)
                    }
                    l += 1
                    MyLog.e("index:$l")
                }
                MyLog.e("updateAdapterData over")
                updateAdapterData(searchSet.toMutableList())
                SyncSpeed.getCityRoadSpeed(this, searchData!!.roadId, this)
                binding.fragmentMap.fragmentSearch.searchView.setQuery("", false)
                binding.fragmentMap.fragmentSearch.root.visibility = View.GONE
                binding.fragmentMap.fragmentHome.root.visibility = View.VISIBLE
                binding.fragmentMap.fragmentHome.textView.text = searchData!!.roadName
                isOpen = false
                AppUtil.showTopToast(requireActivity(), "搜尋中...")
            }
        } catch (e: Exception) {
            MyLog.e(e.toString())
        }
    }

    /** 刪除歷史紀錄 */
    private val deleteListener = View.OnClickListener {
        try {
            val searchData = it.tag as SearchHistory
            viewModel.deleteHistory(searchData)
        } catch (e: Exception) {
            MyLog.e(e.toString())
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
        weatherInit()
    }

    /** 定位按鈕位置 */
    @SuppressLint("UseRequireInsteadOfGet")
    private fun setLocationInitBtn() {
        if (childFragmentManager
                .findFragmentById(R.id.map) != null && parentFragmentManager
                .findFragmentById(R.id.map)?.view?.findViewById<View>("1".toInt()) != null
        ) {
            val locationButton: View =
                (childFragmentManager
                    .findFragmentById(R.id.map)?.view?.findViewById<View>("1".toInt())?.parent as View).findViewById(
                    "2".toInt()
                )
            val rlp: RelativeLayout.LayoutParams =
                locationButton.layoutParams as RelativeLayout.LayoutParams

            rlp.addRule(RelativeLayout.ALIGN_PARENT_TOP, 0)
            rlp.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM, RelativeLayout.TRUE)
            rlp.setMargins(0, 0, 30, 1000)
        }
    }

    /** 點擊定位 */
    override fun onMyLocationButtonClick(): Boolean {
        moveToCurrentLocation()
        return false
    }

    /** 通用移動到現在位置 */
    @SuppressLint("MissingPermission")
    private fun moveToCurrentLocation() {
        if (!permissionDenied) {
            mFusedLocationClient?.lastLocation
                ?.addOnSuccessListener { location: Location? ->
                    location?.let {
                        val newLatLng = LatLng(location.latitude, location.longitude)
                        if (weatherState) {
                            SyncPosition.weatherLocationApi(
                                this@MapFragment,
                                this@MapFragment,
                                newLatLng
                            )
                        }
                        val targetLatLng =
                            LatLng(newLatLng.latitude - 0.00006, newLatLng.longitude)
                        val targetZoomLevel = 18f
                        val cameraUpdate =
                            CameraUpdateFactory.newLatLngZoom(targetLatLng, targetZoomLevel)
                        map.animateCamera(cameraUpdate, 1000, null)
                    }
                }
        } else {
            enableMyLocation()
        }
    }

    /** 要求定位 */
    @SuppressLint("MissingPermission")
    private fun enableMyLocation() {

        if (ContextCompat.checkSelfPermission(
                requireActivity(),
                Manifest.permission.ACCESS_FINE_LOCATION
            ) == PackageManager.PERMISSION_GRANTED ||
            ContextCompat.checkSelfPermission(
                requireActivity(),
                Manifest.permission.ACCESS_COARSE_LOCATION
            ) == PackageManager.PERMISSION_GRANTED
        ) {
            map.isMyLocationEnabled = true
            return
        }

        // 2. If if a permission rationale dialog should be shown
        if (ActivityCompat.shouldShowRequestPermissionRationale(
                requireActivity(),
                Manifest.permission.ACCESS_FINE_LOCATION
            ) ||
            ActivityCompat.shouldShowRequestPermissionRationale(
                requireActivity(),
                Manifest.permission.ACCESS_COARSE_LOCATION
            )
        ) {
            PermissionUtils.RationaleDialog.newInstance(
                MainActivity.LOCATION_PERMISSION_REQUEST_CODE,
                true
            ).show(parentFragmentManager, "dialog")
            return
        }

        // 3. Otherwise, request permission
        ActivityCompat.requestPermissions(
            requireActivity(),
            arrayOf(
                Manifest.permission.ACCESS_FINE_LOCATION,
                Manifest.permission.ACCESS_COARSE_LOCATION
            ),
            MainActivity.LOCATION_PERMISSION_REQUEST_CODE
        )
    }

    /** 定位權限要求result */
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
            permissionDenied = true
        }
    }

    /** 當失去定位權限錯誤 */
    private fun showMissingPermissionError() {
        PermissionUtils.PermissionDeniedDialog.newInstance(true)
            .show(parentFragmentManager, "dialog")
    }

    /** 批量生成mark */
    private fun initMark() {
        mClusterManager = ClusterManager(context, map)
        mClusterManager?.renderer = CustomClusterRenderer(requireActivity(), map, mClusterManager!!)

        SyncPosition.parkingLists.observe(viewLifecycleOwner) {
            for (p in it) {
                mClusterManager?.addItem(
                    MyItem(
                        p.latitude,
                        p.longitude,
                        "停車場: " + p.parkingName,
                        0
                    )
                )

            }
        }

        SyncCamera.cameraLists.observe(viewLifecycleOwner) {
            for (p in it) {
                MyLog.d(p.road + p.longitude + p.latitude)
                mClusterManager?.addItem(
                    MyItem(
                        p.latitude,
                        p.longitude,
                        "測速: " + p.road + p.introduction,
                        1
                    )
                )
            }
        }

        SyncPosition.oilStation.observe(viewLifecycleOwner) {
            for (p in it) {
                mClusterManager?.addItem(
                    MyItem(
                        p.latitude,
                        p.logitude,
                        "加油站: " + p.address,
                        2
                    )
                )

            }
        }

        map.setOnCameraIdleListener(mClusterManager)
        SyncPosition.parkingApi(this, this)
        SyncCamera.cameraMarkApi(this, this)
        SyncPosition.oilStationApi(this, this)
        SyncPosition.parkingApi(this, this)
        mClusterManager?.setOnClusterItemClickListener {
            false
        }
    }

    /** 點擊mark動作 */
    override fun onMyLocationClick(location: Location) {
        Toast.makeText(activity, "Current location:\n$location", Toast.LENGTH_LONG)
            .show()
        val targetlastLatLng = LatLng(
            location.latitude - 0.00006,
            location.longitude
        )
        val targetZoomLevel = 18f
        val cameraUpdate = CameraUpdateFactory.newLatLngZoom(targetlastLatLng, targetZoomLevel)
        map.animateCamera(cameraUpdate, 1000, null)
    }

    /** 側欄導航 */
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
                AppUtil.startFragment(
                    parentFragmentManager,
                    R.id.fragmentMap,
                    NotificationFragment()
                )
            }
        }

        binding.drawerLayout1.closeDrawer(GravityCompat.START)
        return true
    }

    /** 側欄綁定監聽 */
    private fun setNavigationViewListener() {
        binding.navView.setNavigationItemSelectedListener(this)
    }

    /** API CALLBACK */
    override fun onSuccess(successData: ArrayList<String>) {}

    override fun onError(errorCode: Int, errorData: ArrayList<String>) {}

    override fun doInBackground(result: Int, successData: ArrayList<String>) {}
}