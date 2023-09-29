package ntutifm.game.google.ui.notification

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
import ntutifm.game.google.databinding.FragmentNotificationBinding
import ntutifm.game.google.entity.sync.SyncIncident
import ntutifm.game.google.entity.adaptor.NotificationAdaptor
import ntutifm.game.google.global.MyLog
import ntutifm.game.google.net.ApiCallBack
import ntutifm.game.google.ui.oil.NotificationViewModel

class NotificationFragment : Fragment(), ApiCallBack {

    private var _binding : FragmentNotificationBinding? = null
    private val binding get() = _binding!!
    private var recycleView : RecyclerView? = null
    private var adaptor : NotificationAdaptor? = null
    private val viewModel : NotificationViewModel by lazy {
        ViewModelProvider(this)[NotificationViewModel::class.java]
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        _binding = FragmentNotificationBinding.inflate(inflater, container, false)
        return binding.root
    }

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        incidentListInit()
        SyncIncident.incidentLists.observe(viewLifecycleOwner){
            MyLog.e("updateIncident")
            adaptor?.setFilteredList(it)
        }
        SyncIncident.getIncident(this,this)

    }
    @RequiresApi(Build.VERSION_CODES.O)
    private fun incidentListInit(){
        recycleView = binding.recycleIncidentView
        recycleView?.setHasFixedSize(true)
        recycleView?.layoutManager = LinearLayoutManager(MyActivity().context)
        adaptor = NotificationAdaptor(listOf())
        recycleView?.adapter = adaptor
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