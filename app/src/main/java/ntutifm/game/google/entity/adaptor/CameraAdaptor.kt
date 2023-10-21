package ntutifm.game.google.entity.adaptor
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import ntutifm.game.google.R
import ntutifm.game.google.apiClass.Camera
import ntutifm.game.google.databinding.CameraItemBinding

class CameraAdaptor(private var mList: List<Camera>, private val itemOnClickListener: View.OnClickListener,private val itemOnDeleteListener: View.OnClickListener) :
    RecyclerView.Adapter<CameraAdaptor.CameraHolder>() {

    inner class CameraHolder(binding: CameraItemBinding): RecyclerView.ViewHolder(binding.root){
        val district : TextView = binding.routeDtr
        val road : TextView = binding.route
        val description : TextView = binding.routeDes
        val type : TextView = binding.type
        val limit : TextView = binding.limit
        val delete : ImageView = binding.itemCancel
        val image :ImageView = binding.limitIcon
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
        holder.district.text = "台北市" + mList[position].session + "區"
        holder.road.text = mList[position].road
        holder.description.text = mList[position].introduction
        holder.type.text = mList[position].type + "偵測"
        holder.limit.text = "限速:" + mList[position].limit
        holder.image.tag = mList[position]
        holder.image.setImageResource(
            when( mList[position].limit){
                "25" -> {
                    R.drawable.sl25
                }
                "40" -> {
                    R.drawable.sl40
                }
                "50" -> {
                    R.drawable.sl50
                }
                "60" -> {
                    R.drawable.sl60
                }
                "70" -> {
                    R.drawable.sl70
                }
                "80" -> {
                    R.drawable.sl80
                }
                else ->{
                    R.drawable.sl50
                }
            }
        )
        holder.image.setOnClickListener(itemOnClickListener)
        holder.delete.tag = mList[position]
        holder.delete.setOnClickListener(itemOnDeleteListener)
    }

    override fun getItemCount(): Int {
        return mList.size
    }
}