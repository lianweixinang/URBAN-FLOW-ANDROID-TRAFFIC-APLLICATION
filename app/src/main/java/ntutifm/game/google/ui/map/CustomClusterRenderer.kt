package ntutifm.game.google.ui.map

import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.model.BitmapDescriptor
import com.google.android.gms.maps.model.BitmapDescriptorFactory
import com.google.android.gms.maps.model.MarkerOptions
import com.google.maps.android.clustering.Cluster
import com.google.maps.android.clustering.ClusterManager
import com.google.maps.android.clustering.view.ClusterRenderer
import com.google.maps.android.clustering.view.DefaultClusterRenderer
import ntutifm.game.google.R
import ntutifm.game.google.entity.mark.MyItem

class CustomClusterRenderer(
    context: Context,
    map: GoogleMap,
    clusterManager: ClusterManager<MyItem<Any>>
) : DefaultClusterRenderer<MyItem<Any>>(context, map, clusterManager) {
    val r = context.resources
    val displayMetrics = context.resources.displayMetrics
    val dpWidth = displayMetrics.widthPixels / displayMetrics.density
    val iconSize:Int = (dpWidth / 4).toInt()
    override fun onBeforeClusterItemRendered(item: MyItem<Any>, markerOptions: MarkerOptions) {
        // 根據 item 的 type 設置 markerOptions 的圖示
        when (item.type) {
            0 -> {
                val bitmap = BitmapFactory.decodeResource(r, R.drawable.parking)
                val scaledBitmap = Bitmap.createScaledBitmap(bitmap, iconSize, iconSize, false)
                markerOptions.icon(BitmapDescriptorFactory.fromBitmap(scaledBitmap))
            }
            1 -> {
                val bitmap = BitmapFactory.decodeResource(r, R.drawable.speed_camera)
                val scaledBitmap = Bitmap.createScaledBitmap(bitmap, iconSize, iconSize, false)
                markerOptions.icon(BitmapDescriptorFactory.fromBitmap(scaledBitmap))
            }
            2 -> {
                val bitmap = BitmapFactory.decodeResource(r, R.drawable.gas_station)
                val scaledBitmap = Bitmap.createScaledBitmap(bitmap, iconSize, iconSize, false)
                markerOptions.icon(BitmapDescriptorFactory.fromBitmap(scaledBitmap))
            }
        }
        // 設置標記的大小
//        markerOptions.anchor(0.1f, 0.1f)
//        markerOptions.infoWindowAnchor(0.1f, 0.1f)
    }



    override fun onClustersChanged(clusters: MutableSet<out Cluster<MyItem<Any>>>?) {
        super.onClustersChanged(clusters)
    }

    override fun setOnClusterClickListener(listener: ClusterManager.OnClusterClickListener<MyItem<Any>>?) {
        super.setOnClusterClickListener(listener)
    }

    override fun setOnClusterInfoWindowClickListener(listener: ClusterManager.OnClusterInfoWindowClickListener<MyItem<Any>>?) {
        super.setOnClusterInfoWindowClickListener(listener)
    }

    override fun setOnClusterInfoWindowLongClickListener(listener: ClusterManager.OnClusterInfoWindowLongClickListener<MyItem<Any>>?) {
        super.setOnClusterInfoWindowLongClickListener(listener)
    }

    override fun setOnClusterItemClickListener(listener: ClusterManager.OnClusterItemClickListener<MyItem<Any>>?) {
        super.setOnClusterItemClickListener(listener)
    }

    override fun setOnClusterItemInfoWindowClickListener(listener: ClusterManager.OnClusterItemInfoWindowClickListener<MyItem<Any>>?) {
        super.setOnClusterItemInfoWindowClickListener(listener)
    }

    override fun setOnClusterItemInfoWindowLongClickListener(listener: ClusterManager.OnClusterItemInfoWindowLongClickListener<MyItem<Any>>?) {
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
