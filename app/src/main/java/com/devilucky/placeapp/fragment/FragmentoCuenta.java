package com.devilucky.placeapp.fragment;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.ViewPager;
import android.util.Base64;
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
import com.devilucky.placeapp.adapters.AdaptadorComentarios;
import com.devilucky.placeapp.object.ComentOBJ;
import com.devilucky.placeapp.object.LocalesObj;
import com.devilucky.placeapp.object.ObjUser;
import com.devilucky.placeapp.utils.JSONParser;
import com.devilucky.placeapp.utils.LoginMod;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.lang.reflect.Field;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Hakos on 08/06/2017.
 */

public class FragmentoCuenta extends Fragment {
    private AppBarLayout appBar;
    private TabLayout tabLayout;
    private static ViewPager viewPager;
    private static ViewGroup container;

    public static SharedPreferences user;

    public FragmentoCuenta(){}
    public static ObjUser[]usuarios;
    public static LocalesObj[]locales;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle saved){
        View v = inflater.inflate(R.layout.fragment_paginado, container, false);
        this.container=container;
        user = getActivity().getSharedPreferences("user", Context.CONTEXT_RESTRICTED);
        if (saved==null){
            insertarTabs(container);

            viewPager = (ViewPager)v.findViewById(R.id.viewpager);
        }
        if (user.getInt("type",0)==2){
            loadData();
        }else{
            poblarViewPager(viewPager);
            tabLayout.setupWithViewPager(viewPager);
        }
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
                                usuarios=new ObjUser[cali.length()];
                                for(int i=0;i<cali.length();i++){
                                    JSONObject c=cali.getJSONObject(i);
                                    usuarios[i]=new ObjUser(
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
                                if(usuarios!=null){
                                    poblarViewPager(viewPager);
                                    tabLayout.setupWithViewPager(viewPager);
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

        /*requestQueue = Volley.newRequestQueue(getActivity());
        json = new JsonObjectRequest(
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
                                if(locales!=null){
                                    poblarViewPager(viewPager);
                                    tabLayout.setupWithViewPager(viewPager);
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
        requestQueue.add(json);*/
    }

    private void poblarViewPager(ViewPager viewPager) {
        if (user.getInt("type",0)==0) {
            AdaptadorSecciones adapter = new AdaptadorSecciones(getFragmentManager());
            adapter.addFragment(new FragmentoPerfil(), "Perfil");
            adapter.addFragment(new FragmentoPerfil(), "Metodos de pago");
            viewPager.setAdapter(adapter);
        }else if (user.getInt("type",0)==1){
            AdaptadorSecciones adapter = new AdaptadorSecciones(getFragmentManager());
            adapter.addFragment(new FragmentoPerfil(), "Perfil");
            adapter.addFragment(new FragmentoPerfil(), "Metodos de pago");
            adapter.addFragment(new FragmentoPerfil(), "Informacion de negocios");
            adapter.addFragment(new FragmentoPerfil(), "Gestion de negocios");
            viewPager.setAdapter(adapter);
        }else if (user.getInt("type",0)==2){
            AdaptadorSecciones adapter = new AdaptadorSecciones(getFragmentManager());
            adapter.addFragment(new FragmentoPerfil(), "Perfil");
            adapter.addFragment(new FragmentoGestionU(), "Usuarios");
            adapter.addFragment(new FragmentoGestionN(), "negocios");
            adapter.addFragment(new FregmentoRegistroN(), "Registrar negocios");
            viewPager.setAdapter(adapter);
        }
    }

    private void insertarTabs(ViewGroup container) {
        View padre = (View)container.getParent();
        appBar = (AppBarLayout)padre.findViewById(R.id.appbar);
        tabLayout = new TabLayout(getActivity());
        tabLayout.setTabTextColors(Color.parseColor("#ffffff"),Color.parseColor("#ffffff"));
        appBar.addView(tabLayout);
    }
    @Override
    public void onDestroyView(){
        super.onDestroyView();
        appBar.removeView(tabLayout);
    }

    public class AdaptadorSecciones extends FragmentStatePagerAdapter {
        private final List<Fragment> fragmentos = new ArrayList<>();
        private final List<String> titulosFragmentos = new ArrayList<>();

        public AdaptadorSecciones(FragmentManager fm) {
            super(fm);
        }

        @Override
        public android.support.v4.app.Fragment getItem(int position) {
            return fragmentos.get(position);
        }

        @Override
        public int getCount() {
            return fragmentos.size();
        }

        public void addFragment(android.support.v4.app.Fragment fragment, String title) {
            fragmentos.add(fragment);
            titulosFragmentos.add(title);
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return titulosFragmentos.get(position);
        }
    }


    @Override
    public void onDetach() {
        super.onDetach();

        try {
            Field childFragmentManager = Fragment.class.getDeclaredField("mChildFragmentManager");
            childFragmentManager.setAccessible(true);
            childFragmentManager.set(this, null);

        } catch (NoSuchFieldException e) {
            throw new RuntimeException(e);
        } catch (IllegalAccessException e) {
            throw new RuntimeException(e);
        }
    }
}
