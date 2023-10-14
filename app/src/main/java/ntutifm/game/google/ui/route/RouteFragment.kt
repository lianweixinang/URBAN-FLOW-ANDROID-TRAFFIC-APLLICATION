package ntutifm.game.google.ui.route

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.NavOptions
import androidx.navigation.Navigation
import ntutifm.game.google.R
import ntutifm.game.google.databinding.FragmentRouteBinding
import ntutifm.game.google.ui.weather.RouteViewModel

class RouteFragment : Fragment() {

    private var _binding : FragmentRouteBinding? = null
    private val binding get() = _binding!!
    private val viewModel : RouteViewModel by lazy{
        ViewModelProvider(this)[RouteViewModel::class.java]
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        _binding = FragmentRouteBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.navRoad.setOnClickListener{
            val navController = Navigation.findNavController(binding.root)
            val navOptions = NavOptions.Builder()
                .setPopUpTo(R.id.mapFragment, true)
                .build()
            val bundle = Bundle()
            bundle.putBoolean("notReset", true)
            navController.navigate(R.id.roadFragment, bundle, navOptions)
        }
        binding.navPark.setOnClickListener{
            val navController = Navigation.findNavController(binding.root)
            val navOptions = NavOptions.Builder()
                .setPopUpTo(R.id.mapFragment, true)
                .build()
            val bundle = Bundle()
            bundle.putBoolean("notReset", true)
            navController.navigate(R.id.parkingFragment, bundle, navOptions)
        }
        binding.navOilStation.setOnClickListener{
            val navController = Navigation.findNavController(binding.root)
            val navOptions = NavOptions.Builder()
                .setPopUpTo(R.id.mapFragment, true)
                .build()
            val bundle = Bundle()
            bundle.putBoolean("notReset", true)
            navController.navigate(R.id.oilStationFragment, bundle, navOptions)
        }
        binding.navCctv.setOnClickListener{
            val navController = Navigation.findNavController(binding.root)
            val navOptions = NavOptions.Builder()
                .setPopUpTo(R.id.mapFragment, true)
                .build()
            val bundle = Bundle()
            bundle.putBoolean("notReset", true)
            navController.navigate(R.id.CCTVFragment, bundle, navOptions)
        }
        binding.navCamera.setOnClickListener{
            val navController = Navigation.findNavController(binding.root)
            val navOptions = NavOptions.Builder()
                .setPopUpTo(R.id.mapFragment, true)
                .build()
            val bundle = Bundle()
            bundle.putBoolean("notReset", true)
            navController.navigate(R.id.cameraFragment, bundle, navOptions)
        }
    }

    private fun textInit(){
       viewModel.text.observe(viewLifecycleOwner) {
            binding.textView.text = it
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
