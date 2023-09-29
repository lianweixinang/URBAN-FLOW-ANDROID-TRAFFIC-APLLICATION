package ntutifm.game.google.entity.mark;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.google.android.gms.maps.model.LatLng;
import com.google.maps.android.clustering.ClusterItem;
    public class MyItem implements ClusterItem {
        private LatLng mPosition;
        private String mTitle;
        private String mSnippet;
        private Integer mtype;
        public MyItem(double lat,  double lng) {
            mPosition = new LatLng(lat,lng);
            mTitle = null;
            mSnippet = null;
        }

        public MyItem(double lat,  double lng, String title,Integer type) {
            mPosition = new LatLng(lat,lng);
            mTitle = title;
            mSnippet = null;
            mtype = type;
        }

        public void setTitle(String title) {
            mTitle = title;
        }

        public void setSnippet(String snippet) {
            mSnippet = snippet;
        }

        @NonNull
        @Override
        public LatLng getPosition() {
            return mPosition;
        }

        @Nullable
        @Override
        public String getTitle() {
            return mTitle;
        }

        @Nullable
        @Override
        public String getSnippet() {
            return mSnippet;
        }

        public Integer getType(){return mtype;}
    }

