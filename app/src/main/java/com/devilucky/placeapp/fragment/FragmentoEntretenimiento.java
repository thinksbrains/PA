package com.devilucky.placeapp.fragment;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.devilucky.placeapp.R;
import com.devilucky.placeapp.object.LocalesObj;
import com.devilucky.placeapp.utils.JSONParser;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONObject;

import java.net.URL;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Hakos on 11/06/2017.
 */

public class FragmentoEntretenimiento extends Fragment{
    private AppBarLayout appBar;
    private TabLayout tabLayout;
    private ViewPager viewPager;
    public static LocalesObj[] locales;
    public static SharedPreferences user;

    public FragmentoEntretenimiento(){}

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle saved){
        View v = inflater.inflate(R.layout.fragment_paginado, container, false);
        user = getActivity().getSharedPreferences("user", Context.MODE_PRIVATE);
        if (saved==null){
            insertarTabs(container);
            viewPager = (ViewPager)v.findViewById(R.id.viewpager);
            poblarViewPager(viewPager);
            tabLayout.setupWithViewPager(viewPager);
        }
        //new Submit().execute();
        return v;
    }

    private void poblarViewPager(ViewPager viewPager) {
        AdaptadorSecciones adapter = new AdaptadorSecciones(getFragmentManager());
        adapter.addFragment(FragmentoEntret.newInstance(0), "cine");
        adapter.addFragment(FragmentoEntret.newInstance(1), "discotecas");
        viewPager.setAdapter(adapter);
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
            pd = new ProgressDialog(getActivity());
            pd.show();
            pd.setTitle("Descargando");
            pd.setMessage("Un momento porfavor...");
            pd.setIndeterminate(false);
            pd.setCancelable(false);
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
            if (dis) {
                //Toast.makeText(getApplicationContext(),msj,Toast.LENGTH_SHORT).show();
            } else{
                Toast.makeText(getActivity(),msj,Toast.LENGTH_SHORT).show();
            }
            pd.dismiss();
        }
    }*/
}
