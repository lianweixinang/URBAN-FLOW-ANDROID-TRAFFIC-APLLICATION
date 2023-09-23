package ntutifm.game.google.ui.oil

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import ntutifm.game.google.databinding.FragmentAboutBinding
import ntutifm.game.google.databinding.FragmentInstructionBinding
import ntutifm.game.google.net.ApiCallBack

class InstructionFragment : Fragment(), ApiCallBack {

    private var _binding : FragmentInstructionBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        _binding = FragmentInstructionBinding.inflate(inflater, container, false)
        return binding.root
    }


    override fun onSuccess(successData: ArrayList<String>){}

    override fun onError(errorCode: Int, errorData: ArrayList<String>){}

    override fun doInBackground(result: Int, successData: ArrayList<String>){}


    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}