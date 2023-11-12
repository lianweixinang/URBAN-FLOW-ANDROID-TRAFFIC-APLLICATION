package ntutifm.game.urbanflow.entity.adaptor
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.card.MaterialCardView
import ntutifm.game.urbanflow.apiClass.RoadFavorite
import ntutifm.game.urbanflow.databinding.RoadItemBinding

class RoadFavoriteAdaptor(private var mList: List<RoadFavorite>, private val itemOnClickListener: View.OnClickListener,private val itemOnDeleteListener: View.OnClickListener) :
    RecyclerView.Adapter<RoadFavoriteAdaptor.RoadHolder>() {

    inner class RoadHolder(binding: RoadItemBinding): RecyclerView.ViewHolder(binding.root){
        val road : TextView = binding.route1
        val root : MaterialCardView = binding.root
        val delete : ImageView = binding.itemCancel
    }

    fun submitList(mList: List<RoadFavorite>){
        this.mList = mList
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RoadFavoriteAdaptor.RoadHolder {
        val inflater = LayoutInflater.from(parent.context)
        val view = RoadItemBinding.inflate(inflater, parent, false)
        return RoadHolder(view)
    }

    override fun onBindViewHolder(holder: RoadFavoriteAdaptor.RoadHolder, position: Int) {
        holder.road.text = mList[position].roadName
        holder.road.tag = mList[position]
        holder.road.setOnClickListener(itemOnClickListener)
        holder.delete.tag = mList[position]
        holder.delete.setOnClickListener(itemOnDeleteListener)
    }

    override fun getItemCount(): Int {
        return mList.size
    }
}