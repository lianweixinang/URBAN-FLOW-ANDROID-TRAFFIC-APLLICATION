package ntutifm.game.google.ui.search.oil

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import ntutifm.game.google.databinding.FragmentOilBinding

class OilFragment : Fragment() {

    private var _binding:FragmentOilBinding? = null

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val oilViewModel =
            ViewModelProvider(this).get(OilViewModel::class.java)

        _binding =FragmentOilBinding.inflate(inflater, container, false)

        val root: View = binding.root
        val textView1: TextView = binding.t1
        val textView2: TextView = binding.t2
        oilViewModel.text.observe(viewLifecycleOwner) {
            textView1.text = it
            textView2.text = it

        }
        return root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}