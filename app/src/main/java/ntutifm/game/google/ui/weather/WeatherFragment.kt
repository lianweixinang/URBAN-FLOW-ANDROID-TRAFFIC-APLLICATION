package ntutifm.game.google.ui.weather

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import ntutifm.game.google.databinding.FragmentWeatherBinding

class WeatherFragment : Fragment() {

    private var _binding:FragmentWeatherBinding? = null

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val weatherViewModel =
            ViewModelProvider(this).get(WeatherViewModel::class.java)

        _binding =FragmentWeatherBinding.inflate(inflater, container, false)

        val root: View = binding.root
        val textView: TextView = binding.textView
        weatherViewModel.text.observe(viewLifecycleOwner) {
            textView.text = it

        }
        return root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}