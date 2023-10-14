package ntutifm.game.google.entity.adaptor

import android.view.View
import android.widget.AdapterView
import ntutifm.game.google.apiClass.CCTV
import ntutifm.game.google.databinding.FragmentMapBinding
import ntutifm.game.google.ui.map.MapViewModel

class SpnOnItemSelected(val binding: FragmentMapBinding, private val viewModel: MapViewModel) : AdapterView.OnItemSelectedListener {
    override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
        val selectedItem = parent?.getItemAtPosition(position) as? CCTV
        selectedItem?.let {
            binding.webView.loadUrl(it.url)
            binding.fragmentHome.textView.text = it.name
            viewModel.checkCCTV(it.name)
        }

    }

    override fun onNothingSelected(parent: AdapterView<*>?) {

    }

}