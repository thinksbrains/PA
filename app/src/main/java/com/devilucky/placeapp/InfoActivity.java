package com.devilucky.placeapp;

import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.devilucky.placeapp.fragment.FragmentInicio;
import com.devilucky.placeapp.fragment.FragmentoComentar;
import com.devilucky.placeapp.fragment.FragmentoComidas;
import com.devilucky.placeapp.fragment.FragmentoEntretenimiento;
import com.devilucky.placeapp.fragment.FragmentoProductos;
import com.devilucky.placeapp.fragment.GoogleMaps;
import com.devilucky.placeapp.fragment.InfoFragmento;
import com.devilucky.placeapp.object.LocalesObj;
import com.github.clans.fab.FloatingActionButton;
import com.github.clans.fab.FloatingActionMenu;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class InfoActivity extends AppCompatActivity implements View.OnClickListener,OnMapReadyCallback {

    FloatingActionMenu materialDesignFAM;
    FloatingActionButton fabinicio, fabproductos, fabcomentarios, fabubicacion, fabcompartir;
    public static GoogleMaps fragmentmap;
    public static LocalesObj local;
    public static String id,sector;

    DatabaseReference mDatabase;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_info);

        Bundle e = getIntent().getExtras();
        if(e!=null) {
            id=e.getString("id");
            sector=e.getString("sector");
        }

        mDatabase = FirebaseDatabase.getInstance().getReference();

        loadJson();

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        /*InfoFragmento info = new InfoFragmento();
        getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.contenedor_principal,info)
                .commit();*/

        fragmentmap=new GoogleMaps();
        fragmentmap.getMapAsync(this);
    }
    public void loadJson(){
        mDatabase.child(sector).child(id).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                local = dataSnapshot.getValue(LocalesObj.class);
                local.setIdKey(dataSnapshot.getKey());
                InfoFragmento info = new InfoFragmento();
                getSupportFragmentManager()
                        .beginTransaction()
                        .replace(R.id.contenedor_principal,info)
                        .commit();
            }
            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }
    protected void onResume(){
        super.onResume();
        fragmentmap=new GoogleMaps();
        fragmentmap.getMapAsync(this);
    }

    @Override
    public void onClick(View v) {
        Fragment generico = null;
        boolean b=false;
        int id = v.getId();
        switch(id){
            case R.id.fab_inicio:
                generico=new InfoFragmento();
                break;
            case R.id.fab_compras:
                generico=new FragmentoProductos();
                break;
            case R.id.fab_comentarios:
                generico=new FragmentoComentar();
                break;
            case R.id.fab_ubicacion:
                generico=fragmentmap;
                break;
            case R.id.fab_compartir:
                break;
        }
        if (generico!=null){
            getSupportFragmentManager()
                    .beginTransaction()
                    .replace(R.id.contenedor_principal,generico)
                    .addToBackStack(null)
                    .commit();
        }
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

        googleMap.setMyLocationEnabled(true);

        LatLng cali = new LatLng(local.getLat(), local.getLon());
        googleMap.addMarker(new MarkerOptions()
                .position(cali)
                .title("marcador"));
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
}
