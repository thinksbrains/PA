package com.devilucky.placeapp;

import android.content.pm.PackageManager;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.devilucky.placeapp.fragment.FregmentoRegistroN;
import com.devilucky.placeapp.fragment.GoogleMaps;
import com.devilucky.placeapp.fragment.InfoFragmento;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

public class MapsRegister extends AppCompatActivity implements OnMapReadyCallback,GoogleMap.OnMapClickListener{

    GoogleMap googleMap;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps_register);

        GoogleMaps map = new GoogleMaps();
        map.getMapAsync(this);
        getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.content,map)
                .commit();
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        if (ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return;
        }
        this.googleMap=googleMap;
        googleMap.setOnMapClickListener(this);
        googleMap.setMyLocationEnabled(true);

        LatLng cali = new LatLng(-17.784558, -63.182125);

        CameraPosition cameraPosition = CameraPosition.builder()
                .target(cali)
                .zoom(15)
                .build();
        /*cali = new LatLng(-17.795066, -63.166600);
        googleMap.addMarker(new MarkerOptions()
                .position(cali)
                .title("marcador"));
        cali = new LatLng(-17.784758, -63.189191);
        googleMap.addMarker(new MarkerOptions()
                .position(cali)
                .title("marcador"));*/



        googleMap.moveCamera(CameraUpdateFactory.newCameraPosition(cameraPosition));
    }

    @Override
    public void onMapClick(LatLng latLng) {
        googleMap.addMarker(new MarkerOptions()
                .position(latLng)
                .title("marcador"));
        FregmentoRegistroN reg= new FregmentoRegistroN();
        reg.cargaLatLon(latLng.latitude,latLng.longitude);
        finish();
    }
}
