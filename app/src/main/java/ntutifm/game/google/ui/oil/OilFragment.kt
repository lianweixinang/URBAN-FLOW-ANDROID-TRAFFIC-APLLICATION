package ntutifm.game.google.ui.oil

import android.os.Build
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.RequiresApi
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import ntutifm.game.google.MyActivity
import ntutifm.game.google.databinding.FragmentOilBinding
import ntutifm.game.google.entity.SyncOil
import ntutifm.game.google.entity.adaptor.OilAdaptor
import ntutifm.game.google.entity.dbDisplayHistory
import ntutifm.game.google.global.MyLog
import ntutifm.game.google.net.ApiCallBack
import ntutifm.game.google.ui.oil.OilViewModel

class OilFragment : Fragment(), ApiCallBack {

    private var _binding : FragmentOilBinding? = null
    private val binding get() = _binding!!
    private var recycleView : RecyclerView? = null
    private var adaptor : OilAdaptor? = null
    private val viewModel : OilViewModel by lazy {
        ViewModelProvider(this)[OilViewModel::class.java]
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        _binding = FragmentOilBinding.inflate(inflater, container, false)
        return binding.root
    }
    private fun oilListInit(){}

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        oilListInit()
        SyncOil.oilLists.observe(viewLifecycleOwner){
            MyLog.e("oilUpdate")
            binding.t1.text=it[0].ninetwo
            binding.t2.text=it[0].ninefive
            binding.t3.text=it[0].nineeight
            binding.t4.text=it[0].superoil
        }
        SyncOil.getOil(this,this)

    }
    override fun onSuccess(successData: ArrayList<String>){}

    override fun onError(errorCode: Int, errorData: ArrayList<String>){}

    override fun doInBackground(result: Int, successData: ArrayList<String>){}

//    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
//        super.onViewCreated(view, savedInstanceState)
//        oilListInit()
//    }
//
//    private fun oilListInit() {
//        val textView1: TextView = binding.t1
//        viewModel.text.observe(viewLifecycleOwner) {
//            textView1.text = it
//        }
//    }


    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}