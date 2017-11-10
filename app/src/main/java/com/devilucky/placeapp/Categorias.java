package com.devilucky.placeapp;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import com.devilucky.placeapp.fragment.FragmentCategorias;
import com.devilucky.placeapp.fragment.FragmentoBancos;
import com.devilucky.placeapp.fragment.FragmentoComidas;
import com.devilucky.placeapp.fragment.FragmentoEntretenimiento;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class Categorias extends AppCompatActivity {

    SharedPreferences admin;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_categorias);

        admin = getSharedPreferences("admin",CONTEXT_RESTRICTED);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FragmentManager fragmentManager=getSupportFragmentManager();
        FragmentCategorias fragmentCategorias=new FragmentCategorias();
        fragmentManager.beginTransaction().replace(R.id.contenedor_principal,fragmentCategorias).addToBackStack(null).commit();

    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        MenuItem item = menu.findItem(R.id.action_add);
        if(admin.getInt("admin",0)==3) {
            Log.e("menu","entro if");
            item.setVisible(true);
        }
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        Fragment frgenerico=null;

        //noinspection SimplifiableIfStatement
        switch(id){
            case R.id.action_fav:
                frgenerico = new FragmentoComidas();
                break;
            case R.id.action_menu:
                startActivity(new Intent(Categorias.this,ConfigActivity.class));
                break;
            case R.id.action_add:
                final String[]opc = new String[]{"Local","Evento","Promocion"};
                AlertDialog.Builder dialog = new AlertDialog.Builder(this);
                dialog.setTitle("Seleccione")
                        .setItems(opc, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                switch(which){
                                    case 0:
                                        startActivity(new Intent(Categorias.this,RegistrarLocales.class).putExtra("method","local"));
                                        break;
                                    case 1:
                                        startActivity(new Intent(Categorias.this,RegistrarLocales.class).putExtra("method","Evento"));
                                        break;
                                    case 2:
                                        startActivity(new Intent(Categorias.this,RegistrarLocales.class).putExtra("method","Promocion"));
                                        break;
                                }
                            }
                        }).create().show();
                break;
        }

        return super.onOptionsItemSelected(item);
    }
}
