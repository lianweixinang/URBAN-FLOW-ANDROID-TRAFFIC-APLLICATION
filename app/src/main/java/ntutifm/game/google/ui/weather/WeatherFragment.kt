package ntutifm.game.google.ui.weather

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import ntutifm.game.google.databinding.FragmentWeatherBinding
import ntutifm.game.google.entity.SyncWeather
import ntutifm.game.google.global.MyLog
import ntutifm.game.google.net.ApiCallBack

class WeatherFragment : Fragment(), ApiCallBack {

    private var _binding: FragmentWeatherBinding? = null
    private val binding get() = _binding!!
    private var lat:Double? = null
    private var long:Double? = null

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
            MyLog.d(lat.toString()+", "+long.toString())
        }
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        weatherTextInit()
    }

    private fun weatherTextInit() {
        SyncWeather.weatherLists.observe(viewLifecycleOwner) {
            //it的第幾筆是地區，根據查表來拿index

            MyLog.e("Weather Updated")
            binding.position.text = it[0].locationName
            binding.nowWeather.text = it[0].t1+"℃"
            binding.nowWeatherText.text = it[0].wx1
            binding.weatherText1.text = it[0].t1+"℃"
            binding.weatherText2.text = it[0].t2+"℃"
            binding.weatherText3.text = it[0].t3+"℃"
            binding.weatherText4.text = it[0].t4+"℃"
            binding.weatherText5.text = it[0].t5+"℃"
            binding.weatherText6.text = it[0].t6+"℃"
            binding.weatherText7.text = it[0].t7+"℃"
            binding.weatherText8.text = it[0].t8+"℃"
            binding.rainText1.text= it[0].pop6h1+"%"
            binding.rainText2.text= it[0].pop6h1+"%"
            binding.rainText3.text= it[0].pop6h2+"%"
            binding.rainText4.text= it[0].pop6h2+"%"
            binding.rainText5.text= it[0].pop6h3+"%"
            binding.rainText6.text= it[0].pop6h3+"%"
            binding.rainText7.text= it[0].pop6h4+"%"
            binding.rainText8.text= it[0].pop6h4+"%"

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