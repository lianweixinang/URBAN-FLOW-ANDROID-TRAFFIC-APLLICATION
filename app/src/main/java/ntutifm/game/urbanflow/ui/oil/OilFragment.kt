package ntutifm.game.urbanflow.ui.oil

import android.os.Build
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.RequiresApi
import androidx.fragment.app.Fragment
import ntutifm.game.urbanflow.apiClass.Oil
import ntutifm.game.urbanflow.databinding.FragmentOilBinding
import ntutifm.game.urbanflow.entity.sync.SyncOil
import ntutifm.game.urbanflow.global.MyLog
import ntutifm.game.urbanflow.net.ApiCallBack
import java.time.DayOfWeek
import java.time.LocalDate
import kotlin.math.roundToInt

class OilFragment : Fragment(), ApiCallBack {

    private var _binding : FragmentOilBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        _binding = FragmentOilBinding.inflate(inflater, container, false)
        return binding.root
    }

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        oilListInit()
    }

    @RequiresApi(Build.VERSION_CODES.O)
    private fun oilListInit(){
        SyncOil.oilLists.observe(viewLifecycleOwner){
            when (LocalDate.now().dayOfWeek) {
                DayOfWeek.FRIDAY, DayOfWeek.SATURDAY, DayOfWeek.SUNDAY -> {
                    applyWeekendEffect(it)
                }
                DayOfWeek.MONDAY, DayOfWeek.TUESDAY, DayOfWeek.WEDNESDAY, DayOfWeek.THURSDAY -> {
                    applyWeekdayEffect(it[0])
                }
                else ->{}
            }
            MyLog.e("oilUpdate")
        }
        SyncOil.getOil(this,this)
    }
    private fun applyWeekdayEffect(it: Oil){
        binding.t1.text = it.nineTwo
        binding.t2.text = it.nineFive
        binding.t3.text = it.nineEight
        binding.t4.text = it.superOil

        binding.t6.text = ""
        binding.t7.text = "下 周 油 價"
        binding.t8.text = ""
        binding.t9.text = ""

        binding.t10.text = ""
        binding.t11.text = "      尚 未 公 布"
        binding.t12.text = ""
        binding.t13.text = ""
    }

    private fun applyWeekendEffect(it: List<Oil>){
        it[1].apply {
            binding.t1.text = this.nineTwo
            binding.t2.text = this.nineFive
            binding.t3.text = this.nineEight
            binding.t4.text = this.superOil
        }

        it[0].apply {
            binding.t6.text = this.nineTwo
            binding.t7.text = this.nineFive
            binding.t8.text = this.nineEight
            binding.t9.text = this.superOil
        }

        val previous = it[1]
        val current = it[0]

        val values = listOf(
            current.nineTwo.toFloat() - previous.nineTwo.toFloat(),
            current.nineFive.toFloat() - previous.nineFive.toFloat(),
            current.nineEight.toFloat() - previous.nineEight.toFloat(),
            current.superOil.toFloat() - previous.superOil.toFloat()
        )

        val textViews = listOf(binding.t10, binding.t11, binding.t12, binding.t13)

        values.zip(textViews) { value, textView ->
            textView.text = convertTo(value)
        }

    }

    private fun convertTo(value:Float):String{
        return if (value >= 0) {
            "+" + ((value * 100.0).roundToInt() / 100.0).toString() + "元"
        } else {
            ((value * 100.0).roundToInt() / 100.0).toString() + "元"
        }
    }
    override fun onSuccess(successData: ArrayList<String>){}

    override fun onError(errorCode: Int, errorData: ArrayList<String>){}

    override fun doInBackground(result: Int, successData: ArrayList<String>){}

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}