package ntutifm.game.google.ui.weather

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import ntutifm.game.google.R
import ntutifm.game.google.databinding.FragmentWeatherBinding
import ntutifm.game.google.entity.sync.SyncPosition
import ntutifm.game.google.entity.sync.SyncWeather
import ntutifm.game.google.global.MyLog
import ntutifm.game.google.net.ApiCallBack

class WeatherFragment : Fragment(), ApiCallBack {

    private var _binding: FragmentWeatherBinding? = null
    private val binding get() = _binding!!
    private var lat: Double? = null
    private var long: Double? = null

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        _binding = FragmentWeatherBinding.inflate(inflater, container, false)
        arguments?.let { bundle ->
            // 使用 Bundle 中的方法來獲取數據
            lat = bundle.getDouble("lat")
            long = bundle.getDouble("long")
            MyLog.d(lat.toString() + ", " + long.toString())
        }
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        weatherTextInit()
    }

    private fun weatherTextInit() {
        SyncWeather.weatherLists.observe(viewLifecycleOwner) {
            val index = SyncPosition.districtToIndex()
            when (it[index].weatherDescription) {
                "晴天" -> binding.weatherIcon.setImageResource(R.drawable.sun)
                "雨天" -> binding.weatherIcon.setImageResource(R.drawable.heavy_rain)
            }

            MyLog.e("Weather Updated")
            binding.position.text = it[index].locationName
            binding.nowWeather.text = it[index].t1 + "℃"
            binding.nowWeatherText.text = it[index].wx1
            binding.weatherText1.text = it[index].t1 + "℃"
            binding.weatherText2.text = it[index].t2 + "℃"
            binding.weatherText3.text = it[index].t3 + "℃"
            binding.weatherText4.text = it[index].t4 + "℃"
            binding.weatherText5.text = it[index].t5 + "℃"
            binding.weatherText6.text = it[index].t6 + "℃"
            binding.weatherText7.text = it[index].t7 + "℃"
            binding.weatherText8.text = it[index].t8 + "℃"
            binding.rainText1.text = it[index].pop6h1 + "%"
            binding.rainText2.text = it[index].pop6h1 + "%"
            binding.rainText3.text = it[index].pop6h2 + "%"
            binding.rainText4.text = it[index].pop6h2 + "%"
            binding.rainText5.text = it[index].pop6h3 + "%"
            binding.rainText6.text = it[index].pop6h3 + "%"
            binding.rainText7.text = it[index].pop6h4 + "%"
            binding.rainText8.text = it[index].pop6h4 + "%"

        }
        SyncWeather.weatherDataApi(this, this)
    }

    override fun onSuccess(successData: ArrayList<String>) {}

    override fun onError(errorCode: Int, errorData: ArrayList<String>) {}

    override fun doInBackground(result: Int, successData: ArrayList<String>) {}

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}