package ntutifm.game.google.entity.adaptor
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.card.MaterialCardView
import ntutifm.game.google.R
import ntutifm.game.google.apiClass.Camera
import ntutifm.game.google.databinding.CameraItemBinding
import ntutifm.game.google.entity.sync.SyncPosition

class CameraAdaptor(private var mList: List<Camera>, private val itemOnClickListener: View.OnClickListener) :
    RecyclerView.Adapter<CameraAdaptor.CameraHolder>() {

    inner class CameraHolder(binding: CameraItemBinding): RecyclerView.ViewHolder(binding.root){
        val type : TextView = itemView.findViewById(R.id.title1)
        val description : TextView = itemView.findViewById(R.id.content1)
        val root : MaterialCardView = itemView.findViewById(R.id.root)
    }

    fun submitList(mList: List<Camera>){
        this.mList = mList
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CameraAdaptor.CameraHolder {
        val inflater = LayoutInflater.from(parent.context)
        val view = CameraItemBinding.inflate(inflater, parent, false)
        return CameraHolder(view)
    }

    override fun onBindViewHolder(holder: CameraAdaptor.CameraHolder, position: Int) {
        holder.type.text = mList?.get(position)?.road
        holder.root.tag = mList?.get(position) ?: "22"
        holder.root.setOnClickListener(itemOnClickListener)

        val index = SyncPosition.districtToIndex()
    }

    override fun getItemCount(): Int {
        return mList?.size ?: 0
    }
}