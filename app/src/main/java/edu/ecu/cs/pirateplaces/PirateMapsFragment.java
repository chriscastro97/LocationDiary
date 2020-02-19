package edu.ecu.cs.pirateplaces;


import android.location.Location;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.MarkerOptions;

import java.util.ArrayList;
import java.util.List;


/**
 * A simple {@link Fragment} subclass.
 */
public class PirateMapsFragment extends SupportMapFragment {

    private static final String TAG = "PirateMapsFragment";

    private GoogleMap mMap;
    private Location mCurrentLocation;


    public static  PirateMapsFragment newInstance(){ return new PirateMapsFragment();}


    @Override
    public void onCreate(Bundle bundle) {
        super.onCreate(bundle);


        getMapAsync(new OnMapReadyCallback() {
            @Override
            public void onMapReady(GoogleMap googleMap) {
                mMap = googleMap;
                mMap.setOnMapLoadedCallback(new GoogleMap.OnMapLoadedCallback() {
                    @Override
                    public void onMapLoaded() {
                        updateUI();
                    }
                });
            }
        });
    }


    private void updateUI() {

        PirateBase pirateBase = PirateBase.getPirateBase(getActivity());
        List<PiratePlace> piratePlaces = pirateBase.getPiratePlaces();
        List<MarkerOptions> imageMarkers = new ArrayList<>();



        for (int i = 0; i < piratePlaces.size(); i++ ) {

            PiratePlace piratePlace = piratePlaces.get(i);
            if (piratePlace.doesHaslocation()) {
                LatLng itemPoint = new LatLng(piratePlace.getLatitude(), piratePlace.getLongitude());

                MarkerOptions itemMarker = new MarkerOptions()
                        .position(itemPoint)
                        .icon(BitmapDescriptorFactory.defaultMarker());
                imageMarkers.add(itemMarker);
            }
        }

        if (imageMarkers.size() == 0) {
            return;
        }

        mMap.clear();
        for (MarkerOptions itemMarker : imageMarkers) {
            mMap.addMarker(itemMarker);
        }

        LatLngBounds.Builder builder = new LatLngBounds.Builder();
        for (MarkerOptions itemMarker : imageMarkers) {
            builder.include(itemMarker.getPosition());
        }

        LatLngBounds bounds = builder.build();

        int margin = getResources().getDimensionPixelSize(R.dimen.map_inset_margin);
        CameraUpdate update = CameraUpdateFactory.newLatLngBounds(bounds,margin);
        mMap.animateCamera(update);

    }








}
