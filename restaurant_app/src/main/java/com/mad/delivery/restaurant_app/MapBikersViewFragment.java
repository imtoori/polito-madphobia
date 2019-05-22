package com.mad.delivery.restaurant_app;
import android.Manifest;
import android.content.pm.PackageManager;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.core.app.ActivityCompat;
import androidx.fragment.app.Fragment;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.MapsInitializer;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.mad.delivery.resources.Biker;
import com.mad.delivery.resources.Restaurant;

import java.util.List;
import java.util.TreeMap;

public class MapBikersViewFragment extends Fragment {

    MapView mMapView;
    GoogleMap mMap;
    private static final int LOCATION_REQUEST = 500;
    LatLng r;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.location_fragment2, container, false);
        r = getArguments().getParcelable("restaurant");
        mMapView = (MapView) rootView.findViewById(R.id.mapView2);
        mMapView.onCreate(savedInstanceState);
        mMapView.onResume(); // needed to get the map to display immediately

        try {
            MapsInitializer.initialize(getActivity().getApplicationContext());
        } catch (Exception e) {
            e.printStackTrace();
        }


        mMapView.getMapAsync(new OnMapReadyCallback() {
            @Override
            public void onMapReady(GoogleMap googleMap2) {
                mMap = googleMap2;

                mMap.getUiSettings().setZoomControlsEnabled(true);
                if (ActivityCompat.checkSelfPermission(getContext(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(getContext(), Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                    ActivityCompat.requestPermissions(getActivity(), new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, LOCATION_REQUEST);
                    return;

                }
                mMap.setMyLocationEnabled(true);
                CameraPosition cameraPosition = new CameraPosition.Builder().target(r).zoom(12).build();
                mMap.animateCamera(CameraUpdateFactory.newCameraPosition(cameraPosition));
                MarkerOptions markerOptions = new MarkerOptions();
                markerOptions.position(r).icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_RED)).title("Ristorante");
                Marker m = mMap.addMarker(markerOptions);
                m.showInfoWindow();
                RestaurantDatabase.getInstance().getBikersClosest(new FireBaseCallBack<TreeMap<Double,Biker>>() {


                    @Override
                    public void onCallbackList(List<TreeMap< Double,Biker>> list) {

                    }

                    @Override
                    public void onCallback(TreeMap<Double,Biker> user) {
                        user.forEach((d,b)->{
                            MarkerOptions markerOptions = new MarkerOptions();
                            LatLng p = new LatLng(b.latitude,b.longitude);
                            Log.d("BIKER :",d.toString()+" "+p.toString());
                            markerOptions.position(p).title(b.email).snippet("Distanza: "+d.toString()).icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_YELLOW));
                            Marker m =mMap.addMarker(markerOptions);
                            m.showInfoWindow();

                        });


                    }
                });

            }
        });

        return rootView;
    }

    @Override
    public void onResume() {
        super.onResume();
        mMapView.onResume();
    }

    @Override
    public void onPause() {
        super.onPause();
        mMapView.onPause();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        mMapView.onDestroy();
    }

    @Override
    public void onLowMemory() {
        super.onLowMemory();
        mMapView.onLowMemory();
    }


}