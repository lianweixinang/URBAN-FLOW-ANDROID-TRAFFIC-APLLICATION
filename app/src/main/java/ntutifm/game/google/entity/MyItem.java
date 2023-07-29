package ntutifm.game.google.entity;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.google.android.gms.maps.model.LatLng;
import com.google.maps.android.clustering.ClusterItem;
    public class MyItem implements ClusterItem {
        private LatLng mPosition;
        private String mTitle;
        private String mSnippet;
        public MyItem(double lat, double lng) {
            mPosition = new LatLng(lat, lng);
            mTitle = null;
            mSnippet = null;
        }

        public MyItem(double lat, double lng, String title, String snippet) {
            mPosition = new LatLng(lat, lng);
            mTitle = title;
            mSnippet = snippet;
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
    }