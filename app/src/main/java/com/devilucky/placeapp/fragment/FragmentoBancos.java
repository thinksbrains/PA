package com.devilucky.placeapp.fragment;

import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.devilucky.placeapp.R;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

/**
 * Created by Hakos on 11/06/2017.
 */

public class FragmentoBancos extends Fragment implements OnMapReadyCallback {
    GoogleMaps fragmentmap;

    public View onCreateView(LayoutInflater inflater, ViewGroup parent, Bundle saved){
        View v = inflater.inflate(R.layout.fragmento_bancos,parent,false);
        fragmentmap=new GoogleMaps();
        fragmentmap.getMapAsync(this);
        getActivity().getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.content,fragmentmap)
                .commit();
        return v;
    }
    @Override
    public void onMapReady(GoogleMap googleMap) {

        if (ActivityCompat.checkSelfPermission(getActivity(), android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(getActivity(), android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return;
        }

        googleMap.setMyLocationEnabled(true);

        /*LatLng cali = new LatLng(local.getLat(), local.getLon());
        googleMap.addMarker(new MarkerOptions()
                .position(cali)
                .title("marcador"));*/

        LatLng cali = new LatLng(-17.795066, -63.166600);
        googleMap.addMarker(new MarkerOptions()
                .position(cali)
                .title("marcador"));
        cali = new LatLng(-17.784758, -63.189191);
        googleMap.addMarker(new MarkerOptions()
                .position(cali)
                .title("marcador"));
        CameraPosition cameraPosition = CameraPosition.builder()
                .target(cali)
                .zoom(13)
                .build();



        googleMap.moveCamera(CameraUpdateFactory.newCameraPosition(cameraPosition));
    }
}
