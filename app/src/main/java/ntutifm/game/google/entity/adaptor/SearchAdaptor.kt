package ntutifm.game.google.entity.adaptor
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.card.MaterialCardView
import ntutifm.game.google.R
import ntutifm.game.google.databinding.SearchItemBinding
import ntutifm.game.google.net.apiClass.CityRoad

class SearchAdaptor(private var mList: List<CityRoad>?, private val itemOnClickListener: View.OnClickListener,  private val deleteListener: View.OnClickListener) :
    RecyclerView.Adapter<SearchAdaptor.SearchViewHolder>() {

    inner class SearchViewHolder(binding: SearchItemBinding): RecyclerView.ViewHolder(binding.root){
        val logo : ImageView = itemView.findViewById(R.id.logoIv)
        val title : TextView = itemView.findViewById(R.id.titleTv)
        val root : MaterialCardView = itemView.findViewById(R.id.root)
        val delete : ImageView = itemView.findViewById(R.id.delete)
    }

    fun setFilteredList(mList: List<CityRoad>){
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
        holder.title.text = mList?.get(position)?.roadName ?: ""
        holder.title.tag = mList?.get(position) ?: "22"
        holder.delete.tag = mList?.get(position) ?: ""
        holder.title.setOnClickListener(itemOnClickListener)
//        holder.root.setOnTouchListener { view, motionEvent ->
//                when(motionEvent.action){
//                    MotionEvent.ACTION_DOWN ->view.parent.requestDisallowInterceptTouchEvent(false)
//                    MotionEvent.ACTION_UP ->view.parent.requestDisallowInterceptTouchEvent(true)
//                }
//                return@setOnTouchListener false
//            }
        holder.delete.setOnClickListener (deleteListener)
    }

    override fun getItemCount(): Int {
        return mList?.size ?: 0
    }
}