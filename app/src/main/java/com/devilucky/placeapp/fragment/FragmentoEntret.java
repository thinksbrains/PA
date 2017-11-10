package com.devilucky.placeapp.fragment;

import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.GridLayoutManager;
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
import com.devilucky.placeapp.MainActivity;
import com.devilucky.placeapp.R;
import com.devilucky.placeapp.adapters.AdaptadorComidas;
import com.devilucky.placeapp.object.LocalesObj;
import com.devilucky.placeapp.utils.JSONParser;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.net.URL;
import java.util.ArrayList;

/**
 * Created by Hakos on 11/06/2017.
 */

public class FragmentoEntret extends Fragment {
    private static final String TAG_INDICE="extra_seccion";
    private RecyclerView recyclerView;
    private GridLayoutManager gridLayoutManager;
    private AdaptadorComidas adapter;
    private LocalesObj[] locales;
    private SwipeRefreshLayout swip;
    int seccion;

    public static FragmentoEntret newInstance(int indiceSeccion){
        FragmentoEntret fragment = new FragmentoEntret();
        Bundle arg = new Bundle();
        arg.putInt(TAG_INDICE,indiceSeccion);
        fragment.setArguments(arg);
        return fragment;
    }

    public FragmentoEntret (){}

    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle saved){
        View v = inflater.inflate(R.layout.fragment_comidas,container,false);
        swip = (SwipeRefreshLayout)v.findViewById(R.id.swipe);
        locales = new MainActivity().locales;
        recyclerView=(RecyclerView)v.findViewById(R.id.reciclador);
        gridLayoutManager = new GridLayoutManager(getActivity(),2);
        recyclerView.setLayoutManager(gridLayoutManager);
        /*swip.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                new Submit().execute();
            }
        });*/

        seccion=0;
        Bundle arg=getArguments();
        if (arg!=null) {
            seccion = arg.getInt(TAG_INDICE,0);
        }

        //loadJson();
        return v;
    }

    public void loadSectors(){
        /*int cont=0;
        int pos=0;
        int[]posisionesR;
        if (locales!=null) {
            switch (seccion) {
                case 0:
                    LocalesObj[] locC;
                    for (int i = 0; i < locales.length; i++) {
                        if (locales[i].getSector().equals("cine"))
                            cont++;
                    }
                    locC = new LocalesObj[cont];
                    posisionesR = new int[cont];
                    for (int i = 0; i < locales.length; i++) {
                        if (locales[i].getSector().equals("cine")) {
                            locC[pos] = locales[i];
                            posisionesR[pos] = i;
                            pos++;
                        }
                    }
                    adapter = new AdaptadorComidas(locC, getContext(), "cine", posisionesR);
                    break;
                case 1:
                    LocalesObj[] locD;
                    cont = 0;
                    for (int i = 0; i < locales.length; i++) {
                        if (locales[i].getSector().equals("discotecas"))
                            cont++;
                    }
                    locD = new LocalesObj[cont];
                    posisionesR = new int[cont];
                    for (int i = 0; i < locales.length; i++) {
                        if (locales[i].getSector().equals("discotecas")) {
                            locD[pos] = locales[i];
                            posisionesR[pos] = i;
                            pos++;
                        }
                    }
                    adapter = new AdaptadorComidas(locD, getContext(), "discotecas", posisionesR);
                    break;
            }
            recyclerView.setAdapter(adapter);
        }else Toast.makeText(getActivity(),"No existen Loacales",Toast.LENGTH_SHORT).show();*/
    }

    /*public void loadJson(){
        RequestQueue requestQueue = Volley.newRequestQueue(getActivity());
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
                                            c.getDouble("lon"),
                                            c.getInt("promo")
                                    );
                                }

                                if(locales!=null) {
                                    loadSectors();
                                }else Toast.makeText(getActivity(),"No existen Locales",Toast.LENGTH_SHORT).show();

                            }else Toast.makeText(getActivity(),response.getString("mensaje"),Toast.LENGTH_SHORT).show();
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
        requestQueue.add(json);
    }*/

    /*class Submit extends AsyncTask<URL,Void,Void> {
        ProgressDialog pd;
        Boolean dis = false;
        byte[] byteImg;
        String msj="Error en la carga",url,urln;

        public Submit(){}

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            urln = "http://publink.esy.es/placeapp/negocios.php";
        }

        @Override
        protected Void doInBackground(URL... params) {
            ArrayList<NameValuePair> parameterss = new ArrayList<NameValuePair>();
            parameterss.add(new BasicNameValuePair("flag", "admin"));
            JSONObject json = JSONParser.makeHttpRequest(urln, "POST", parameterss);
            try {
                int success = json.getInt("success");
                if (success == 1) {
                    JSONArray cali = json.getJSONArray("negocios");
                    locales = new LocalesObj[cali.length()];
                    for(int i=0; i<cali.length();i++){
                        JSONObject c = cali.getJSONObject(i);
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
                        dis = true;
                    }
                }
            } catch (Exception e) {
                Log.e("Exception", e.getMessage());
            }
            return null;
        }

        @Override
        protected void onPostExecute(Void publi) {
            /*if (dis) {
                int[]posisionesR;
                int cont=0;
                int pos=0;
                if (locales!=null) {
                    switch (seccion) {
                        case 0:
                            LocalesObj[] locC;
                            for (int i = 0; i < locales.length; i++) {
                                if (locales[i].getSector().equals("cine"))
                                    cont++;
                            }
                            locC = new LocalesObj[cont];
                            posisionesR = new int[cont];
                            for (int i = 0; i < locales.length; i++) {
                                if (locales[i].getSector().equals("cine")) {
                                    locC[pos] = locales[i];
                                    posisionesR[pos] = i;
                                    pos++;
                                }
                            }
                            adapter = new AdaptadorComidas(locC, getContext(), "cine", posisionesR);
                            break;
                        case 1:
                            LocalesObj[] locD;
                            cont = 0;
                            for (int i = 0; i < locales.length; i++) {
                                if (locales[i].getSector().equals("discotecas"))
                                    cont++;
                            }
                            locD = new LocalesObj[cont];
                            posisionesR = new int[cont];
                            for (int i = 0; i < locales.length; i++) {
                                if (locales[i].getSector().equals("discotecas")) {
                                    locD[pos] = locales[i];
                                    posisionesR[pos] = i;
                                    pos++;
                                }
                            }
                            adapter = new AdaptadorComidas(locD, getContext(), "discotecas", posisionesR);
                            break;
                    }
                    recyclerView.setAdapter(adapter);

                }
            } else{
                Toast.makeText(getActivity(),msj,Toast.LENGTH_SHORT).show();
            }swip.setRefreshing(false);
        }
    }*/
}
