package ntutifm.game.google.entity.adaptor

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.TextView
import ntutifm.game.google.apiClass.CCTV

class RoadAdaptor(
    context: Context,
    resource: Int,
    objects: MutableList<CCTV> = mutableListOf(
        CCTV(
            "中山北路-通河街口",
            url = "https://cctv.bote.gov.taipei:8501/mjpeg/232"
        ),
        CCTV("至善路-福林路口", url = "https://cctv.bote.gov.taipei:8501/mjpeg/233"),
        CCTV(
            "福林路-雨農路-中正路口",
            url = "https://cctv.bote.gov.taipei:8501/mjpeg/234"
        )
    ),
) :
    ArrayAdapter<CCTV>(context, resource, objects) {

    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
        val road = getItem(position)
        val view = convertView ?: LayoutInflater.from(context).inflate(
            android.R.layout.simple_spinner_item,
            parent,
            false
        )

        val textView = view.findViewById<TextView>(android.R.id.text1)
        textView.text = road?.name
        view.tag = road
        return view
    }

    override fun getDropDownView(position: Int, convertView: View?, parent: ViewGroup): View {
        return getView(position, convertView, parent)
    }

    override fun addAll(vararg items: CCTV?) {
        super.addAll(*items)
    }
}