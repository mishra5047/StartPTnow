package com.doctappo;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import org.json.JSONException;
import org.json.JSONObject;

import models.ActiveModels;
import models.BusinessModel;
import util.CommonClass;
import util.GPSTracker;

public class MapActivity extends CommonActivity implements OnMapReadyCallback {

    private BusinessModel selected_salon;
    private GPSTracker gpsTracker;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_map);

        allowBack();
        setHeaderTitle(getString(R.string.tab_map));

        selected_salon = ActiveModels.BUSINESS_MODEL;


        com.google.android.gms.maps.MapFragment mapFragment = (com.google.android.gms.maps.MapFragment) getFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
    }

    @Override
    public void onMapReady(GoogleMap map) {
        Double lat = Double.parseDouble(selected_salon.getBus_latitude());
        Double lon = Double.parseDouble(selected_salon.getBus_longitude());
        map.moveCamera(CameraUpdateFactory.newLatLngZoom(
                new LatLng(lat, lon), 12));

        // You can customize the marker image using images bundled with
        // your app, or dynamically generated bitmaps.
        map.addMarker(new MarkerOptions()
                .icon(BitmapDescriptorFactory.fromResource(R.drawable.map_marker_icon))
                .anchor(0.0f, 1.0f) // Anchors the marker on the bottom left
                .title(selected_salon.getBus_title())
                .position(new LatLng(lat, lon)));


    }
}
