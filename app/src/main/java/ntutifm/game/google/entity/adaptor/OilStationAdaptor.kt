package ntutifm.game.google.entity.adaptor
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.card.MaterialCardView
import ntutifm.game.google.apiClass.OilStation
import ntutifm.game.google.databinding.OilStationItemBinding

class OilStationAdaptor(private var mList: List<OilStation>, private val itemOnClickListener: View.OnClickListener) :
    RecyclerView.Adapter<OilStationAdaptor.OilStationHolder>() {

    inner class OilStationHolder(binding: OilStationItemBinding): RecyclerView.ViewHolder(binding.root){
        val title : TextView = binding.route1
        val root : MaterialCardView = binding.root
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
        holder.root.tag = mList[position]
        holder.root.setOnClickListener(itemOnClickListener)
    }

    override fun getItemCount(): Int {
        return mList.size
    }
}