package ntutifm.game.google.ui.weather

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import ntutifm.game.google.databinding.FragmentWeatherBinding

class WeatherFragment : Fragment() {

    private var _binding:FragmentWeatherBinding? = null
    private val binding get() = _binding!!
    private val weatherViewModel : WeatherViewModel by lazy{
        ViewModelProvider(this)[WeatherViewModel::class.java]
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        _binding = FragmentWeatherBinding.inflate(inflater, container, false)
        return binding.root
    }
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        weatherTextInit()
    }
    private fun weatherTextInit(){
        weatherViewModel.text.observe(viewLifecycleOwner) {
            binding.position
        }
    }
    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}