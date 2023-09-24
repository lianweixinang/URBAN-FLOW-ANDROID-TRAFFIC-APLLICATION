package ntutifm.game.google.ui.map

import android.Manifest
import android.annotation.SuppressLint
import android.content.Context
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
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.RelativeLayout
import android.widget.Spinner
import android.widget.TextView
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.core.view.GravityCompat
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.findFragment
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.gms.location.*
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.Marker
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.bottomsheet.BottomSheetBehavior.BottomSheetCallback
import com.google.android.material.navigation.NavigationView
import com.google.maps.android.clustering.ClusterManager
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import ntutifm.game.google.*
import ntutifm.game.google.R
import ntutifm.game.google.databinding.ActivityMainBinding
import ntutifm.game.google.entity.*
import ntutifm.game.google.entity.adaptor.SearchAdaptor
import ntutifm.game.google.global.AppUtil
import ntutifm.game.google.global.MyLog
import ntutifm.game.google.net.*
import ntutifm.game.google.net.ApiClass.CityRoad
import ntutifm.game.google.net.ApiClass.Incident
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
    private var mClusterManager: ClusterManager<MyItem>? = null
    private var behavior: BottomSheetBehavior<View>? = null
    private var favoriteFlag: Boolean = false
    private var searchData: CityRoad? = null
    private var isOpen: Boolean = false
    internal var mCurrLocationMarker: Marker? = null
    internal var mLastLocation: Location? = null
    private var _binding: ActivityMainBinding? = null
    private val binding get() = _binding!!
    private var mFusedLocationClient: FusedLocationProviderClient? = null
    private var permissionDenied = false
    private lateinit var map: GoogleMap
    private lateinit var mLocationRequest: LocationRequest
    private var latLng: LatLng? = null
    private var recycleView: RecyclerView? = null
    private var adaptor: SearchAdaptor? = null
    private val weatherState: Boolean = true
    private var sitMode: Boolean = true
    private var oldIncident: List<Incident>? = null
    private var adapter: RoadAdapter? = null

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
        binding.fragmentMap.fragmentHome.searchBtn.setOnClickListener(searchBtnListener)
        binding.fragmentMap.bg.setOnClickListener(backBtnListener)
        binding.fragmentMap.menuButton.setOnClickListener(menuButtonListener)
        binding.fragmentMap.weatherButton.setOnClickListener(weatherButtonListener)
        binding.fragmentMap.fragmentHome.textContainer.setOnClickListener(searchBtnListener)
        binding.fragmentMap.fragmentHome.searchBtn.setOnClickListener(searchBtnListener)
        cameraInit()
        favoriteInit()
        bottomSheetInit()
        setNavigationViewListener()
        searchViewInit()
        searchListInit()
        webViewInit()
        incidentCheck()
    }
    private fun cameraInit(){
        binding.fragmentMap.slButton.setOnClickListener(slButtonListener)
        SyncCamera.camara.observe(viewLifecycleOwner){
            if(it!=null && it.distance<500){
                binding.fragmentMap.slButton.visibility = View.VISIBLE
                binding.fragmentMap.gvSL.text = it.limit
                AppUtil.showTopToast(requireActivity(),"前方限速:${it.limit}公里，距離:${it.distance}公尺")
            }else{
                binding.fragmentMap.gvSL.visibility = View.GONE
                binding.fragmentMap.slButton.setImageResource(R.drawable.sldash)
            }
        }
    }
    private fun weatherInit() {
        SyncPosition.weatherLocation.observe(viewLifecycleOwner) {
            SyncWeather.weatherDataApi(this, this)
        }
        SyncWeather.weatherLists.observe(viewLifecycleOwner) {
            when (it[SyncPosition.districtToIndex()].weatherDescription) {
                "晴天" -> binding.fragmentMap.weatherButton.setImageResource(R.drawable.sun)
                "雨天" -> binding.fragmentMap.weatherButton.setImageResource(R.drawable.heavy_rain)
            }
        }
    }

    private fun updateAdapterData(dataList: List<CCTV>) {
        MyLog.e("updateAdapterData")
        for (i in dataList){
            MyLog.e("updateAdapterData"+i.name)
        }
        adapter?.clear() // 清除适配器中的数据
        adapter?.addAll(dataList) // 将新的数据列表添加到适配器
        adapter?.notifyDataSetChanged() // 通知适配器数据已更改
    }

    /** 監視器初始化 */
    @SuppressLint("SetJavaScriptEnabled")
    private fun webViewInit() {
        adapter = RoadAdapter(
            requireActivity(),
            android.R.layout.simple_spinner_item)

        adapter?.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        binding.fragmentMap.spinner.adapter = adapter
        binding.fragmentMap.spinner.setSelection(0, false)
        binding.fragmentMap.spinner.onItemSelectedListener = SpnOnItemSelected(binding)

        binding.fragmentMap.webView.settings.javaScriptEnabled = true
        binding.fragmentMap.webView.loadUrl("https://cctvatis4.ntpc.gov.tw/C000232")
    }
    class RoadAdapter(context: Context, resource: Int, objects: MutableList<CCTV> = mutableListOf(CCTV("九份老街",url="https://cctvatis4.ntpc.gov.tw/C000232"),CCTV("至善路-福林路口",url="https://cctvatis4.ntpc.gov.tw/C000233"),CCTV("福林路-雨農路-中正路口",url="https://cctvatis4.ntpc.gov.tw/C000234"))) :
        ArrayAdapter<CCTV>(context, resource, objects) {

        override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
            val road = getItem(position)
            val view = convertView ?: LayoutInflater.from(context).inflate(
                android.R.layout.simple_spinner_item,
                parent,
                false
            )

            val textView = view.findViewById<TextView>(android.R.id.text1)
            textView.text = road?.name
            view.tag = road
            return view
        }

        override fun getDropDownView(position: Int, convertView: View?, parent: ViewGroup): View {
            return getView(position, convertView, parent)
        }
        override fun addAll(vararg items: CCTV?) {
            super.addAll(*items)
        }
    }
    class SpnOnItemSelected(val binding:ActivityMainBinding): AdapterView.OnItemSelectedListener{
        override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
            val selectedItem = parent?.getItemAtPosition(position) as? CCTV
            selectedItem?.let {
                binding.fragmentMap.webView.loadUrl(it.url)
            }

        }

        override fun onNothingSelected(parent: AdapterView<*>?) {

        }

    }

    private fun incidentCheck() {
        SyncIncident.incidentLists.observe(viewLifecycleOwner) {
            if (it != null && it[0] != oldIncident?.get(0)) {
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

    /** 測速初始化 */
    private val slButtonListener = View.OnClickListener {
        sitMode = !sitMode
        if (sitMode) {
            viewLifecycleOwner.lifecycleScope.launch {
                binding.fragmentMap.currentSpeed.apply { //設定自身速度
                    this.setImageResource(R.drawable.slnull)
                    this.visibility = View.VISIBLE
                    val layoutParams = this.layoutParams as ViewGroup.MarginLayoutParams
                    layoutParams.setMargins(left, 50, right, bottom)  // 替換為你需要的值
                    this.layoutParams = layoutParams
                }
                binding.fragmentMap.mySL.text="0"
                binding.fragmentMap.mySL.visibility=View.VISIBLE
                binding.fragmentMap.slButton.apply() {//設定限速
                    this.setImageResource(R.drawable.sldash)
                    val layoutParams = this.layoutParams as ViewGroup.MarginLayoutParams
                    layoutParams.setMargins(left, 250, right, bottom)  // 替換為你需要的值
                    this.layoutParams = layoutParams
                }
                binding.fragmentMap.gvSL.visibility=View.GONE
                val lastPosition = latLng
                //開啟測速模式
                while (sitMode) {
                    moveToCurrentLocation()
                    if(latLng !=null && lastPosition != latLng){
                        val distance = calculateDistance(lastPosition!!,latLng!!)
                        val kmPerHour = distance * 6 / 10
                        binding.fragmentMap.mySL.text = kmPerHour.toString()
                        //修改UI
                        SyncCamera.cameraFindCamera(this@MapFragment,this@MapFragment,latLng!!)
                    }else{
                        binding.fragmentMap.mySL.text = "0"
                    }

                    delay(6000)
                }
                //關閉測速模式
                binding.fragmentMap.slButton.apply {
                    val layoutParams = this.layoutParams as ViewGroup.MarginLayoutParams
                    layoutParams.setMargins(left, 5, right, bottom)  // 替換為你需要的值
                    this.layoutParams = layoutParams
                    this.setImageResource(R.drawable.sl)
                }
                binding.fragmentMap.currentSpeed.visibility = View.GONE
                return@launch
            }
        }
    }
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

    @RequiresApi(Build.VERSION_CODES.O)
    private fun favoriteInit() {
        favoriteFlag = dbFavDisplay(Road("南京東路"), requireActivity())
        binding.fragmentMap.fragmentHome.favoriteBtn.apply {
            if (favoriteFlag) {
                this.setImageResource(R.drawable.ic_baseline_star_25)
            } else {
                this.setImageResource(R.drawable.ic_baseline_star_24)
            }
            binding.fragmentMap.fragmentHome.favoriteBtn.setOnClickListener(favoriteBtnListener)
        }
    }

    private fun bottomSheetInit() {
        val bottomSheet: View = binding.fragmentMap.bg
        behavior = BottomSheetBehavior.from(bottomSheet)
        behavior?.state = BottomSheetBehavior.STATE_HALF_EXPANDED
        behavior?.addBottomSheetCallback(object : BottomSheetCallback() {
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
        })

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

    private val menuButtonListener = View.OnClickListener {
        MyLog.d("openDrawer")
        binding.drawerLayout1.openDrawer(GravityCompat.START)
    }
    private val weatherButtonListener = View.OnClickListener {
        if (latLng != null) {
            val bundle = Bundle()
            bundle.putDouble("lat", latLng!!.latitude)
            bundle.putDouble("long", latLng!!.longitude)
            AppUtil.startFragment(
                parentFragmentManager,
                R.id.fragmentMap,
                WeatherFragment(),
                bundle
            )
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
                    dbAddFavRoad(searchData!!, requireActivity())
                }
            } else {
                this.setImageResource(R.drawable.ic_baseline_star_25)
                if (searchData != null) {
                    val data = Road(searchData!!.roadName)
                    dbFavCDelete(data, requireActivity())
                }
            }
            favoriteFlag = !favoriteFlag
        }

    }

    /** 關閉搜尋欄 */
    private val backBtnListener = View.OnClickListener {
        MyLog.e(isOpen.toString())
        binding.fragmentMap.fragmentSearch.root.visibility = View.GONE
        binding.fragmentMap.fragmentHome.root.visibility = View.VISIBLE
        binding.fragmentMap.webView.visibility = View.VISIBLE
        binding.fragmentMap.spinner.visibility = View.VISIBLE
        when (behavior?.state) {
            //全開
            3 -> {
                if (isOpen == true) {
                    binding.fragmentMap.carDirection.visibility = View.VISIBLE
                    binding.fragmentMap.trafficFlow.visibility = View.VISIBLE
                }
            }
            //半開
            6 -> {
                if (isOpen == true) {
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
        MyLog.e(isOpen.toString())
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

    /** 初始化搜尋*/
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
            SearchAdaptor(dbDisplayHistory(requireActivity()), itemOnClickListener, deleteListener)
        recycleView?.adapter = adaptor
        SyncRoad.searchLists.observe(viewLifecycleOwner) {
            adaptor?.setFilteredList(it)
        }
    }

    /** 當文字變化 */
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

    /** 文字變化 */
    private fun filterList(newText: String?) {
        MyLog.e("textChange")
        if (newText != "" && newText != null) {
            SyncRoad.filterSearch(this, newText, this)
        } else {
            adaptor?.setFilteredList(dbDisplayHistory(requireActivity()))
        }

    }

    /** 搜尋清單點擊 */
    @RequiresApi(Build.VERSION_CODES.O)
    private val itemOnClickListener = View.OnClickListener {
        try {
            searchData = it.tag as CityRoad
            if (searchData != null) {
                dbAddHistory(searchData!!, requireActivity())
                MyLog.d(searchData!!.roadName)
                MyLog.d(searchData!!.roadId)
                var l = 0
                val searchSet = mutableSetOf<CCTV>()
                while(l < searchData!!.roadName.length-1){
                    val s = searchData!!.roadName.substring(l,l+2)
                    if(s.contains("段") ||s.contains("路") || s.contains("巷") || s.contains("號") ||s.contains("橋") ||s.contains("街") ){
                        l+=1
                        continue
                    }
                    MyLog.e("searchWord:$s")
                    val matchingKeys = mapData.entries.filter { roadData->
                        roadData.key.contains(s)
                    }
                    if (matchingKeys.isNotEmpty()) {
                        val roadList = matchingKeys.map { (name, no) ->
                            MyLog.e("searchResult:$name")
                            CCTV(name, url= "https://cctvatis4.ntpc.gov.tw/C000$no")
                        }
                        searchSet.addAll(roadList)
                    }
                    l+=1
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
            val searchData = it.tag as CityRoad
            dbDeleteHistory(searchData.roadName, requireActivity())
            adaptor?.setFilteredList(dbDisplayHistory(requireActivity()))
        } catch (e: Exception) {
            MyLog.e(e.toString())
        }
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
                    .findFragmentById(R.id.map)?.view?.findViewById<View>("1".toInt())?.parent as View).findViewById<View>(
                    "2".toInt()
                )
            val rlp: RelativeLayout.LayoutParams =
                locationButton.layoutParams as RelativeLayout.LayoutParams

            rlp.addRule(RelativeLayout.ALIGN_PARENT_TOP, 0)
            rlp.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM, RelativeLayout.TRUE)
            rlp.setMargins(0, 0, 30, 1000)
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
            if (weatherState && latLng != null) {
                SyncPosition.weatherLocationApi(this@MapFragment, this@MapFragment, latLng!!)
            }
            if (latLng != null) {
                map.moveCamera(
                    CameraUpdateFactory.newLatLngZoom(
                        LatLng(
                            latLng!!.latitude - 0.00005,
                            latLng!!.longitude
                        ), 50f
                    )
                )
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
    private fun moveToCurrentLocation() {
        mLocationRequest = LocationRequest()
        mLocationRequest.interval = 120000 // two minute interval
        mLocationRequest.fastestInterval = 120000
        mLocationRequest.priority = LocationRequest.PRIORITY_BALANCED_POWER_ACCURACY
        if (!permissionDenied) {
            mFusedLocationClient?.requestLocationUpdates(
                mLocationRequest,
                mLocationCallback,
                Looper.myLooper()
            )
        } else {
            enableMyLocation()
        }
        if (latLng != null) {
            map.moveCamera(
                CameraUpdateFactory.newLatLngZoom(
                    LatLng(
                        latLng!!.latitude - 0.00005,
                        latLng!!.longitude
                    ), 50f
                )
            )
        }
    }

    /** 點擊mark動作 */
    override fun onMyLocationClick(location: Location) {
        Toast.makeText(activity, "Current location:\n$location", Toast.LENGTH_LONG)
            .show()
        map.moveCamera(
            CameraUpdateFactory.newLatLngZoom(
                LatLng(
                    location.latitude - 0.00005,
                    location.longitude
                ), 50f
            )
        )
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
        PermissionUtils.PermissionDeniedDialog.newInstance(true)
            .show(parentFragmentManager, "dialog")
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
        Log.e("GGG", "Ggg")
        _binding = null
    }

    /** 批量生成mark */
    private fun initMark() {
        mClusterManager = ClusterManager(context, map)
        mClusterManager?.renderer = CustomClusterRenderer(requireActivity(), map, mClusterManager!!)
        SyncPosition.parkingLists.observe(viewLifecycleOwner) {
            for (p in it) {
                mClusterManager?.addItem(MyItem(p.lat, p.lng, "停車場: " + p.parkingName, 0))

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
                mClusterManager?.addItem(MyItem(p.latitude, p.logitude, "加油站: " + p.address, 2))

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

    override fun onSuccess(successData: ArrayList<String>) {}

    override fun onError(errorCode: Int, errorData: ArrayList<String>) {}

    override fun doInBackground(result: Int, successData: ArrayList<String>) {}

}