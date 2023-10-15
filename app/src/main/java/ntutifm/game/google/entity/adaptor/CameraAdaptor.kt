package ntutifm.game.google.entity.adaptor
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.card.MaterialCardView
import ntutifm.game.google.apiClass.Camera
import ntutifm.game.google.databinding.CameraItemBinding

class CameraAdaptor(private var mList: List<Camera>, private val itemOnClickListener: View.OnClickListener) :
    RecyclerView.Adapter<CameraAdaptor.CameraHolder>() {

    inner class CameraHolder(binding: CameraItemBinding): RecyclerView.ViewHolder(binding.root){
        val type : TextView = binding.route1
        val description : TextView = binding.route2
        val root : MaterialCardView = binding.root
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
        holder.type.text = mList[position].road
        holder.description.text = mList[position].introduction
        holder.root.tag = mList[position]
        holder.root.setOnClickListener(itemOnClickListener)
    }

    override fun getItemCount(): Int {
        return mList.size
    }
}