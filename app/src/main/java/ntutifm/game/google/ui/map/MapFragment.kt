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
import android.widget.ImageView
import android.widget.RelativeLayout
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import com.google.android.gms.location.*
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.BitmapDescriptorFactory
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.Marker
import com.google.android.gms.maps.model.MarkerOptions
import com.google.maps.android.clustering.ClusterManager
import ntutifm.game.google.*
import ntutifm.game.google.R
import ntutifm.game.google.databinding.FragmentMapBinding
import ntutifm.game.google.entity.MyItem
import ntutifm.game.google.entity.MyItemReader
import ntutifm.game.google.ui.search.SearchFragment
import org.json.JSONException
import java.io.InputStream


class MapFragment : Fragment() , GoogleMap.OnMyLocationButtonClickListener,
    GoogleMap.OnMyLocationClickListener, OnMapReadyCallback,
    ActivityCompat.OnRequestPermissionsResultCallback {
    private var permissionDenied = false
    private lateinit var map: GoogleMap
    internal var mCurrLocationMarker: Marker? = null
    internal var mFusedLocationClient: FusedLocationProviderClient? = null
    internal var mLastLocation : Location? = null
    private var _binding: FragmentMapBinding? = null
    private lateinit var mLocationRequest: LocationRequest
    private val binding get() = _binding!!
    private var mClusterManager:ClusterManager<MyItem>? = null

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        _binding = FragmentMapBinding.inflate(inflater, container, false)
        val mapFragment =
            childFragmentManager.findFragmentById(R.id.map) as SupportMapFragment?
        mapFragment?.getMapAsync(this)
        val search: ImageView = binding.fragmentHome.button7
        search.setOnClickListener {
            val fragment = SearchFragment()
            val transaction = parentFragmentManager?.beginTransaction()
            transaction?.replace(R.id.fragment_home, fragment)
            transaction?.commit()
        }


        val root: View = binding.root
        return root
    }


    @SuppressLint("UseRequireInsteadOfGet")
    override fun onMapReady(googleMap: GoogleMap) {
        map = googleMap
        map.isTrafficEnabled =true
        enableMyLocation()
        mFusedLocationClient = LocationServices.getFusedLocationProviderClient(activity!!)
//        val sydney = LatLng(-34.0, 151.0)
//
//        map.addMarker(MarkerOptions()
//            .position(sydney)
//            .title("Marker in Sydney"))
//        map.moveCamera(CameraUpdateFactory.newLatLng(sydney))
        map.uiSettings.isMyLocationButtonEnabled = true
        initMark()
        if (childFragmentManager
                .findFragmentById(R.id.map)!=null && childFragmentManager
                .findFragmentById(R.id.map)!!.view!!.findViewById<View>("1".toInt()) != null) {
            val locationButton : View =
                (childFragmentManager
                    .findFragmentById(R.id.map)!!.view!!.findViewById<View>("1".toInt()).parent as View).findViewById<View>("2".toInt())
            val rlp : RelativeLayout.LayoutParams = locationButton.layoutParams as RelativeLayout.LayoutParams

            rlp.addRule(RelativeLayout.ALIGN_PARENT_TOP, 0)
            rlp.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM, RelativeLayout.TRUE)
            rlp.setMargins(0, 0, 30, 30)
            map.setOnMyLocationButtonClickListener(this)
            map.setOnMyLocationClickListener(this)

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


    internal var mLocationCallback: LocationCallback = object : LocationCallback() {
        override fun onLocationResult(locationResult: LocationResult) {
            val locationList = locationResult.locations
            Log.d("pppp3", "hi")
            if (locationList.isNotEmpty()) {
                val location = locationList.last()
                Log.i("MapsActivity", "Location: " + location.latitude + " " + location.longitude)
                mLastLocation = location
                if (mCurrLocationMarker != null) {
                    mCurrLocationMarker?.remove()
                }

                val latLng = LatLng(location.latitude, location.longitude)
                val markerOptions = MarkerOptions()
                markerOptions.position(latLng)
                markerOptions.title("Current Position")
                markerOptions.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_MAGENTA))
                mCurrLocationMarker = map.addMarker(markerOptions)

                map.moveCamera(CameraUpdateFactory.newLatLngZoom(latLng, 11.0F))
            }
        }
    }

    @SuppressLint("MissingPermission")
    override fun onMyLocationButtonClick(): Boolean {
        Toast.makeText(activity, "MyLocation button clicked!!!", Toast.LENGTH_SHORT)
            .show()
        mLocationRequest = LocationRequest()
        mLocationRequest.interval = 120000 // two minute interval
        mLocationRequest.fastestInterval = 120000
        mLocationRequest.priority = LocationRequest.PRIORITY_BALANCED_POWER_ACCURACY
        Log.d("pppp", permissionDenied.toString())
        if(!permissionDenied){
            mFusedLocationClient?.requestLocationUpdates(mLocationRequest, mLocationCallback, Looper.myLooper())
        }else{
            enableMyLocation()
        }
        return false
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

        readItems()
        mClusterManager?.setOnClusterItemClickListener { item ->
            false
        }
    }

    @Throws(JSONException::class)
    private fun readItems() {
        val inputStream: InputStream = resources.openRawResource(R.raw.radar_search)
        val items: List<MyItem> = MyItemReader().read(inputStream)
        for (i in 0..9) {
            val offset = i / 60.0
            for (item in items) {
                val position: LatLng = item.getPosition()
                val lat = position.latitude + offset
                val lng = position.longitude + offset
                val offsetItem = MyItem(lat, lng)
                mClusterManager?.addItem(offsetItem)
            }
        }
    }

}