package ntutifm.game.urbanflow.entity.mark;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.google.android.gms.maps.model.LatLng;
import com.google.maps.android.clustering.ClusterItem;
    public class MyItem<T> implements ClusterItem {
        private LatLng mPosition;
        private Integer mtype;
        private T mData;
        public MyItem(double lat,  double lng) {
            mPosition = new LatLng(lat,lng);
        }

        public MyItem(double lat,  double lng, Integer type, T data) {
            mPosition = new LatLng(lat,lng);
            mtype = type;
            mData = data;
        }


        @NonNull
        @Override
        public LatLng getPosition() {
            return mPosition;
        }

        @Nullable
        @Override
        public String getTitle() {
            return null;
        }

        @Nullable
        @Override
        public String getSnippet() {
            return null;
        }

        public Integer getType(){return mtype;}
        public T getData(){return mData;}
    }

