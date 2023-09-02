package ntutifm.game.google.entity.adaptor
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.card.MaterialCardView
import ntutifm.game.google.R
import ntutifm.game.google.databinding.OilItemBinding
import ntutifm.game.google.net.ApiClass.Oil

class OilAdaptor(private var mList: List<Oil>?) :
    RecyclerView.Adapter<OilAdaptor.OilHolder>() {

    inner class OilHolder(binding: OilItemBinding): RecyclerView.ViewHolder(binding.root){
        val ninetwo : TextView = itemView.findViewById(R.id.t1)
        val ninefive : TextView = itemView.findViewById(R.id.t2)
        val nineeight : TextView = itemView.findViewById(R.id.t3)
        val superoil : TextView = itemView.findViewById(R.id.t4)

    }

    fun setFilteredList(mList: List<Oil>){
        this.mList = mList
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): OilAdaptor.OilHolder {
        val inflater = LayoutInflater.from(parent.context)
        val view = OilItemBinding.inflate(inflater, parent, false)
        return OilHolder(view)
    }

    override fun onBindViewHolder(holder: OilAdaptor.OilHolder, position: Int) {
        holder.ninetwo.text = mList?.get(position)?.ninetwo ?: ""
        holder.ninefive.text = mList?.get(position)?.ninefive ?: ""
        holder.nineeight.text = mList?.get(position)?.nineeight ?: ""
        holder.superoil.text = mList?.get(position)?.superoil ?: ""

    }

    override fun getItemCount(): Int {
        return mList?.size ?: 0
    }
}