package com.devilucky.placeapp;

import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;

import com.devilucky.placeapp.fragment.FragmentEvenPromo;
import com.devilucky.placeapp.fragment.FregmentoRegistroN;

public class RegistrarLocales extends AppCompatActivity {

    public static String reg;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registrar_locales);

        Bundle e= getIntent().getExtras();
        if(e!=null) reg=e.getString("method");

        if(reg.equalsIgnoreCase("local")) {
            FragmentManager frmanager = getSupportFragmentManager();
            FregmentoRegistroN fr = new FregmentoRegistroN();
            frmanager.beginTransaction().replace(R.id.container, fr).commit();
        }else{
            FragmentManager frmanager = getSupportFragmentManager();
            FragmentEvenPromo fr = new FragmentEvenPromo();
            frmanager.beginTransaction().replace(R.id.container, fr).commit();
        }
    }
}
