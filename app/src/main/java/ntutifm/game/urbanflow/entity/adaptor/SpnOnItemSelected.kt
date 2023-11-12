package ntutifm.game.urbanflow.entity.adaptor

import android.view.View
import android.widget.AdapterView
import ntutifm.game.urbanflow.apiClass.CCTV
import ntutifm.game.urbanflow.databinding.FragmentMapBinding
import ntutifm.game.urbanflow.ui.map.MapViewModel

class SpnOnItemSelected(val binding: FragmentMapBinding, private val viewModel: MapViewModel, private val listener:()->Unit) : AdapterView.OnItemSelectedListener {
    override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
        val selectedItem = parent?.getItemAtPosition(position) as? CCTV
        selectedItem?.let {
            binding.webView.loadUrl(it.url)
            binding.fragmentHome.textView.text = it.name
            viewModel.checkCCTV(it.name)
        }
        listener()
    }

    override fun onNothingSelected(parent: AdapterView<*>?) {

    }

}