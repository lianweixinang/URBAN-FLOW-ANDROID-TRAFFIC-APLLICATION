package ntutifm.game.urbanflow.entity.adaptor
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.card.MaterialCardView
import ntutifm.game.urbanflow.apiClass.Parking
import ntutifm.game.urbanflow.databinding.ParkingItemBinding

class ParkingAdaptor(private var mList: List<Parking>, private val itemOnClickListener: View.OnClickListener,private val itemOnDeleteListener: View.OnClickListener) :
    RecyclerView.Adapter<ParkingAdaptor.ParkingHolder>() {

    inner class ParkingHolder(binding: ParkingItemBinding): RecyclerView.ViewHolder(binding.root){
        val type : TextView = binding.route1
        val root : MaterialCardView = binding.root
        val delete : ImageView = binding.ParkItemCancel
    }

    fun submitList(mList: List<Parking>){
        this.mList = mList
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ParkingAdaptor.ParkingHolder {
        val inflater = LayoutInflater.from(parent.context)
        val view = ParkingItemBinding.inflate(inflater, parent, false)
        return ParkingHolder(view)
    }

    override fun onBindViewHolder(holder: ParkingAdaptor.ParkingHolder, position: Int) {
        holder.type.text = mList[position].parkingName
        holder.type.tag = mList[position]
        holder.type.setOnClickListener(itemOnClickListener)
        holder.delete.tag = mList[position]
        holder.delete.setOnClickListener(itemOnDeleteListener)

    }

    override fun getItemCount(): Int {
        return mList.size
    }
}