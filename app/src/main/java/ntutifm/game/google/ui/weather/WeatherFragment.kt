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

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        _binding = FragmentWeatherBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        weatherTextInit()
    }

    private fun weatherTextInit() {

        SyncWeather.weatherLists.observe(viewLifecycleOwner) {
            MyLog.e("Weather Updated")
            binding.position.text = it[0].locationName
            binding.nowWeather.text = it[0].t1
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