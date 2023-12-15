package ntutifm.game.urbanflow.ui.map

import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.model.BitmapDescriptor
import com.google.android.gms.maps.model.BitmapDescriptorFactory
import com.google.android.gms.maps.model.MarkerOptions
import com.google.maps.android.clustering.Cluster
import com.google.maps.android.clustering.ClusterManager
import com.google.maps.android.clustering.view.DefaultClusterRenderer
import ntutifm.game.urbanflow.R
import ntutifm.game.urbanflow.entity.mark.MyItem

class CustomClusterRenderer(
    context: Context,
    map: GoogleMap,
    clusterManager: ClusterManager<MyItem<Any>>
) : DefaultClusterRenderer<MyItem<Any>>(context, map, clusterManager) {
    val r = context.resources
    val displayMetrics = context.resources.displayMetrics
    val dpWidth = displayMetrics.widthPixels / displayMetrics.density
    val iconSize:Int = (dpWidth / 4).toInt()
    private val parkingIcon: BitmapDescriptor by lazy {
        val bitmap = BitmapFactory.decodeResource(r, R.drawable.parking)
        BitmapDescriptorFactory.fromBitmap(Bitmap.createScaledBitmap(bitmap, iconSize, iconSize, false))
    }

    private val speedCameraIcon: BitmapDescriptor by lazy {
        val bitmap = BitmapFactory.decodeResource(r, R.drawable.speed_camera)
        BitmapDescriptorFactory.fromBitmap(Bitmap.createScaledBitmap(bitmap, iconSize, iconSize, false))
    }

    private val gasStationIcon: BitmapDescriptor by lazy {
        val bitmap = BitmapFactory.decodeResource(r, R.drawable.gas_station)
        BitmapDescriptorFactory.fromBitmap(Bitmap.createScaledBitmap(bitmap, iconSize, iconSize, false))
    }

    override fun onBeforeClusterItemRendered(item: MyItem<Any>, markerOptions: MarkerOptions) {
        when (item.type) {
            0 -> markerOptions.icon(parkingIcon)
            1 -> markerOptions.icon(speedCameraIcon)
            2 -> markerOptions.icon(gasStationIcon)
        }
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
