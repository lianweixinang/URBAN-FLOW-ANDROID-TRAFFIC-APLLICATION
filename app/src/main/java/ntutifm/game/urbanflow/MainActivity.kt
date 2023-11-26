package ntutifm.game.urbanflow

import android.app.Activity
import android.graphics.Color
import android.os.Build
import android.os.Bundle
import android.view.*
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.GravityCompat
import androidx.navigation.NavOptions
import androidx.navigation.fragment.NavHostFragment
import com.google.android.material.navigation.NavigationView
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.SupervisorJob
import ntutifm.game.urbanflow.databinding.ActivityMainBinding


open class MainActivity : AppCompatActivity(),
    NavigationView.OnNavigationItemSelectedListener {

    private lateinit var binding: ActivityMainBinding

    @RequiresApi(Build.VERSION_CODES.R)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        setNavigationViewListener()
        val window = window

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
            // For Android 11 (API Level 30) and above
            val insetsController = window.insetsController
            insetsController?.setSystemBarsAppearance(
                WindowInsetsController.APPEARANCE_LIGHT_STATUS_BARS or WindowInsetsController.APPEARANCE_LIGHT_NAVIGATION_BARS, // Use light status and navigation bar icons
                WindowInsetsController.APPEARANCE_LIGHT_STATUS_BARS or WindowInsetsController.APPEARANCE_LIGHT_NAVIGATION_BARS // Apply to both status and navigation bars
            )
        } else {
            // For Android 8 (Oreo) to Android 10
            var uiVisibility = window.decorView.systemUiVisibility
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                uiVisibility = uiVisibility or View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR // For status bar
            }
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                uiVisibility = uiVisibility or View.SYSTEM_UI_FLAG_LIGHT_NAVIGATION_BAR // For navigation bar
            }
            window.decorView.systemUiVisibility = uiVisibility
        }
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        // Inflate the menu; this adds items to the action bar if it is present.
        menuInflater.inflate(R.menu.menulayout, menu)
        return true
    }

    override fun onNavigationItemSelected(item: MenuItem): Boolean {
        val navHostFragment =
            supportFragmentManager.findFragmentById(R.id.fragmentContainerView) as NavHostFragment
        val navController = navHostFragment.navController
        val navOptions = NavOptions.Builder()
            .setPopUpTo(R.id.mapFragment, true)
            .build()
        val bundle = Bundle()
        bundle.putBoolean("notReset", true)
        when (item.itemId) {
            R.id.nav_map -> {
                navController.navigate(R.id.mapFragment, bundle, navOptions)
            }
            R.id.nav_oil -> {
                navController.navigate(R.id.oilFragment, bundle, navOptions)
            }
            R.id.nav_weather -> {
                navController.navigate(R.id.weatherFragment, bundle, navOptions)
            }
            R.id.nav_route -> {
                navController.navigate(R.id.routeFragment, bundle, navOptions)
            }
            R.id.nav_notification -> {
                navController.navigate(R.id.notificationFragment, bundle, navOptions)
            }
            R.id.nav_about -> {
                navController.navigate(R.id.aboutFragment, bundle, navOptions)
            }
        }
        //AppUtil.startFragment(supportFragmentManager, R.id.fragmentMap, MapFragment())

        binding.drawerLayout1.closeDrawer(GravityCompat.START)
        return true
    }

    private fun setNavigationViewListener() {
        val navigationView = findViewById<NavigationView>(R.id.nav_view)
        navigationView.setNavigationItemSelectedListener(this)
    }

    companion object {
        const val LOCATION_PERMISSION_REQUEST_CODE = 1
        val applicationScope = CoroutineScope(SupervisorJob())
        private const val TAG = "MPlusApplication"
        private val activityStatus: MutableList<String> = ArrayList()

        //判斷當前Activity是否在前景
        @JvmStatic
        val isMainActivityForeground: Boolean
            get() = isActivityForeground(MainActivity::class.java)


        @JvmStatic
        fun isActivityForeground(activityClass: Class<out Activity?>): Boolean {
            return activityStatus.contains(activityClass.name)
        }
    }
}

class MyActivity : MainActivity() {

    val context = this@MyActivity
}
