package ntutifm.game.google

import android.app.Activity
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.GravityCompat
import androidx.lifecycle.MutableLiveData
import androidx.navigation.Navigation
import androidx.navigation.fragment.NavHostFragment
import com.google.android.material.navigation.NavigationView
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.SupervisorJob
import ntutifm.game.google.databinding.ActivityMainBinding
import ntutifm.game.google.global.AppUtil
import ntutifm.game.google.ui.map.MapFragment
import ntutifm.game.google.ui.notification.NotificationFragment
import ntutifm.game.google.ui.oil.AboutFragment
import ntutifm.game.google.ui.oil.InstructionFragment
import ntutifm.game.google.ui.oil.OilFragment
import ntutifm.game.google.ui.route.RouteFragment
import ntutifm.game.google.ui.weather.WeatherFragment
open class MainActivity : AppCompatActivity(),
    NavigationView.OnNavigationItemSelectedListener{

    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        setNavigationViewListener()
//        AppUtil.startFragment(supportFragmentManager, R.id.fragmentMap, MapFragment())
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        // Inflate the menu; this adds items to the action bar if it is present.
        menuInflater.inflate(R.menu.menulayout, menu)
        return true
    }
    override fun onNavigationItemSelected(item: MenuItem): Boolean {
        val navHostFragment = supportFragmentManager.findFragmentById(R.id.fragmentContainerView) as NavHostFragment
        val navController = navHostFragment.navController
        when (item.itemId) {
            R.id.nav_map -> {
                navController.navigate(R.id.mapFragment)
//                AppUtil.startFragment(supportFragmentManager, R.id.fragmentMap, MapFragment())
            }
            R.id.nav_oil -> {
                navController.navigate(R.id.oilFragment)
            }
            R.id.nav_weather -> {
                navController.navigate(R.id.weatherFragment)
            }
            R.id.nav_route -> {
                navController.navigate(R.id.routeFragment)
            }
            R.id.nav_notification -> {
                navController.navigate(R.id.notificationFragment)
            }
            R.id.nav_about -> {
                navController.navigate(R.id.aboutFragment)
            }
            R.id.nav_instruction -> {
                navController.navigate(R.id.instructionFragment)
            }
        }

        binding.drawerLayout1.closeDrawer(GravityCompat.START)
        return true
    }

    private fun setNavigationViewListener() {
        val navigationView = findViewById(R.id.nav_view) as NavigationView
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
