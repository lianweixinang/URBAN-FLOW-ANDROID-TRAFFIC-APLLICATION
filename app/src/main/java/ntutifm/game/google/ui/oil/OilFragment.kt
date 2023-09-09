package ntutifm.game.google.ui.oil

import android.os.Build
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.RequiresApi
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.RecyclerView
import ntutifm.game.google.databinding.FragmentOilBinding
import ntutifm.game.google.entity.SyncOil
import ntutifm.game.google.entity.adaptor.OilAdaptor
import ntutifm.game.google.global.MyLog
import ntutifm.game.google.net.ApiCallBack
import kotlin.math.roundToInt

class OilFragment : Fragment(), ApiCallBack {

    private var _binding : FragmentOilBinding? = null
    private val binding get() = _binding!!
    private var recycleView : RecyclerView? = null
    private var adaptor : OilAdaptor? = null

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
            binding.t6.text=it[1].ninetwo
            binding.t7.text=it[1].ninefive
            binding.t8.text=it[1].nineeight
            binding.t9.text=it[1].superoil
            binding.t10.text= (((it[1].ninetwo.toFloat() - it[0].ninetwo.toFloat())*100.0).roundToInt()/100.0).toString()+"元"
            binding.t11.text= (((it[1].ninefive.toFloat() - it[0].ninefive.toFloat())*100.0).roundToInt()/100.0).toString()+"元"
            binding.t12.text= (((it[1].nineeight.toFloat() - it[0].nineeight.toFloat())*100.0).roundToInt()/100.0).toString()+"元"
            binding.t13.text= (((it[1].superoil.toFloat() - it[0].superoil.toFloat())*100.0).roundToInt()/100.0).toString()+"元"
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