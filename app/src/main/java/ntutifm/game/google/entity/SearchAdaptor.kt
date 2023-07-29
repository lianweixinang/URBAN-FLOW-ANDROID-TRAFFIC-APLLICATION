package ntutifm.game.google.entity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import ntutifm.game.google.R

class SearchAdaptor(var mList: List<SearchData>) :
    RecyclerView.Adapter<SearchAdaptor.SearchViewHolder>() {

    inner class SearchViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val logo : ImageView = itemView.findViewById(R.id.logoIv)
        val titleTv : TextView = itemView.findViewById(R.id.titleTv)
    }

    fun setFilteredList(mList: List<SearchData>){
        this.mList = mList
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SearchViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.each_item , parent , false)
        return SearchViewHolder(view)
    }

    override fun onBindViewHolder(holder: SearchViewHolder, position: Int) {
        holder.logo.setImageResource(mList[position].logo)
        holder.titleTv.text = mList[position].title

    }

    override fun getItemCount(): Int {
        return mList.size
    }
}