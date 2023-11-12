package ntutifm.game.urbanflow.entity.adaptor
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.card.MaterialCardView
import ntutifm.game.urbanflow.apiClass.OilStation
import ntutifm.game.urbanflow.databinding.OilStationItemBinding

class OilStationAdaptor(private var mList: List<OilStation>, private val itemOnClickListener: View.OnClickListener, private val itemOnDeleteListener: View.OnClickListener) :
    RecyclerView.Adapter<OilStationAdaptor.OilStationHolder>() {

    inner class OilStationHolder(binding: OilStationItemBinding): RecyclerView.ViewHolder(binding.root){
        val title : TextView = binding.route1
        val root : MaterialCardView = binding.root
        val delete : ImageView = binding.OilstationItemCancel
    }

    fun submitList(mList: List<OilStation>){
        this.mList = mList
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): OilStationAdaptor.OilStationHolder {
        val inflater = LayoutInflater.from(parent.context)
        val view = OilStationItemBinding.inflate(inflater, parent, false)
        return OilStationHolder(view)
    }

    override fun onBindViewHolder(holder: OilStationHolder, position: Int) {
        holder.title.text = mList[position].station
        holder.title.tag = mList[position]
        holder.delete.tag = mList[position]
        holder.title.setOnClickListener(itemOnClickListener)
        holder.delete.setOnClickListener(itemOnDeleteListener)
    }

    override fun getItemCount(): Int {
        return mList.size
    }
}