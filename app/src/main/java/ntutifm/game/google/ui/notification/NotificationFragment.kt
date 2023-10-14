package ntutifm.game.google.ui.notification

import android.annotation.SuppressLint
import android.content.Context
import android.os.Build
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.*
import android.widget.ImageButton
import android.widget.PopupWindow
import android.widget.TextView
import androidx.annotation.RequiresApi
import androidx.cardview.widget.CardView
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.NavOptions
import androidx.navigation.Navigation
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.bottomsheet.BottomSheetBehavior
import ntutifm.game.google.MyActivity
import ntutifm.game.google.R
import ntutifm.game.google.apiClass.Incident
import ntutifm.game.google.databinding.FragmentNotificationBinding
import ntutifm.game.google.entity.sync.SyncIncident
import ntutifm.game.google.entity.adaptor.NotificationAdaptor
import ntutifm.game.google.entity.sync.SyncPosition
import ntutifm.game.google.entity.sync.SyncWeather
import ntutifm.game.google.global.AppUtil
import ntutifm.game.google.global.MyLog
import ntutifm.game.google.net.ApiCallBack
import ntutifm.game.google.ui.oil.NotificationViewModel

class NotificationFragment : Fragment(), ApiCallBack {

    private var _binding: FragmentNotificationBinding? = null
    private val binding get() = _binding!!
    private var recycleView: RecyclerView? = null
    private var adaptor: NotificationAdaptor? = null
    private val viewModel: NotificationViewModel by lazy {
        ViewModelProvider(this)[NotificationViewModel::class.java]
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentNotificationBinding.inflate(inflater, container, false)
        return binding.root
    }

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        incidentListInit()

        notificationTextInit()

    }

    @RequiresApi(Build.VERSION_CODES.O)
    private fun incidentListInit() {
        recycleView = binding.recycleIncidentView
        recycleView?.setHasFixedSize(true)
        recycleView?.layoutManager = LinearLayoutManager(MyActivity().context)
        adaptor = NotificationAdaptor(listOf(), incidentBtnListener)
        recycleView?.adapter = adaptor
    }

    @SuppressLint("NotifyDataSetChanged")
    private fun notificationTextInit() {
        SyncIncident.incidentLists.observe(viewLifecycleOwner) {
            MyLog.e("updateIncident")
            adaptor?.setFilteredList(it)
            adaptor?.notifyDataSetChanged()
        }
        SyncIncident.getIncident(this, this)
    }

    private val incidentBtnListener = View.OnClickListener() {
        val inflater = requireActivity().getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
        val popupView = inflater.inflate(R.layout.fragment_details_notification, null)
        val detailTitle = popupView.findViewById<TextView>(R.id.detail_title1)
        val detailState = popupView.findViewById<TextView>(R.id.detail_state1)
        val detailSource = popupView.findViewById<TextView>(R.id.detail_source1)
        val detailTime = popupView.findViewById<TextView>(R.id.detail_time1)
        val detailContent = popupView.findViewById<TextView>(R.id.detail_content1)
        val incident = it.tag as Incident

        detailTitle.text = incident.part + incident.type
        detailContent.text = incident.title
        detailState.text = "(狀況:" +
                (if(incident.solved!="nxx"){
                    incident.solved
                } else {" 未排除"} ) + ")"
        detailSource.text = "來源:" + incident.auth
        detailTime.text = "時間:" + incident.raiseTime


        val popupWindow = PopupWindow(
            popupView,
            WindowManager.LayoutParams.WRAP_CONTENT,
            WindowManager.LayoutParams.WRAP_CONTENT

        ).apply {
            isOutsideTouchable = true
        }

        popupWindow.showAtLocation(popupView, Gravity.CENTER, 0, 0)

        val closebtn = popupView.findViewById<ImageButton>(R.id.close_btn) .setOnClickListener(){
            popupWindow.dismiss()
        }
    }

    override fun onSuccess(successData: ArrayList<String>) {}

    override fun onError(errorCode: Int, errorData: ArrayList<String>) {}

    override fun doInBackground(result: Int, successData: ArrayList<String>) {}

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