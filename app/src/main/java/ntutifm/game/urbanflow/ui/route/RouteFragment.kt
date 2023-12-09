package ntutifm.game.urbanflow.ui.route

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.NavOptions
import androidx.navigation.Navigation
import ntutifm.game.urbanflow.R
import ntutifm.game.urbanflow.databinding.FragmentRouteBinding
import ntutifm.game.urbanflow.ui.ShareViewModel

class RouteFragment : Fragment() {

    private var _binding : FragmentRouteBinding? = null
    private val binding get() = _binding!!
    private val shareData: ShareViewModel by activityViewModels()

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        _binding = FragmentRouteBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        shareData.roadFavorite.value = null
        shareData.cctv.value = null
        shareData.roadFavorite.value = null
        val navController = Navigation.findNavController(binding.root)
        val navOptions = NavOptions.Builder()
            .setPopUpTo(R.id.routeFragment, false)
            .build()
        binding.navRoad.setOnClickListener{
            navController.navigate(R.id.roadFragment, null, navOptions)
        }
        binding.navPark.setOnClickListener{
            navController.navigate(R.id.parkingFragment, null, navOptions)
        }
        binding.navOilStation.setOnClickListener{
            navController.navigate(R.id.oilStationFragment, null, navOptions)
        }
        binding.navCctv.setOnClickListener{
            navController.navigate(R.id.CCTVFragment, null, navOptions)
        }
        binding.navCamera.setOnClickListener{
            navController.navigate(R.id.cameraFragment, null, navOptions)
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
