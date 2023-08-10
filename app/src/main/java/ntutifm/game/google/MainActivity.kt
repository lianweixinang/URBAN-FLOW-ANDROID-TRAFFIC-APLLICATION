package ntutifm.game.google

import android.app.Activity
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.GravityCompat
import androidx.fragment.app.Fragment
import androidx.lifecycle.MutableLiveData
import com.google.android.material.navigation.NavigationView
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.SupervisorJob
import ntutifm.game.google.databinding.ActivityMainBinding
import ntutifm.game.google.net.ApiClass.City
import ntutifm.game.google.net.RetrofitManager
import ntutifm.game.google.ui.home.HomeFragment
import ntutifm.game.google.ui.map.MapFragment
import ntutifm.game.google.ui.oil.OilFragment
import ntutifm.game.google.ui.route.RouteFragment
import ntutifm.game.google.ui.search.SearchFragment
import ntutifm.game.google.ui.weather.WeatherFragment
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

val isOpen: MutableLiveData<Boolean> = MutableLiveData<Boolean>(false)

open class MainActivity : AppCompatActivity(),
    NavigationView.OnNavigationItemSelectedListener{

    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        val fragment = MapFragment()
        val transaction = supportFragmentManager.beginTransaction()
        transaction?.replace(R.id.fragment_main, fragment)
        transaction?.commit()
        setNavigationViewListener()
        //豬頭2
    }

    fun api(){
        val myAPIService = RetrofitManager.getInstance().api
        val call: Call<List<City>>? = myAPIService.cityList
        Log.d("cityName", "Null")
        call!!.enqueue(object : Callback<List<City>> {
            override fun onResponse(
                call: Call<List<City>>?,
                response: Response<List<City>>?
            ) {
                if(response?.body()!=null){
                    Log.d("cityName", response?.body()!![0].cityName)
                }else{
                    Log.d("cityName", "Null")
                }
            }
            override fun onFailure(call: Call<List<City>>?, t: Throwable?) {
                Log.d("titlecity", t.toString())
            }
        })
    }
    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        // Inflate the menu; this adds items to the action bar if it is present.
        menuInflater.inflate(R.menu.menulayout, menu)
        return true
    }
    override fun onNavigationItemSelected(item: MenuItem): Boolean {
        var fragment:Fragment
        when (item.itemId) {
             R.id.nav_home -> {
                 fragment = HomeFragment()
                 val transaction = supportFragmentManager.beginTransaction()
                 transaction?.replace(R.id.fragment_main, fragment)
                 transaction?.commit()
            }
            R.id.nav_search -> {
                fragment = SearchFragment() //新視窗
                val transaction = supportFragmentManager.beginTransaction()
                transaction?.replace(R.id.fragment_main, fragment)
                transaction?.commit()
            }
            R.id.nav_map -> {
                val fragment = MapFragment()
                val transaction = supportFragmentManager.beginTransaction()
                transaction?.replace(R.id.fragment_main, fragment)
                transaction?.commit()
            }
            R.id.nav_oil -> {
                val fragment = OilFragment()
                val transaction = supportFragmentManager.beginTransaction()
                transaction?.replace(R.id.fragment_main, fragment)
                transaction?.commit()
            }
            R.id.nav_weather -> {
                val fragment = WeatherFragment()
                val transaction = supportFragmentManager.beginTransaction()
                transaction?.replace(R.id.fragment_main, fragment)
                transaction?.commit()
            }
            R.id.nav_route -> {
                val fragment = RouteFragment()
                val transaction = supportFragmentManager.beginTransaction()
                transaction?.replace(R.id.fragment_main, fragment)
                transaction?.commit()
            }

        }

        //close navigation drawer
        binding.drawerLayout.closeDrawer(GravityCompat.START)
        return true
    }
    //            R.id.nav_run -> {val intent = Intent(this, SecondActivity::class.java)
    //                startActivity(intent)}
    private fun setNavigationViewListener() {
        val navigationView = findViewById<NavigationView>(R.id.nav_view) as NavigationView
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
