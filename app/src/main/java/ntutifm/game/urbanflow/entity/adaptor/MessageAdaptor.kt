package ntutifm.game.urbanflow.entity.adaptor
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.card.MaterialCardView
import ntutifm.game.urbanflow.apiClass.Message
import ntutifm.game.urbanflow.databinding.MessageItemBinding

class MessageAdaptor(private var mList: List<Message>, private val itemOnClickListener: View.OnClickListener, private val itemOnDeleteListener: View.OnClickListener) :
    RecyclerView.Adapter<MessageAdaptor.MessageHolder>() {

    inner class MessageHolder(binding: MessageItemBinding): RecyclerView.ViewHolder(binding.root){
        val title : TextView = binding.route1
        val root : MaterialCardView = binding.root
        val delete : ImageView = binding.OilstationItemCancel
    }

    fun submitList(mList: List<Message>){
        this.mList = mList
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MessageAdaptor.MessageHolder {
        val inflater = LayoutInflater.from(parent.context)
        val view = MessageItemBinding.inflate(inflater, parent, false)
        return MessageHolder(view)
    }

    override fun onBindViewHolder(holder: MessageHolder, position: Int) {
        holder.title.tag = mList[position]
        holder.delete.tag = mList[position]
        holder.title.setOnClickListener(itemOnClickListener)
        holder.delete.setOnClickListener(itemOnDeleteListener)
    }

    override fun getItemCount(): Int {
        return mList.size
    }
}