package com.devilucky.placeapp.fragment;

import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.devilucky.placeapp.R;
import com.devilucky.placeapp.adapters.AdaptadorInicio;
import com.devilucky.placeapp.adapters.AdapterGestion;
import com.devilucky.placeapp.object.LocalesObj;
import com.devilucky.placeapp.object.ObjUser;
import com.devilucky.placeapp.utils.JSONParser;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.net.URL;
import java.util.ArrayList;

/**
 * Created by Hakos on 10/06/2017.
 */

public class FragmentoGestionU extends Fragment {

    private RecyclerView reciclador;
    private LinearLayoutManager layoutManager;
    private AdapterGestion adapter;
    private ObjUser[] users;
    private SwipeRefreshLayout swip;
    public FragmentoGestionU(){users=new FragmentoCuenta().usuarios;}

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup parent, Bundle saved){
        View v = inflater.inflate(R.layout.fragmento_gestion,parent,false);

        swip=(SwipeRefreshLayout)v.findViewById(R.id.swipe);

        reciclador = (RecyclerView)v.findViewById(R.id.reciclador);
        layoutManager = new LinearLayoutManager(getActivity());
        reciclador.setLayoutManager(layoutManager);
        adapter = new AdapterGestion(users,getActivity());
        reciclador.setAdapter(adapter);

        swip.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                loadData();
            }
        });

        return v;
    }

    private void loadData(){
        RequestQueue requestQueue = Volley.newRequestQueue(getActivity());
        JsonObjectRequest json = new JsonObjectRequest(
                Request.Method.GET,
                getResources().getString(R.string.enlace) + "usuarios",
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            int state=response.getInt("estado");
                            if(state==1){
                                JSONArray cali = response.getJSONArray("usuarios");
                                users=new ObjUser[cali.length()];
                                for(int i=0;i<cali.length();i++){
                                    JSONObject c=cali.getJSONObject(i);
                                    users[i]=new ObjUser(
                                            c.getInt("id"),
                                            c.getInt("type"),
                                            c.getString("user"),
                                            c.getString("fullname"),
                                            c.getString("email"),
                                            c.getString("telf"),
                                            c.getString("perfil"),
                                            c.getString("datereg")
                                    );
                                }
                                if(users!=null){
                                    adapter = new AdapterGestion(users,getActivity());
                                    reciclador.setAdapter(adapter);
                                    swip.setRefreshing(false);
                                }
                            }else Toast.makeText(getActivity(),response.getString("mensaje"),Toast.LENGTH_SHORT).show();
                        } catch (JSONException e) {
                            Log.e("error","errorJson "+e.getMessage());
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.e("error", "Error Respuesta en JSON: " + error.getMessage());
                    }
                });
        requestQueue.add(json);
    }
}
