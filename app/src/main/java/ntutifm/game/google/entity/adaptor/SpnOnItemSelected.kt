package ntutifm.game.google.entity.adaptor

import android.view.View
import android.widget.AdapterView
import ntutifm.game.google.apiClass.CCTV
import ntutifm.game.google.databinding.FragmentMapBinding

class SpnOnItemSelected(val binding: FragmentMapBinding) : AdapterView.OnItemSelectedListener {
    override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
        val selectedItem = parent?.getItemAtPosition(position) as? CCTV
        selectedItem?.let {
            binding.webView.loadUrl(it.url)
        }

    }

    override fun onNothingSelected(parent: AdapterView<*>?) {

    }

}