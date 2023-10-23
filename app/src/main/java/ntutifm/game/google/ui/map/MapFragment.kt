package ntutifm.game.google.ui.map

import android.Manifest
import android.annotation.SuppressLint
import android.content.Context
import android.content.pm.PackageManager
import android.hardware.Sensor
import android.hardware.SensorEvent
import android.hardware.SensorEventListener
import android.hardware.SensorManager
import android.location.Location
import android.net.http.SslError
import android.os.Build
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.speech.tts.TextToSpeech
import android.util.Log
import android.view.*
import android.webkit.SslErrorHandler
import android.webkit.WebView
import android.webkit.WebViewClient
import android.widget.*
import androidx.annotation.RequiresApi
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.core.view.GravityCompat
import androidx.core.view.isVisible
import androidx.drawerlayout.widget.DrawerLayout
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.NavOptions
import androidx.navigation.Navigation
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.gms.location.*
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.CameraPosition
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.PolylineOptions
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.bottomsheet.BottomSheetBehavior.BottomSheetCallback
import com.google.maps.android.PolyUtil
import com.google.maps.android.clustering.ClusterManager
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import ntutifm.game.google.*
import ntutifm.game.google.R
import ntutifm.game.google.apiClass.*
import ntutifm.game.google.databinding.FragmentMapBinding
import ntutifm.game.google.entity.*
import ntutifm.game.google.entity.adaptor.RoadAdaptor
import ntutifm.game.google.entity.adaptor.SearchAdaptor
import ntutifm.game.google.entity.adaptor.SpnOnItemSelected
import ntutifm.game.google.entity.mark.MyItem
import ntutifm.game.google.entity.sync.*
import ntutifm.game.google.global.AppUtil
import ntutifm.game.google.global.MyLog
import ntutifm.game.google.net.*
import java.util.*
import kotlin.math.*


@SuppressLint("InflateParams")
class MapFragment : Fragment(), GoogleMap.OnMyLocationButtonClickListener,
    GoogleMap.OnMyLocationClickListener, OnMapReadyCallback,
    ActivityCompat.OnRequestPermissionsResultCallback, ApiCallBack {
    private var _binding: FragmentMapBinding? = null
    private val binding get() = _binding!!
    private val viewModel: MapViewModel by lazy {
        ViewModelProvider(
            this,
            MapViewModel.MapViewModelFactory(requireActivity().application)
        )[MapViewModel::class.java]
    }

    private var mClusterManager: ClusterManager<MyItem<Any>>? = null
    private var behavior: BottomSheetBehavior<View>? = null
    private lateinit var map: GoogleMap
    private var textToSpeech: TextToSpeech? = null
    private val infoWindowView: View by lazy {
        layoutInflater.inflate(R.layout.mark_detail, null)
    }
    private val markFavorite: ImageView by lazy {
        infoWindowView.findViewById<View>(R.id.mark_like) as ImageView
    }

    data class UiState(
        var isOpen: Boolean = false,
        var sitMode: Boolean = false,
        var weatherState: Boolean = true
    )

    data class InitializationState(
        var isTTSInitialized: Boolean = false,
        var isUiInitialized: Boolean = false,
        var permissionDenied: Boolean = false
    )

    data class UserInteractionState(
        var favoriteFlag: Boolean = false,
        var markLike: Boolean = false,
        var noChange: Boolean = false
    )
    private val uiState = UiState()
    private val initializationState = InitializationState()
    private val userInteractionState = UserInteractionState()


    private var searchData: SearchHistory? = null
    private var oldIncident: List<Incident>? = null
    private var mFusedLocationClient: FusedLocationProviderClient? = null
    private var lastLatLng: LatLng? = null
    private var lastCamera: Camera? = null
    private var azimuth: Float = 0f
    private var roadFavorite: SearchHistory? = null

    private var recycleView: RecyclerView? = null
    private var adaptor: SearchAdaptor? = null
    private var adapter: RoadAdaptor? = null

    private var destination: LatLng? = null
    private var cctv: CCTV? = null

    /** 根據layout建構畫面 */
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?,
    ): View {
        _binding = FragmentMapBinding.inflate(inflater, container, false)
        arguments?.let { bundle ->
            initializationState.isUiInitialized = bundle.getBoolean("notReset")
            destination = LatLng(
                bundle.getDouble("latitude"),
                bundle.getDouble("longitude")
            )
            cctv = CCTV(
                null,
                bundle.getString("cctvName", ""),
                bundle.getString("cctvUrl", "")
            )
            roadFavorite = SearchHistory(
                null,
                bundle.getString("roadId", ""),
                bundle.getString("roadName", ""),
                null
            )
        }
        return binding.root
    }

    /** 邏輯功能初始化 */
    @RequiresApi(Build.VERSION_CODES.O)
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        loadingView()
        val mapFragment = childFragmentManager.findFragmentById(R.id.map) as SupportMapFragment?
        mapFragment?.getMapAsync(this)
        binding.menuButton.setOnClickListener(menuButtonListener)
        speechInit()
        titleInit()
        cameraInit()
        searchInit()
        favoriteInit()
        bottomSheetInit()
        incidentInit()
        webViewInit()
        roadInit()
    }

    override fun onResume() {
        super.onResume()
        if (initializationState.permissionDenied) {
            showMissingPermissionError()
            initializationState.permissionDenied = false
        }
    }

    override fun onDestroyView() {
        _binding = null
        textToSpeech?.stop()
        textToSpeech?.shutdown()
        super.onDestroyView()
    }

    @RequiresApi(Build.VERSION_CODES.O)
    private fun roadInit() {
        if (roadFavorite != null && roadFavorite?.roadId != "") {
            searchItem(roadFavorite!!)
        } else {
            userInteractionState.noChange = true
            searchItem(SearchHistory(null, "600333A", "忠孝東路一段", null))
        }
    }

    /** 初始化文字轉語音 */
    private fun speechInit() {
        textToSpeech = TextToSpeech(requireActivity()) { status ->
            if (status == TextToSpeech.SUCCESS) {
                val result = textToSpeech?.setLanguage(Locale.CHINESE)
                if (result == TextToSpeech.LANG_MISSING_DATA || result == TextToSpeech.LANG_NOT_SUPPORTED) {
                    Log.e("TTS", "該語言不被支持或缺少數據")
                } else {
                    initializationState.isTTSInitialized = true
                }
            } else {
                Log.e("TTS", "初始化失敗")
            }
        }
    }

    /** 關掉cover */
    private fun loadingView() {
        if (!initializationState.isUiInitialized) {
            viewLifecycleOwner.lifecycleScope.launch(Dispatchers.Default) {
                delay(6000)
                withContext(Dispatchers.Main) {
                    binding.cover.visibility = View.GONE
                }
            }
        } else {
            binding.cover.visibility = View.GONE
        }
    }

    /** 撥放語音 */
    private fun speakText(textToSpeak: String) {
        if (initializationState.isTTSInitialized) {
            textToSpeech?.speak(textToSpeak, TextToSpeech.QUEUE_FLUSH, null, null)
        }
    }

    /** 標題初始化 */
    private fun titleInit() {
        binding.fragmentHome.searchBtn.setOnClickListener(searchBtnListener)
        binding.fragmentHome.textContainer.setOnClickListener(searchBtnListener)
        binding.fragmentHome.searchBtn.setOnClickListener(searchBtnListener)
    }

    /** 天氣初始化 */
    private fun weatherInit() {
        binding.weatherButton.setOnClickListener(weatherButtonListener)
        SyncPosition.weatherLocation.observe(viewLifecycleOwner) {
            SyncWeather.weatherDataApi(this, this)
        }
        SyncWeather.weatherLists.observe(viewLifecycleOwner) {
            binding.weatherButton.setImageResource(
                when (it[SyncPosition.districtToIndex()].wx1) {
                    "晴天" -> R.drawable.sun
                    "雨天" -> R.drawable.heavy_rain
                    "短暫陣雨" -> R.drawable.heavy_rain
                    "多雲短暫陣雨" -> R.drawable.heavy_rain
                    "陰時多雲短暫陣雨" -> R.drawable.heavy_rain
                    "多雲午後短暫陣雨" -> R.drawable.heavy_rain
                    "晴時多雲" -> R.drawable.cloudy
                    "多雲晴時" -> R.drawable.cloudy
                    "午後短暫雷陣雨" -> R.drawable.storm
                    "短暫陣雨或雷雨" -> R.drawable.storm
                    "多雲午後短暫雷陣雨" -> R.drawable.storm
                    "多雲" -> R.drawable.cloudy_nosun
                    "多雲時陰" -> R.drawable.cloudy_nosun
                    "多雲時晴" -> R.drawable.cloudy_nosun
                    "陰時多雲" -> R.drawable.cloudy_nosun
                    else -> R.drawable.cloudy_nosun
                }
            )
        }
    }

    /** 更新錄像清單 */
    private fun updateAdapterData(dataList: List<CCTV>) {
        if (BuildConfig.DEBUG) {
            MyLog.e("updateAdapterData")
            for (i in dataList) {
                MyLog.e("updateAdapterData" + i.name)
            }
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


        binding.spinner.adapter = adapter
        binding.spinner.setSelection(0, false)
        binding.spinner.onItemSelectedListener = SpnOnItemSelected(
            binding, viewModel
        ) { attachCCTVListener() }
        binding.webView.webViewClient = object : WebViewClient() {
            @SuppressLint("WebViewClientOnReceivedSslError")
            override fun onReceivedSslError(
                view: WebView?,
                handler: SslErrorHandler?,
                error: SslError?,
            ) {
                handler?.proceed()
            }
        }
        binding.webView.settings.apply {
            this.javaScriptEnabled = true
            this.loadWithOverviewMode = true
            this.useWideViewPort = true
        }
        if (cctv?.url != "" && cctv?.url != null) {
            adapter?.clear()
            adapter?.add(cctv)
            adapter?.notifyDataSetChanged()
            viewModel.checkCCTV(cctv!!.name)
            binding.fragmentHome.textView.text = cctv?.name
            binding.webView.loadUrl(cctv!!.url)
            binding.carDirection1.visibility = View.GONE
            binding.trafficFlow.visibility = View.GONE
        } else {
            binding.webView.loadUrl("https://cctv.bote.gov.taipei:8501/mjpeg/232")
        }
    }

    private fun attachCCTVListener() {
        binding.fragmentHome.favoriteBtn.setOnClickListener(cctvListener)
    }

    private fun accident(c: FragmentActivity, incident: Incident) {
        MyLog.e(incident.title)
        // 載入自定義的 layout
        val inflater = c.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
        val popupView = inflater.inflate(R.layout.accident_item, null)
        val popupText2 = popupView.findViewById<TextView>(R.id.popup_text2)
        val popupText = popupView.findViewById<TextView>(R.id.popup_text)
        val popupText1 = popupView.findViewById<TextView>(R.id.popup_text1)
        popupText2.text = incident.part
        popupText.text = incident.title
        popupText1.text = if (incident.solved == "nxx") "尚未排除" else incident.solved
        MyLog.e("設定PopupWindow資料")
        // 創建 PopupWindow
        val popupWindow = PopupWindow(
            popupView,
            ViewGroup.LayoutParams.WRAP_CONTENT,
            ViewGroup.LayoutParams.WRAP_CONTENT
        ).apply {
            isOutsideTouchable = true
        }

        popupText.setOnClickListener {
            popupWindow.dismiss()
            val navController = Navigation.findNavController(binding.root)
            val navOptions = NavOptions.Builder()
                .setPopUpTo(R.id.mapFragment, true)
                .build()
            val bundle = Bundle()
            bundle.putBoolean("notReset", true)
            navController.navigate(R.id.notificationFragment, bundle, navOptions)
        }
        Handler(Looper.getMainLooper()).post {
            popupWindow.showAtLocation(
                popupView,
                Gravity.TOP,
                0,
                300
            )
        }
        Handler(Looper.getMainLooper()).postDelayed({
            popupWindow.dismiss()
        }, 5000)
    }

    /** 事故觀察及定時 */
    private fun incidentInit() {
        SyncIncident.incidentLists.observe(viewLifecycleOwner) {
            if (it != null && it.isNotEmpty() && it[0] != oldIncident?.get(0)) {
                accident(requireActivity(), it[0])
                oldIncident = it
            }
        }

        viewLifecycleOwner.lifecycleScope.launch {
            while (true) {
                delay(6000)
                SyncIncident.getIncident(this@MapFragment, this@MapFragment)
            }
        }
    }

    /** 測速UI初始化 */
    private fun cameraInit() {
        binding.mySpeedButton.setOnClickListener(slButtonListener)
        SyncCamera.camara.observe(viewLifecycleOwner) {
            fun showCurrent(distance: Int) {
                val text = "前方限速:${it.limit}公里，距離:${distance}公尺"
                AppUtil.showTopToast(
                    requireActivity(),
                    text
                )
                speakText(text)
            }
            if (it != null && it.distance < 500) {
                binding.cameraSpeedButton.setImageResource(R.drawable.slnull)
                binding.cameraSpeedNumber.apply {
                    this.visibility = View.VISIBLE
                    this.text = it.limit
                }
                when (it.distance) {
                    in 251..500 -> {
                        if (lastCamera == null || (it.cameraId != lastCamera!!.cameraId)) {
                            lastCamera = it
                        }
                    }

                    in 151..250 -> {
                        if ((lastCamera!!.distance in 151..250) ||
                            lastCamera!!.distance < it.distance
                        ) {
                            return@observe
                        }
                        lastCamera!!.distance = it.distance
                        showCurrent(200)
                    }

                    in 51..150 -> {
                        if ((lastCamera!!.distance in 51..150) ||
                            lastCamera!!.distance < it.distance
                        ) {
                            return@observe
                        }
                        lastCamera!!.distance = it.distance
                        showCurrent(100)
                    }

                    in 3..50 -> {
                        if ((lastCamera!!.distance in 3..50) ||
                            lastCamera!!.distance < it.distance
                        ) {
                            return@observe
                        }
                        lastCamera!!.distance = it.distance
//                        showCurrent(50)
                    }

                    in 0..2 -> {
                        if ((lastCamera!!.distance in 0..2) ||
                            lastCamera!!.distance < it.distance
                        ) {
                            return@observe
                        }
                        lastCamera!!.distance = it.distance
                        AppUtil.showTopToast(
                            requireActivity(),
                            "通過測速向"
                        )
                    }
                }
            } else {
                binding.cameraSpeedButton.setImageResource(R.drawable.sldash)
                binding.cameraSpeedNumber.visibility = View.GONE
            }
        }
    }


    /** 測速初始化 */
    private val slButtonListener = View.OnClickListener {
        uiState.sitMode = !uiState.sitMode
        if (uiState.sitMode) {
            AppUtil.showTopToast(requireActivity(), "開啟測速模式")
            speakText("開啟測速模式")
            opensl()
            startDistanceMeasurement()
        } else {
            AppUtil.showTopToast(requireActivity(), "關閉測速模式")
            speakText("關閉測速模式")
            mFusedLocationClient?.removeLocationUpdates(locationCallback)
            closeSl()
        }
    }

    /** 開啟測速模式 */
    private fun opensl() {
        binding.mySpeedButton.setImageResource(R.drawable.slnull)
        binding.mySpeedNumber.apply {
            this.text = "0"
            this.visibility = View.VISIBLE
        }
        binding.cameraSpeedButton.apply {//設定限速
            this.setImageResource(R.drawable.sldash)
            this.visibility = View.VISIBLE
        }
        binding.cameraSpeedNumber.visibility = View.GONE
    }

    /** 關閉測速模式 */
    private fun closeSl() {
        binding.mySpeedButton.setImageResource(R.drawable.sl)
        binding.mySpeedNumber.visibility = View.GONE
        binding.cameraSpeedButton.visibility = View.GONE
        binding.cameraSpeedNumber.visibility = View.GONE
    }

    /** 開啟定時測速 */
    @SuppressLint("MissingPermission", "VisibleForTests")
    private fun startDistanceMeasurement() {
        val locationRequest = LocationRequest()
        locationRequest.interval = 1000
        locationRequest.fastestInterval = 1000
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
            binding.mySpeedNumber.text = (distance * 36 / 10).toString()
            if (lastCamera != null && lastCamera!!.limit.toInt() < (distance * 36 / 10)) {
                speakText("注意!!您已超速")
                AppUtil.showTopToast(requireActivity(), "注意!!您已超速")
            }
        }
    }

    /** 測速模式移動到現在位置 */
    private fun moveTo(location: LatLng) {
        val targetLatLng = LatLng(location.latitude - 0.00006, location.longitude)
        val currentCameraPosition = map.cameraPosition
        val newCameraPosition = CameraPosition.builder(currentCameraPosition)
            .target(targetLatLng)
            .bearing(azimuth)
            .zoom(18f)
            .build()
        Handler(Looper.getMainLooper()).post {
            map.animateCamera(CameraUpdateFactory.newCameraPosition(newCameraPosition))

        }
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
        viewLifecycleOwner.lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.favoriteState.collect {
                    userInteractionState.favoriteFlag = if (it) {
                        binding.fragmentHome.favoriteBtn.setImageResource(R.drawable.ic_baseline_star_25)
                        true
                    } else {
                        binding.fragmentHome.favoriteBtn.setImageResource(R.drawable.ic_baseline_star_24)
                        false
                    }
                }
            }
        }
        viewLifecycleOwner.lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.markState.collect {
                    MyLog.d("收藏:$it")
                    userInteractionState.noChange = if (it) {
                        markFavorite.setImageResource(R.drawable.ic_baseline_star_25)
                        true
                    } else {
                        markFavorite.setImageResource(R.drawable.ic_baseline_star_24)
                        false
                    }
                }
            }
        }
        binding.fragmentHome.favoriteBtn.setOnClickListener(favoriteBtnListener)
    }

    /** 收藏切換 */
    @RequiresApi(Build.VERSION_CODES.O)
    private val favoriteBtnListener = View.OnClickListener {
        val data = it.tag as SearchHistory
        if (BuildConfig.DEBUG) {
            MyLog.e(userInteractionState.favoriteFlag.toString())
        }
        if (userInteractionState.favoriteFlag) {
            binding.fragmentHome.favoriteBtn.setImageResource(R.drawable.ic_baseline_star_24)
            viewModel.deleteFavorite(data.roadName)
        } else {
            binding.fragmentHome.favoriteBtn.setImageResource(R.drawable.ic_baseline_star_25)
            viewModel.addFavorite(
                RoadFavorite(
                    id = null,
                    roadId = data.roadId,
                    roadName = data.roadName
                )
            )
        }
        userInteractionState.favoriteFlag = !userInteractionState.favoriteFlag

    }

    private val cameraMarkListener = View.OnClickListener {
        val data = it.tag as Camera
        val image = it as ImageView
        if (userInteractionState.noChange) {
            viewModel.deleteMarkCamera(data.cameraId)
            image.setImageResource(R.drawable.ic_baseline_star_24)
        } else {
            viewModel.addMarkCamera(data)
            image.setImageResource(R.drawable.ic_baseline_star_25)
        }
        userInteractionState.noChange = !userInteractionState.noChange
    }

    /** 抽屜初始化 */
    @SuppressLint("SetTextI18n")
    private fun bottomSheetInit() {
        binding.bg.setOnClickListener(backBtnListener)
        behavior = BottomSheetBehavior.from(binding.bg)
        behavior?.state = BottomSheetBehavior.STATE_COLLAPSED
        behavior?.addBottomSheetCallback(bottomSheetCallback)

        SyncSpeed.speedLists.observe(viewLifecycleOwner) {
            if (BuildConfig.DEBUG) {
                MyLog.e("updateSpeedEnd")
            }
            if (it.isNotEmpty()) {
                val oneSpeed = it[0]
                binding.cars.text = "${oneSpeed.volume} Cars"
                binding.speed.text = "${oneSpeed.avgSpeed.roundToInt()} km/h"
                if (it.size > 1) {
                    val twoSpeed = it[1]
                    binding.cars2.text = "${twoSpeed.volume} Cars"
                    binding.speed2.text = "${twoSpeed.avgSpeed.roundToInt()} km/h"
                }
            } else {
                binding.cars.text = "無資料"
                binding.speed.text = "無資料"
                binding.cars2.text = "無資料"
                binding.speed2.text = "無資料"
            }
            if (!userInteractionState.noChange) {
                behavior?.state = BottomSheetBehavior.STATE_EXPANDED
                binding.webView.visibility = View.VISIBLE
                binding.carDirection1.visibility = View.VISIBLE
                binding.trafficFlow.visibility = View.VISIBLE
                binding.spinner.visibility = View.VISIBLE
            }
        }
    }

    /** 抽屜邏輯 */
    private val bottomSheetCallback = object : BottomSheetCallback() {
        override fun onStateChanged(bottomSheet: View, newState: Int) {
            if (binding.fragmentSearch.root.visibility == View.GONE) {
                binding.webView.isVisible = true
                binding.trafficFlow.isVisible =
                    newState == BottomSheetBehavior.STATE_EXPANDED
            } else {
                binding.webView.isVisible = false
                binding.trafficFlow.isVisible = false
            }
        }

        override fun onSlide(bottomSheet: View, slideOffset: Float) {
            // Do something when the bottom sheet is sliding
        }
    }

    /** 側欄按鈕初始化 */
    private val menuButtonListener = View.OnClickListener {
        if (BuildConfig.DEBUG) {
            MyLog.d("openDrawer")
        }
        val drawerView =
            requireActivity().findViewById<View>(R.id.drawerLayout1) as DrawerLayout
        drawerView.openDrawer(GravityCompat.START)
    }

    /** 跳轉到天氣 */
    @SuppressLint("MissingPermission")
    private val weatherButtonListener = View.OnClickListener {
        if (!initializationState.permissionDenied) {
            mFusedLocationClient?.lastLocation
                ?.addOnSuccessListener { location: Location? ->
                    location?.let {
                        val bundle = Bundle()
                        bundle.putDouble("lat", it.latitude)
                        bundle.putDouble("long", it.longitude)
                        Navigation.findNavController(binding.root)
                            .navigate(R.id.action_mapFragment_to_weatherFragment)
                    }
                }
        } else {
            enableMyLocation()
        }
    }


    /** 關閉搜尋欄 */
    @SuppressLint("SwitchIntDef")
    private val backBtnListener = View.OnClickListener {
        if (BuildConfig.DEBUG) {
            MyLog.e(uiState.isOpen.toString())
        }
        binding.fragmentSearch.root.visibility = View.GONE
        binding.fragmentHome.root.visibility = View.VISIBLE
        binding.webView.visibility = View.VISIBLE
        binding.spinner.visibility = View.VISIBLE
        when (behavior?.state) {
            //全開
            3 -> {
                if (uiState.isOpen) {
                    binding.carDirection1.visibility = View.VISIBLE
                    binding.trafficFlow.visibility = View.VISIBLE
                }
            }
            //半開
            6 -> {
                if (uiState.isOpen) {
                    binding.carDirection1.visibility = View.GONE
                    binding.trafficFlow.visibility = View.GONE
                }
            }
        }
        uiState.isOpen = false
    }

    /** 開啟搜尋欄 */
    private val searchBtnListener = View.OnClickListener {
        uiState.isOpen = true
        binding.fragmentSearch.root.visibility = View.VISIBLE
        binding.fragmentHome.root.visibility = View.GONE
        binding.webView.visibility = View.GONE
        binding.carDirection1.visibility = View.GONE
        binding.trafficFlow.visibility = View.GONE
        binding.spinner.visibility = View.GONE
        binding.fragmentSearch.searchView.apply {
            this.clearFocus()
            this.requestFocus()
            this.onActionViewExpanded()
            AppUtil.showSoftKeyboard(requireActivity(), this)
        }
        behavior?.state = BottomSheetBehavior.STATE_EXPANDED
    }

    /** 初始化搜尋 */
    @RequiresApi(Build.VERSION_CODES.O)
    private fun searchInit() {
        binding.fragmentSearch.searchView.setOnQueryTextListener(queryTextListener)
        recycleView = binding.fragmentSearch.recycleView
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
        if (BuildConfig.DEBUG) {
            MyLog.e("textChange")
        }
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
                searchItem(searchData!!)

            }
        } catch (e: Exception) {
            MyLog.e(e.toString())
        }
    }

    private fun filterCCTV(data: SearchHistory) {
        var l = 0
        val searchSet = mutableSetOf<CCTV>()
        while (l < data.roadName.length - 1) {
            val s = data.roadName.substring(l, l + 2)
            if (s.contains("段") || s.contains("路") || s.contains("巷") || s.contains("號") || s.contains(
                    "橋"
                ) || s.contains("街")
            ) {
                l += 1
                continue
            }
            val matchingKeys = mapData.entries.filter { roadData ->
                roadData.key.contains(s)
            }
            if (matchingKeys.isNotEmpty()) {
                val roadList = matchingKeys.map { (name, no) ->
                    if (BuildConfig.DEBUG) {
                        MyLog.e("searchResult:$name")
                    }
                    CCTV(
                        id = null,
                        name = name,
                        url = "https://cctv.bote.gov.taipei:8501/mjpeg/$no"
                    )
                }
                searchSet.addAll(roadList)
            }
            l += 1
        }
        updateAdapterData(searchSet.toMutableList())
    }

    @RequiresApi(Build.VERSION_CODES.O)
    private fun searchItem(data: SearchHistory) {
        filterCCTV(data)
        binding.fragmentHome.favoriteBtn.tag = data
        binding.fragmentHome.favoriteBtn.setOnClickListener(favoriteBtnListener)
        binding.fragmentSearch.searchView.setQuery("", false)
        SyncSpeed.getCityRoadSpeed(this, data.roadId, this)

        binding.fragmentSearch.root.visibility = View.GONE
        binding.fragmentHome.root.visibility = View.VISIBLE
        binding.fragmentHome.textView.text = data.roadName
        viewModel.checkFavorite(data.roadName)

        uiState.isOpen = false
        if (!userInteractionState.noChange) {
            AppUtil.showTopToast(requireActivity(), "搜尋中...")
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

    private fun initSensor() {
        val sensorManager =
            requireActivity().getSystemService(Context.SENSOR_SERVICE) as SensorManager
        val rotationMatrix = FloatArray(9)
        val orientationValues = FloatArray(3)

        val rotationSensor = sensorManager.getDefaultSensor(Sensor.TYPE_ROTATION_VECTOR)

        sensorManager.registerListener(object : SensorEventListener {
            override fun onSensorChanged(event: SensorEvent) {
                if (event.sensor == rotationSensor) {
                    SensorManager.getRotationMatrixFromVector(rotationMatrix, event.values)
                    SensorManager.getOrientation(rotationMatrix, orientationValues)
                    azimuth = Math.toDegrees(orientationValues[0].toDouble()).toFloat()

                }
            }

            override fun onAccuracyChanged(sensor: Sensor?, accuracy: Int) {
                // 当传感器精度变化时的处理
            }
        }, rotationSensor, SensorManager.SENSOR_DELAY_NORMAL)
    }

    /** 設置地圖 */
    override fun onMapReady(googleMap: GoogleMap) {
        initSensor()
        map = googleMap
        map.isTrafficEnabled = true
        mFusedLocationClient =
            LocationServices.getFusedLocationProviderClient(requireActivity())
        map.uiSettings.isMyLocationButtonEnabled = true
        map.uiSettings.isRotateGesturesEnabled = true
        map.setOnMyLocationButtonClickListener(this)
        map.setOnMyLocationClickListener(this)
        enableMyLocation()
        currentLocationInit()
        markInit()
        setLocationInitBtn()
        weatherInit()
        if (destination != null) {
            MyLog.d("開啟導航")
            getNavigation(destination!!)
        }
    }

    @SuppressLint("MissingPermission")
    private fun getNavigation(destination: LatLng) {
        if (!initializationState.permissionDenied) {
            MyLog.d("獲取當前位置1")
            mFusedLocationClient?.lastLocation
                ?.addOnSuccessListener { location: Location? ->
                    MyLog.d("獲取當前位置2")
                    location?.let {
                        MyLog.d("獲取當前位置3")
                        val origin = LatLng(location.latitude, location.longitude)
                        getDestination(origin, destination)
                    }
                }
        }
    }

    private fun getDestination(origin: LatLng, destination: LatLng) {
        val service = NavigationAPI.api
        viewLifecycleOwner.lifecycleScope.launch(Dispatchers.IO) {
            try {
                val response = service?.getDirections(
                    "${origin.latitude},${origin.longitude}",
                    "${destination.latitude},${destination.longitude}",
                    "driving",
                    "AIzaSyDRjDWqLgUb2xrIOzhKNixOOLbn249kAto"
                )
                if (response != null) {
                    val route = response.routes[0]
                    val steps = route.legs[0].steps
                    val routePoints = mutableListOf<LatLng>()
                    for (step in steps) {
                        val points = step.polyline.points
                        val decodedPoints = PolyUtil.decode(points)
                        routePoints.addAll(decodedPoints)
                    }
                    MyLog.d("$routePoints")
                    withContext(Dispatchers.Main) {
                        drawRoute(routePoints)
                    }
                }
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }

    private fun drawRoute(routePoints: List<LatLng>) {
        val lineOptions = PolylineOptions()
        lineOptions.addAll(routePoints)
        lineOptions.width(40f) // 路线宽度
        lineOptions.color(R.color.purple_700) // 路线颜色
        lineOptions.zIndex(20f)
        map.isTrafficEnabled = false
        map.addPolyline(lineOptions)
        MyLog.d("繪製完成")
    }

    /** 定位按鈕位置 */
    @SuppressLint("UseRequireInsteadOfGet")
    private fun setLocationInitBtn() {
        if (childFragmentManager
                .findFragmentById(R.id.map) != null && childFragmentManager
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

    private fun rotateCamera() {
        val currentCameraPosition = map.cameraPosition
        val newCameraPosition = CameraPosition.builder(currentCameraPosition)
            .bearing(azimuth)
            .build()
        Handler(Looper.getMainLooper()).post {
            map.animateCamera(CameraUpdateFactory.newCameraPosition(newCameraPosition))
        }

    }

    /** 初始化位置 */

    private fun currentLocationInit() {
        getLocation{
            if (uiState.weatherState) {
                SyncPosition.weatherLocationApi(
                    this@MapFragment,
                    this@MapFragment,
                    it
                )
            }
            MyLog.e("Start Navigation")
            val targetLatLng =
                LatLng(it.latitude - 0.00006, it.longitude)
            val targetZoomLevel = 18f
            val currentCameraPosition = map.cameraPosition

            val newCameraPosition = CameraPosition.builder(currentCameraPosition)
                .target(targetLatLng)
                .bearing(azimuth)
                .zoom(targetZoomLevel)
                .build()
            Handler(Looper.getMainLooper()).post {
                map.moveCamera(
                    CameraUpdateFactory.newCameraPosition(
                        newCameraPosition
                    )
                )
            }
        }
    }

    /** 通用移動到現在位置 */
    @SuppressLint("MissingPermission")
    private fun moveToCurrentLocation() {
        getLocation{
            MyLog.e("Start Navigation")
            val targetLatLng =
                LatLng(it.latitude - 0.00006, it.longitude)
            val targetZoomLevel = 18f
            val currentCameraPosition = map.cameraPosition

            val newCameraPosition = CameraPosition.builder(currentCameraPosition)
                .target(targetLatLng)
                .bearing(azimuth)
                .zoom(targetZoomLevel)
                .build()
            Handler(Looper.getMainLooper()).post {
                map.animateCamera(
                    CameraUpdateFactory.newCameraPosition(
                        newCameraPosition
                    )
                )

            }
        }
    }
    /** 拿取現在位置 */
    @SuppressLint("MissingPermission")
    private fun getLocation(action:(location:LatLng)->Unit) {
        if (!initializationState.permissionDenied) {
            mFusedLocationClient?.lastLocation
                ?.addOnSuccessListener { location: Location? ->
                    location?.let {
                        action(LatLng(location.latitude, location.longitude))
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
            initializationState.permissionDenied = true
        }
    }

    /** 當失去定位權限錯誤 */
    private fun showMissingPermissionError() {
        PermissionUtils.PermissionDeniedDialog.newInstance(true)
            .show(parentFragmentManager, "dialog")
    }

    /** 批量生成mark */
    private fun markInit() {
        mClusterManager = ClusterManager(context, map)
        mClusterManager?.renderer =
            CustomClusterRenderer(requireActivity(), map, mClusterManager!!)

        viewLifecycleOwner.lifecycleScope.launch(Dispatchers.IO) {
            repeatOnLifecycle(Lifecycle.State.CREATED) {
                SyncPosition.parkingLists.collect {
                    for (p in it) {
                        mClusterManager?.addItem(
                            MyItem(
                                p.latitude,
                                p.longitude,
                                0,
                                p
                            )
                        )
                        delay(100)
                    }
                    withContext(Dispatchers.Main) {
                        SyncCamera.cameraMarkApi()
                    }
                }
            }
        }
        viewLifecycleOwner.lifecycleScope.launch(Dispatchers.IO) {
            repeatOnLifecycle(Lifecycle.State.CREATED) {
                SyncCamera.cameraLists.collect {
                    for (p in it) {
                        mClusterManager?.addItem(
                            MyItem(
                                p.latitude,
                                p.longitude,
                                1, p
                            )
                        )
                        delay(100)
                    }
                    withContext(Dispatchers.Main) {
                        SyncPosition.oilStationApi()
                    }
                }
            }
        }

        viewLifecycleOwner.lifecycleScope.launch(Dispatchers.IO) {
            repeatOnLifecycle(Lifecycle.State.CREATED) {
                SyncPosition.oilStation.collect {
                    for (p in it) {
                        mClusterManager?.addItem(
                            MyItem(
                                p.latitude,
                                p.longitude,
                                2, p
                            )
                        )
                        delay(100)
                    }
                }
            }
        }

        map.setOnCameraIdleListener(mClusterManager)
        SyncPosition.parkingApi()
        mClusterManager?.setOnClusterItemClickListener {
            showInfoWindowForItem(it)
            true
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

    @SuppressLint("SetTextI18n")
    private fun showInfoWindowForItem(item: MyItem<Any>) {
        val targetLatLng = LatLng(item.position.latitude - 0.0005, item.position.longitude)
        val targetZoomLevel = 18f
        val cameraUpdate = CameraUpdateFactory.newLatLngZoom(targetLatLng, targetZoomLevel)
        map.animateCamera(cameraUpdate, 1000, null)
        val title = infoWindowView.findViewById<TextView>(R.id.title)
        val description = infoWindowView.findViewById<TextView>(R.id.description)

        if (item.data::class == Parking::class) {
            val data = item.data as Parking
            viewModel.checkMarkParking(data.parkingName)
            title.text = "停車場: " + data.parkingName
            title.text = "停車場: " + data.parkingName
            description.text = "連結"

            markFavorite.tag = data
            markFavorite.setOnClickListener(parkingMarkListener)
        }
        if (item.data::class == Camera::class) {
            val data = item.data as Camera
            viewModel.checkMarkCamera(data.cameraId)
            title.text = "測速照相: ${data.road}"
            description.text = "限速:${data.limit} "

            markFavorite.tag = data
            markFavorite.setOnClickListener(cameraMarkListener)
        }
        if (item.data::class == OilStation::class) {
            val data = item.data as OilStation
            viewModel.checkMarkOil(data.station)
            title.text = "加油站: ${data.station}"
            description.text = data.address

            markFavorite.tag = data
            markFavorite.setOnClickListener(oilStationMarkListener)
        }

        // 創建 PopupWindow
        val popupWindow = PopupWindow(
            infoWindowView,
            WindowManager.LayoutParams.WRAP_CONTENT,
            WindowManager.LayoutParams.WRAP_CONTENT
        ).apply {
            isOutsideTouchable = true
        }

        popupWindow.showAtLocation(infoWindowView, Gravity.TOP, 0, 300)

    }

    private val parkingMarkListener = View.OnClickListener {
        val data = it.tag as Parking
        val image = it as ImageView
        if (userInteractionState.noChange) {
            viewModel.deleteMarkParking(data.parkingName)
            image.setImageResource(R.drawable.ic_baseline_star_24)
        } else {
            viewModel.addMarkParking(data)
            image.setImageResource(R.drawable.ic_baseline_star_25)
        }
        userInteractionState.noChange = !userInteractionState.noChange
    }
    private val oilStationMarkListener = View.OnClickListener {
        val data = it.tag as OilStation
        val image = it as ImageView
        if (userInteractionState.noChange) {
            viewModel.deleteMarkOil(data.station)
            image.setImageResource(R.drawable.ic_baseline_star_24)
        } else {
            viewModel.addMarkOil(data)
            image.setImageResource(R.drawable.ic_baseline_star_25)
        }
        userInteractionState.noChange = !userInteractionState.noChange
    }
    private val cctvListener = View.OnClickListener {
        val data = binding.spinner.selectedItem as CCTV
        if (userInteractionState.favoriteFlag) {
            viewModel.deleteCCTV(data.name)
            binding.fragmentHome.favoriteBtn.setImageResource(R.drawable.ic_baseline_star_24)
        } else {
            viewModel.addCCTV(data)
            binding.fragmentHome.favoriteBtn.setImageResource(R.drawable.ic_baseline_star_25)
        }
        userInteractionState.favoriteFlag = !userInteractionState.favoriteFlag
    }

    /** API CALLBACK */
    override fun onSuccess(successData: ArrayList<String>) {}

    override fun onError(errorCode: Int, errorData: ArrayList<String>) {}

    override fun doInBackground(result: Int, successData: ArrayList<String>) {}
}