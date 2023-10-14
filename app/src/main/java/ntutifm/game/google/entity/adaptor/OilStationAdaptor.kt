package ntutifm.game.google.entity.adaptor
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.card.MaterialCardView
import ntutifm.game.google.R
import ntutifm.game.google.databinding.IncidentItemBinding
import ntutifm.game.google.apiClass.OilStation
import ntutifm.game.google.entity.sync.SyncPosition

class OilStationAdaptor(private var mList: List<OilStation>?, private val itemOnClickListener: View.OnClickListener) :
    RecyclerView.Adapter<OilStationAdaptor.OilStationHolder>() {

    inner class OilStationHolder(binding: IncidentItemBinding): RecyclerView.ViewHolder(binding.root){
        val type : TextView = itemView.findViewById(R.id.title1)
        val root : MaterialCardView = itemView.findViewById(R.id.root)
    }

    fun submitList(mList: List<OilStation>){
        this.mList = mList
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): OilStationAdaptor.OilStationHolder {
        val inflater = LayoutInflater.from(parent.context)
        val view = IncidentItemBinding.inflate(inflater, parent, false)
        return OilStationHolder(view)
    }

    override fun onBindViewHolder(holder: OilStationHolder, position: Int) {
        holder.type.text = mList?.get(position)?.station
        holder.root.setOnClickListener(itemOnClickListener)
        val index = SyncPosition.districtToIndex()
    }

    override fun getItemCount(): Int {
        return mList?.size ?: 0
    }
}