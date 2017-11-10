package com.devilucky.placeapp.fragment;

import android.app.ProgressDialog;
import android.graphics.BitmapFactory;
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
import com.devilucky.placeapp.adapters.AdaptadorInicio;
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
 * Created by Hakos on 08/06/2017.
 */

public class FragmentComida extends Fragment {
    private static final String TAG_INDICE="extra_seccion";
    private RecyclerView recyclerView;
    private GridLayoutManager gridLayoutManager;
    private AdaptadorComidas adapter;
    private LocalesObj[] locales;
    private SwipeRefreshLayout swip;
    int seccion;

    public static FragmentComida newInstance(int indiceSeccion){
        FragmentComida fragment = new FragmentComida();
        Bundle arg = new Bundle();
        arg.putInt(TAG_INDICE,indiceSeccion);
        fragment.setArguments(arg);
        return fragment;
    }

    public FragmentComida (){}

    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle saved){
        View v = inflater.inflate(R.layout.fragment_comidas,container,false);
        swip = (SwipeRefreshLayout)v.findViewById(R.id.swipe);
        //locales = new MainActivity().locales;
        recyclerView=(RecyclerView)v.findViewById(R.id.reciclador);
        gridLayoutManager = new GridLayoutManager(getActivity(),1);
        recyclerView.setLayoutManager(gridLayoutManager);

        swip.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                //loadJson();
            }
        });

        seccion=0;
        Bundle arg=getArguments();
        if (arg!=null) {
            seccion = arg.getInt(TAG_INDICE,0);
        }

        return v;
    }



    private void loadSectors(){
        int cont=0;
        for (int i =0;i<locales.length;i++){
            if (locales[i].getSector().equals("restaurantes"))
                cont++;

        }
        /*adapter = new AdaptadorComidas(locales, getContext(), "restaurantes",cont);
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
}
