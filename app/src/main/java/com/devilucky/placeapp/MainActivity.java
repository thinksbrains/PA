package com.devilucky.placeapp;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.NavigationView;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.view.GravityCompat;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.devilucky.placeapp.fragment.FragmentInicio;
import com.devilucky.placeapp.fragment.FragmentoBancos;
import com.devilucky.placeapp.fragment.FragmentoComidas;
import com.devilucky.placeapp.fragment.FragmentoCuenta;
import com.devilucky.placeapp.fragment.FragmentoEntretenimiento;
import com.devilucky.placeapp.object.LocalesObj;
import com.devilucky.placeapp.utils.JSONParser;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.net.URL;
import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {
    DrawerLayout drawer;
    public static SharedPreferences user;
    static NavigationView navigationView;

    private RequestQueue requestQueue;

    private AppBarLayout appbar;
    private TabLayout tabLayout;
    private ViewPager viewPager;
    FragmentManager frManager;

    public static LocalesObj[] locales;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        requestQueue = Volley.newRequestQueue(this);

        user = getSharedPreferences("user",CONTEXT_RESTRICTED);
        locales=SplashActivity.locales;

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        if (user.getInt("status",0)!=1){
            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setMessage("para una mejor experiencia inicia sesion o crea un usuario")
                    .setPositiveButton("Login", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            Intent intent = new Intent(MainActivity.this, CuentaActivity.class);
                            startActivity(intent);
                        }
                    })
                    .setNegativeButton("Register", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            startActivity(new Intent(MainActivity.this,ActivityRegister.class));
                        }
                    }).create().show();
        }

        drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        navigationView = (NavigationView) findViewById(R.id.nav_view);

        if (navigationView!=null){
            prepararDrawer(navigationView);
            seleccionarItem(navigationView.getMenu().getItem(0));
        }

        /*JsonObjectRequest json = new JsonObjectRequest(
                Request.Method.GET,
                getResources().getString(R.string.enlace) + "locales",
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            int state=response.getInt("estado");
                            if(state==1){
                                JSONArray cali = response.getJSONArray("locales");
                                locales=new LocalesObj[cali.length()];
                                for(int i=0;i<cali.length();i++){
                                    JSONObject c=cali.getJSONObject(i);
                                    locales[i]=new LocalesObj(
                                            c.getInt("id"),
                                            c.getString("id_usu"),
                                            c.getString("sector"),
                                            c.getString("nombre"),
                                            c.getString("descripcion"),
                                            c.getInt("rating"),
                                            c.getString("portada"),
                                            c.getString("logo"),
                                            c.getString("imagenes"),
                                            c.getDouble("lat"),
                                            c.getDouble("lon"),
                                            c.getInt("promo")
                                    );
                                }
                            }else Toast.makeText(getApplicationContext(),response.getString("mensaje"),Toast.LENGTH_SHORT).show();
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.e("error", "Error Respuesta en JSON: " + error.getMessage());
                    }
                });
        requestQueue.add(json);*/
        //new Submit().execute();
    }

    private void seleccionarItem(MenuItem item) {
        String tag="";
        Fragment frgenerico=null;
        frManager=getSupportFragmentManager();
        switch (item.getItemId()){
            case R.id.nav_inicio:
                frgenerico= new FragmentInicio();
                tag="inicio";
                break;
            case R.id.nav_entretenimiento:
                    frgenerico = new FragmentoEntretenimiento();
                break;
            case R.id.nav_bancos:
                frgenerico = new FragmentoBancos();
                break;
            case R.id.nav_comida:
                frgenerico = new FragmentoComidas();
                tag="comida";
                break;
            case R.id.nav_servicios:
                break;
            case R.id.nav_mi_cuenta:
                if (user.getInt("status",0)==1){
                    frgenerico = new FragmentoCuenta();
                    tag="user";
                }else {
                    Intent intent = new Intent(MainActivity.this, CuentaActivity.class);
                    startActivity(intent);
                }
                break;
            case R.id.nav_preferencias:
                break;
            case R.id.nav_salir:
                SharedPreferences.Editor e = user.edit();
                e.putInt("status",0);
                e.commit();
                break;
        }
        if (frgenerico!=null){
            frManager
                    .beginTransaction()
                    .replace(R.id.contenedor_principal,frgenerico,tag)
                    .addToBackStack(null)
                    .commit();
        }
    }

    private void prepararDrawer(NavigationView navigationView) {
        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                item.setChecked(true);
                seleccionarItem(item);
                drawer.closeDrawers();
                return true;
            }
        });
    }


    @Override
    public void onBackPressed() {
        if (frManager.getBackStackEntryCount()>1) {
            frManager.popBackStack();
        } else {
            finish();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
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
        /*switch(id){
            case R.id.action_comida:
                frgenerico = new FragmentoComidas();
                break;
            case R.id.action_entretenimiento:
                frgenerico = new FragmentoEntretenimiento();
                break;
            case R.id.action_banco:
                frgenerico = new FragmentoBancos();
                break;
        }
        if(frgenerico!=null){
            frManager
                    .beginTransaction()
                    .replace(R.id.contenedor_principal,frgenerico)
                    .addToBackStack(null)
                    .commit();
        }*/

        return super.onOptionsItemSelected(item);
    }
}
