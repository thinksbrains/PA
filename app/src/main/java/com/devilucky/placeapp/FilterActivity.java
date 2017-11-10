package com.devilucky.placeapp;

import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.Rect;
import android.os.AsyncTask;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.drawable.GlideDrawable;
import com.bumptech.glide.request.animation.GlideAnimation;
import com.bumptech.glide.request.target.SimpleTarget;
import com.devilucky.placeapp.fragment.GoogleMaps;
import com.devilucky.placeapp.object.LocalesObj;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.concurrent.ExecutionException;

public class FilterActivity extends AppCompatActivity implements OnMapReadyCallback,GoogleMap.OnInfoWindowClickListener {

    public static GoogleMaps fragmentmap;
    //LocalesObj[]locales;
    private String sector;
    FloatingActionButton fab;

    List<LocalesObj> locales;

    Bitmap []logos;

    HashMap<String,String> identificadores;
    private DatabaseReference mDatabase;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_filter);

        identificadores=new HashMap<>();

        mDatabase= FirebaseDatabase.getInstance().getReference();
        locales = new ArrayList<>();

        Bundle e = getIntent().getExtras();
        if(e!=null)sector =e.getString("sector");

        fab=(FloatingActionButton)findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(FilterActivity.this,ListaLocalesActivity.class);
                intent.putExtra("sector",sector);
                startActivity(intent);
            }
        });

        LoadData();

        fragmentmap=new GoogleMaps();
    }

    private void loadMap(){
        fragmentmap.getMapAsync(this);
        getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.conteiner,fragmentmap)
                .commit();
    }

    private void LoadData(){
        mDatabase.child(sector).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (DataSnapshot snapshot:
                     dataSnapshot.getChildren()) {
                    LocalesObj local=snapshot.getValue(LocalesObj.class);
                    local.setIdKey(snapshot.getKey());
                    locales.add(local);
                    if(locales!=null) loadMap();
                    else Toast.makeText(getApplicationContext(),"no hay locales",Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
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

        if(locales!=null) {
            final GoogleMap map=googleMap;
            for(int i=0;i<locales.size();i++) {
                final int coun=i;
                Thread thread = new Thread(new Runnable(){
                    Bitmap bmp;
                    @Override
                    public void run(){
                        URL url ;
                        try {
                            url = new URL(locales.get(coun).getLogo().replace(" ","%20"));
                            Log.e("logo",locales.get(coun).getLogo().replace(" ","%20"));
                            bmp = Glide.with(getApplication()).load(url)
                            .asBitmap().into(80,80).get(); //BitmapFactory.decodeStream(url.openStream());
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                LatLng latlon = new LatLng(locales.get(coun).getLat(),locales.get(coun).getLon());
                                Marker mark = map.addMarker(new MarkerOptions()
                                        .position(latlon)
                                        .title(locales.get(coun).getNombre())
                                        .snippet(locales.get(coun).getInfo())
                                .icon(BitmapDescriptorFactory.fromBitmap(getCroppedBitmap(bmp))));
                                identificadores.put(mark.getId()+"-uid",locales.get(coun).getIdKey());
                                identificadores.put(mark.getId()+"-sector",locales.get(coun).getSector());

                            }
                        });
                    }
                });
                thread.start();
            }

            googleMap.setOnInfoWindowClickListener(this);

            CameraPosition cameraPosition = CameraPosition.builder()
                    .target(new LatLng(-17.783354, -63.182189))
                    .zoom(16)
                    .build();

            googleMap.moveCamera(CameraUpdateFactory.newCameraPosition(cameraPosition));

        }
    }

    public Bitmap getCroppedBitmap(Bitmap bitmap) {
        Bitmap output = Bitmap.createBitmap(bitmap.getWidth(),
                bitmap.getHeight(), Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(output);

        final int color = 0xff424242;
        final Paint paint = new Paint();
        final Rect rect = new Rect(0, 0, bitmap.getWidth(), bitmap.getHeight());

        paint.setAntiAlias(true);
        canvas.drawARGB(0, 0, 0, 0);
        paint.setColor(color);
        // canvas.drawRoundRect(rectF, roundPx, roundPx, paint);
        canvas.drawCircle(bitmap.getWidth() / 2, bitmap.getHeight() / 2,
                bitmap.getWidth() / 2, paint);
        paint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.SRC_IN));
        canvas.drawBitmap(bitmap, rect, rect, paint);
        //Bitmap _bmp = Bitmap.createScaledBitmap(output, 60, 60, false);
        //return _bmp;
        return output;
    }

    @Override
    public void onInfoWindowClick(Marker marker) {
        Intent intent = new Intent(this, InfoActivity.class);
        intent.putExtra("id", identificadores.get(marker.getId()+"-uid"));
        intent.putExtra("sector", identificadores.get(marker.getId()+"-sector"));
        //intent.putExtra("position", posisionesR[pos]);
        startActivity(intent);
    }
}