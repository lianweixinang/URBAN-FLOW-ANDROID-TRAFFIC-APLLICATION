package ntutifm.game.google.ui.map

import android.content.Context
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.model.BitmapDescriptorFactory
import com.google.android.gms.maps.model.MarkerOptions
import com.google.maps.android.clustering.Cluster
import com.google.maps.android.clustering.ClusterManager
import com.google.maps.android.clustering.view.DefaultClusterRenderer
import ntutifm.game.google.entity.mark.MyItem

class CustomClusterRenderer(
    context: Context,
    map: GoogleMap,
    clusterManager: ClusterManager<MyItem>
) : DefaultClusterRenderer<MyItem>(context, map, clusterManager) {
    override fun onBeforeClusterItemRendered(item: MyItem, markerOptions: MarkerOptions) {
        // 根據 item 的 type 設置 markerOptions 的顏色
        when (item.type) {
            0 -> markerOptions.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_RED))
            1 -> markerOptions.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_BLUE))
            2 -> markerOptions.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_GREEN))
            // ... 為其他類型設置不同的顏色
        }
    }

    override fun onClustersChanged(clusters: MutableSet<out Cluster<MyItem>>?) {
        super.onClustersChanged(clusters)
    }

    override fun setOnClusterClickListener(listener: ClusterManager.OnClusterClickListener<MyItem>?) {
        super.setOnClusterClickListener(listener)
    }

    override fun setOnClusterInfoWindowClickListener(listener: ClusterManager.OnClusterInfoWindowClickListener<MyItem>?) {
        super.setOnClusterInfoWindowClickListener(listener)
    }

    override fun setOnClusterInfoWindowLongClickListener(listener: ClusterManager.OnClusterInfoWindowLongClickListener<MyItem>?) {
        super.setOnClusterInfoWindowLongClickListener(listener)
    }

    override fun setOnClusterItemClickListener(listener: ClusterManager.OnClusterItemClickListener<MyItem>?) {
        super.setOnClusterItemClickListener(listener)
    }

    override fun setOnClusterItemInfoWindowClickListener(listener: ClusterManager.OnClusterItemInfoWindowClickListener<MyItem>?) {
        super.setOnClusterItemInfoWindowClickListener(listener)
    }

    override fun setOnClusterItemInfoWindowLongClickListener(listener: ClusterManager.OnClusterItemInfoWindowLongClickListener<MyItem>?) {
        super.setOnClusterItemInfoWindowLongClickListener(listener)
    }

    override fun setAnimation(animate: Boolean) {
        super.setAnimation(animate)
    }

    override fun onAdd() {
        super.onAdd()
    }

    override fun onRemove() {
        super.onRemove()
    }
}
