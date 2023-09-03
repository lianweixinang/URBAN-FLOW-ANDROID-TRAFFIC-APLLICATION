package ntutifm.game.google.entity.adaptor
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.card.MaterialCardView
import ntutifm.game.google.R
import ntutifm.game.google.databinding.OilItemBinding
import ntutifm.game.google.databinding.WeatherItemBinding
import ntutifm.game.google.net.ApiClass.Oil
import ntutifm.game.google.net.ApiClass.Weather

class WeatherAdaptor(private var mList: List<Weather>?) :
    RecyclerView.Adapter<WeatherAdaptor.WeatherHolder>() {

    inner class WeatherHolder(binding: WeatherItemBinding): RecyclerView.ViewHolder(binding.root){
        val locationName : TextView = itemView.findViewById(R.id.position)
//        val description : TextView = itemView.findViewById(R.id.t2)
//        val pop : TextView = itemView.findViewById(R.id.t3)
//        val week : TextView = itemView.findViewById(R.id.t4)
        val temperature : TextView = itemView.findViewById(R.id.now_weather)


    }

    fun setFilteredList(mList: List<Weather>){
        this.mList = mList
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): WeatherAdaptor.WeatherHolder {
        val inflater = LayoutInflater.from(parent.context)
        val view = WeatherItemBinding.inflate(inflater, parent, false)
        return WeatherHolder(view)
    }

    override fun onBindViewHolder(holder: WeatherAdaptor.WeatherHolder, position: Int) {
        holder.locationName.text = mList?.get(position)?.locationName ?: ""
//        holder.description.text = mList?.get(position)?.weatherDescription ?: ""
//        holder.pop.text = mList?.get(position)?.pop12h1 ?: ""
//        holder.week.text = mList?.get(position)?.wx1 ?: ""
        holder.temperature.text = mList?.get(position)?.t1 ?: ""

    }

    override fun getItemCount(): Int {
        return mList?.size ?: 0
    }
}