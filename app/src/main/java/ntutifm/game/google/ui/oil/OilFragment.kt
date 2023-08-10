package ntutifm.game.google.ui.oil

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import ntutifm.game.google.MainActivity
import ntutifm.game.google.MyActivity
import ntutifm.game.google.databinding.FragmentOilBinding

class OilFragment : Fragment() {

    private var _binding : FragmentOilBinding? = null
    private val binding get() = _binding!!
    private val viewModel : OilViewModel by lazy {
        ViewModelProvider(this)[OilViewModel::class.java]
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        _binding = FragmentOilBinding.inflate(inflater, container, false)
        return binding.root
    }
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        oilListInit()
    }

    private fun oilListInit() {
        val textView1: TextView = binding.t1
        val textView2: TextView = binding.t2
        viewModel.text.observe(viewLifecycleOwner) {
            textView1.text = it
            textView2.text = it
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}