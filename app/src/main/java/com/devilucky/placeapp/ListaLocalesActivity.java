package com.devilucky.placeapp;

import android.support.v4.app.FragmentManager;
import android.support.v4.view.MenuItemCompat;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
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
import com.devilucky.placeapp.adapters.AdaptadorComidas;
import com.devilucky.placeapp.fragment.FragmentCategorias;
import com.devilucky.placeapp.fragment.FragmentComida;
import com.devilucky.placeapp.object.LocalesObj;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class ListaLocalesActivity extends AppCompatActivity implements SearchView.OnQueryTextListener{

    private RecyclerView recyclerView;
    private GridLayoutManager gridLayoutManager;
    private AdaptadorComidas adapter;
    //private LocalesObj[] locales;
    private SwipeRefreshLayout swip;
    private static String sector;

    Toolbar toolbar;

    private List<LocalesObj> locales;
    DatabaseReference mDatabase;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lista_locales);

        toolbar=(Toolbar)findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        Bundle e=getIntent().getExtras();
        if(e!=null)sector=e.getString("sector");


        swip = (SwipeRefreshLayout)findViewById(R.id.swipe);
        //locales = new MainActivity().locales;
        recyclerView=(RecyclerView)findViewById(R.id.reciclador);
        gridLayoutManager = new GridLayoutManager(this,1);
        recyclerView.setLayoutManager(gridLayoutManager);

        locales =new ArrayList<>();
        mDatabase= FirebaseDatabase.getInstance().getReference();

        adapter = new AdaptadorComidas(locales, this, sector);
        recyclerView.setAdapter(adapter);

        swip.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                loadJson();
            }
        });


        loadJson();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_search,menu);
        MenuItem search = menu.findItem(R.id.action_search);
        SearchView sv = (SearchView) MenuItemCompat.getActionView(search);
        sv.setOnQueryTextListener(this);
        return true;
    }

    public void loadJson(){

        mDatabase.child(sector).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                locales.removeAll(locales);
                for (DataSnapshot snapshot:
                     dataSnapshot.getChildren()) {
                    LocalesObj local = snapshot.getValue(LocalesObj.class);
                    local.setIdKey(snapshot.getKey());
                    locales.add(local);
                }
                adapter.notifyDataSetChanged();
                swip.setRefreshing(false);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Log.e("error update",databaseError.getMessage());
            }
        });

        /*RequestQueue requestQueue = Volley.newRequestQueue(this);
        JsonObjectRequest json = new JsonObjectRequest(
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
                                            c.getDouble("lon")
                                    );
                                }

                                if(locales!=null) {
                                    loadSectors();
                                }else Toast.makeText(getApplicationContext(),"No existen Locales",Toast.LENGTH_SHORT).show();

                            }else Toast.makeText(getApplicationContext(),response.getString("mensaje"),Toast.LENGTH_SHORT).show();
                            swip.setRefreshing(false);
                        } catch (JSONException e) {
                            Log.e("error","errorJson "+e.getMessage());
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.e("error", "Error Respuesta en JSON: " + error.getMessage());
                        swip.setRefreshing(false);
                    }
                });
        requestQueue.add(json);*/
    }
    private void loadSectors(){
        /*int cont=0;
        LocalesObj[]loc;
        for (int i =0;i<locales.length;i++){
            if (locales[i].getSector().equals(sector))
                cont++;
        }
        loc=new LocalesObj[cont];
        int pos=0;
        for (int i=0;i<locales.length;i++) {
            if(locales[i].getSector().equals(sector)) {
                loc[pos] = locales[i];
                pos++;
            }
        }
        adapter = new AdaptadorComidas(loc, this, sector,cont);
        recyclerView.setAdapter(adapter);
        /*int cont=0;
        int pos=0;
        int[]posisionesR;
        if (locales!=null) {
            switch (seccion) {
                case 0:
                    LocalesObj[] locCR;
                    for (int i = 0; i < locales.length; i++) {
                        if (locales[i].getSector().equals("comida rapida")) cont++;
                    }
                    locCR = new LocalesObj[cont];
                    posisionesR = new int[cont];
                    for (int i = 0; i < locales.length; i++) {
                        if (locales[i].getSector().equals("comida rapida")) {
                            locCR[pos] = locales[i];
                            posisionesR[pos] = i;
                            pos++;
                        }
                    }
                    adapter = new AdaptadorComidas(locCR, getContext(), "comida rapida", posisionesR);
                    break;
                case 1:
                    LocalesObj[] locC;
                    for (int i = 0; i < locales.length; i++) {
                        if (locales[i].getSector().equals("cafeteria"))
                            cont++;
                    }
                    locC = new LocalesObj[cont];
                    posisionesR = new int[cont];
                    for (int i = 0; i < locales.length; i++) {
                        if (locales[i].getSector().equals("cafeteria")) {
                            locC[pos] = locales[i];
                            posisionesR[pos] = i;
                            pos++;
                        }
                    }
                    adapter = new AdaptadorComidas(locC, getContext(), "cafeteria", posisionesR);
                    break;
            }
            recyclerView.setAdapter(adapter);
        }else Toast.makeText(getActivity(),"No existen Loacales",Toast.LENGTH_SHORT).show();*/
    }

    @Override
    public boolean onQueryTextSubmit(String query) {
        return false;
    }

    @Override
    public boolean onQueryTextChange(String newText) {
        String newtext = newText.toLowerCase();
        ArrayList<LocalesObj> newList = new ArrayList<>();
        for (LocalesObj local:
             locales) {
            String name = local.getNombre().toLowerCase();
            if(name.contains(newtext))
                newList.add(local);
        }
        adapter.filter(newList);
        return true;
    }
}
