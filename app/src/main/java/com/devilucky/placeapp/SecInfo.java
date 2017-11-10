package com.devilucky.placeapp;

import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

import com.devilucky.placeapp.fragment.FragmentoComentar;
import com.devilucky.placeapp.fragment.FragmentoProductos;
import com.devilucky.placeapp.fragment.GoogleMaps;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;

public class SecInfo extends AppCompatActivity {

    private String carga;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sec_info);

        Bundle e =getIntent().getExtras();
        if(e!=null) carga=e.getString("carga");

        Fragment generico = null;

        switch(carga){
            case "productos":
                generico=new FragmentoProductos();
                break;
            case "comentarios":
                generico=new FragmentoComentar();
                break;
            case "ubicacion":
                generico=new InfoActivity().fragmentmap;
                break;
            case "compartir":
                break;
        }

        if (generico!=null){
            getSupportFragmentManager()
                    .beginTransaction()
                    .replace(R.id.conteiner,generico)
                    .commit();
        }

    }
}
