package ntutifm.game.google.ui.route

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
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
        textInit()
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
