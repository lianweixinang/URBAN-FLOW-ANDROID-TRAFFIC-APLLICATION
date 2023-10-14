package ntutifm.game.google.entity.adaptor
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.card.MaterialCardView
import ntutifm.game.google.R
import ntutifm.game.google.apiClass.Parking
import ntutifm.game.google.databinding.ParkingBinding
import ntutifm.game.google.entity.sync.SyncPosition

class ParkingAdaptor(private var mList: List<Parking>, private val itemOnClickListener: View.OnClickListener) :
    RecyclerView.Adapter<ParkingAdaptor.ParkingHolder>() {

    inner class ParkingHolder(binding: ParkingBinding): RecyclerView.ViewHolder(binding.root){
        val type : TextView = itemView.findViewById(R.id.title1)
        val description : TextView = itemView.findViewById(R.id.content1)
        val root : MaterialCardView = itemView.findViewById(R.id.root)
    }

    fun submitList(mList: List<Parking>){
        this.mList = mList
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ParkingAdaptor.ParkingHolder {
        val inflater = LayoutInflater.from(parent.context)
        val view = ParkingBinding.inflate(inflater, parent, false)
        return ParkingHolder(view)
    }

    override fun onBindViewHolder(holder: ParkingAdaptor.ParkingHolder, position: Int) {
        holder.type.text = mList?.get(position)?.parkingName
        holder.description.text = mList?.get(position)?.parkingName

        holder.root.tag = mList?.get(position) ?: "22"
        holder.root.setOnClickListener(itemOnClickListener)

        val index = SyncPosition.districtToIndex()
    }

    override fun getItemCount(): Int {
        return mList?.size ?: 0
    }
}