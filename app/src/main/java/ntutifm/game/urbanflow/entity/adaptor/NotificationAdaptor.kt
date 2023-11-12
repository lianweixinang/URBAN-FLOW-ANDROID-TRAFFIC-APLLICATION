package ntutifm.game.urbanflow.entity.adaptor
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.card.MaterialCardView
import ntutifm.game.urbanflow.R
import ntutifm.game.urbanflow.databinding.IncidentItemBinding
import ntutifm.game.urbanflow.apiClass.Incident
import ntutifm.game.urbanflow.entity.sync.SyncPosition

class NotificationAdaptor(private var mList: List<Incident>?, private val itemOnClickListener: View.OnClickListener) :
    RecyclerView.Adapter<NotificationAdaptor.IncidentHolder>() {

    inner class IncidentHolder(binding: IncidentItemBinding): RecyclerView.ViewHolder(binding.root){
        val type : TextView = itemView.findViewById(R.id.title1)
//        val time : TextView = itemView.findViewById(R.id.raiseTime1)
//        val from : TextView = itemView.findViewById(R.id.source1)
//        val state : TextView = itemView.findViewById(R.id.state1)
        val description : TextView = itemView.findViewById(R.id.content1)
        val root : MaterialCardView = itemView.findViewById(R.id.root)
    }

    fun setFilteredList(mList: List<Incident>){
        this.mList = mList
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): NotificationAdaptor.IncidentHolder {
        val inflater = LayoutInflater.from(parent.context)
        val view = IncidentItemBinding.inflate(inflater, parent, false)
        return IncidentHolder(view)
    }

    override fun onBindViewHolder(holder: NotificationAdaptor.IncidentHolder, position: Int) {
        holder.type.text = (mList?.get(position)?.part + mList?.get(position)?.type) ?: ""
        holder.description.text = mList?.get(position)?.title ?: ""
//        holder.time.text = mList?.get(position)?.raiseTime ?: ""
//        holder.from.text = ("來源:" + mList?.get(position)?.auth) ?: ""
//        holder.state.text = "(狀況:" +
//              (if(mList?.get(position)?.solved !="nxx"){
//                  mList?.get(position)?.solved
//              } else {" 未排除"} )  +
//         ")"
        holder.root.tag = mList?.get(position) ?: "22"
        holder.root.setOnClickListener(itemOnClickListener)

        val index = SyncPosition.districtToIndex()
    }

    override fun getItemCount(): Int {
        return mList?.size ?: 0
    }
}