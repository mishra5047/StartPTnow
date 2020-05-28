package fragments;

import android.app.Activity;
import android.os.Bundle;
import androidx.fragment.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.doctappo.R;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import Config.ApiParams;
import models.BusinessModel;
import util.CommonClass;
import util.GPSTracker;
import util.VJsonRequest;

/**
 * Created by LENOVO on 7/10/2016.
 */
public class MapFragment extends Fragment implements OnMapReadyCallback {

    private CommonClass common;
    private ArrayList<BusinessModel> postItems;
    String PREF_BUSINESS = "pref_business";
    private GPSTracker gpsTracker;
    private Double cur_latitude, cur_longitude;

    private Activity act;

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        if (isVisibleToUser) {
            // animate here
            Log.e("Map", "animate here");

        } else {
            Log.e("Map", "fragment is no longer visible");
            // fragment is no longer visible
        }
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.activity_map, container, false);
        act = getActivity();

        postItems = new ArrayList<>();
        common = new CommonClass(act);

        gpsTracker = new GPSTracker(act);
        if (gpsTracker.canGetLocation()) {
            if (gpsTracker.getLatitude() != 0.0)
                cur_latitude = gpsTracker.getLatitude();
            if (gpsTracker.getLongitude() != 0.0)
                cur_longitude = gpsTracker.getLongitude();
        } else {
            gpsTracker.showSettingsAlert();
        }

        loadGetResult();

        return rootView;
    }

    @Override
    public void onResume() {
        super.onResume();
    }

    @Override
    public void onMapReady(GoogleMap map) {
        if (cur_latitude != null && cur_longitude != null) {
            map.moveCamera(CameraUpdateFactory.newLatLngZoom(
                    new LatLng(cur_latitude, cur_longitude), 12));

            // You can customize the marker image using images bundled with
            // your app, or dynamically generated bitmaps.
            map.addMarker(new MarkerOptions()
                    .icon(BitmapDescriptorFactory.fromResource(R.drawable.map_marker_icon))
                    .anchor(0.0f, 1.0f) // Anchors the marker on the bottom left
                    .title("You")
                    .position(new LatLng(cur_latitude, cur_longitude)));
        }
        if (postItems != null) {
            for (int i = 0; i < postItems.size(); i++) {

                BusinessModel jObj = postItems.get(i);
                Double lat = Double.parseDouble(jObj.getBus_latitude());
                Double lon = Double.parseDouble(jObj.getBus_longitude());
                map.addMarker(new MarkerOptions()
                        .icon(BitmapDescriptorFactory.fromResource(R.drawable.flag_icon))
                        .anchor(0.0f, 1.0f) // Anchors the marker on the bottom left
                        .title(jObj.getBus_title())
                        .snippet(jObj.getBus_google_street() + "\n" + jObj.getBus_contact())
                        .position(new LatLng(lat, lon)));


            }
        }
    }

    // get business data from api
    public void loadGetResult() {

        HashMap<String, String> params = new HashMap<>();
        if (getArguments() != null && getArguments().containsKey("search"))
            params.put("search", getArguments().getString("search"));

        //if (type !=null && !type.equalsIgnoreCase("0"))
        //    params.put("type","page_type_id");

        if (getArguments().containsKey("cat_id"))
            params.put("cat_id", getArguments().getString("cat_id"));
        if (getArguments().containsKey("locality_id"))
            params.put("locality_id", getArguments().getString("locality_id"));
        int radius = 60;
        if (common.containKeyInSession("radius")) {
            radius = common.getSessionInt("radius");
            if (radius <= 0) {
                radius = 60;
            }
        }

        if (getArguments().containsKey("lat"))
            params.put("lat", getArguments().getString("lat"));
        if (getArguments().containsKey("lon"))
            params.put("lon", getArguments().getString("lon"));

        params.put("rad", String.valueOf(radius));

        // this class for handle request response thread and return response data
        VJsonRequest vJsonRequest = new VJsonRequest(getActivity(), ApiParams.BUSINESS_LIST, params,
                new VJsonRequest.VJsonResponce() {
                    @Override
                    public void VResponce(String responce) {

                        Gson gson = new Gson();
                        Type listType = new TypeToken<List<BusinessModel>>() {
                        }.getType();
                        ArrayList<BusinessModel> arraylist = (ArrayList<BusinessModel>) gson.fromJson(responce, listType);


                        postItems.clear();

                        postItems.addAll(arraylist);

                        com.google.android.gms.maps.MapFragment mapFragment = (com.google.android.gms.maps.MapFragment) act.getFragmentManager()
                                .findFragmentById(R.id.map);
                        mapFragment.getMapAsync(MapFragment.this);

                    }

                    @Override
                    public void VError(String responce) {

                    }
                });
    }
}
