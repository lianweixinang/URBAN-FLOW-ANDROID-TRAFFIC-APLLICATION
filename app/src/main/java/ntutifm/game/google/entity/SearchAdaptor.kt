package ntutifm.game.google.entity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.card.MaterialCardView
import ntutifm.game.google.R
import ntutifm.game.google.databinding.FragmentWeatherBinding
import ntutifm.game.google.databinding.SearchItemBinding

class SearchAdaptor(private var mList: List<SearchData>, private val itemOnClickListener: View.OnClickListener) :
    RecyclerView.Adapter<SearchAdaptor.SearchViewHolder>() {

    inner class SearchViewHolder(binding: SearchItemBinding): RecyclerView.ViewHolder(binding.root){
        val logo : ImageView = itemView.findViewById(R.id.logoIv)
        val title : TextView = itemView.findViewById(R.id.titleTv)
        val root : MaterialCardView = itemView.findViewById(R.id.root)
    }

    fun setFilteredList(mList: List<SearchData>){
        this.mList = mList
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SearchViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val view = SearchItemBinding.inflate(inflater, parent, false)
        return SearchViewHolder(view)
    }

    override fun onBindViewHolder(holder: SearchViewHolder, position: Int) {
        holder.logo.setImageResource(R.drawable.ic_baseline_search_25)
        holder.title.text = mList[position].title
        holder.root.setOnClickListener(itemOnClickListener)
        holder.root.tag = mList[position]
    }

    override fun getItemCount(): Int {
        return mList.size
    }
}