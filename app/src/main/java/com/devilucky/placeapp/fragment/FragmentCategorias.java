package com.devilucky.placeapp.fragment;

import android.graphics.Color;
import android.os.Bundle;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.devilucky.placeapp.R;

import java.util.ArrayList;
import java.util.List;

public class FragmentCategorias extends Fragment {

    private AppBarLayout appBar;
    private TabLayout tabLayout;
    private ViewPager viewPager;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_fragment_categorias, container, false);
        if (savedInstanceState==null){
            insertarTabs(container);

            viewPager = (ViewPager)v.findViewById(R.id.viewpager);
            poblarViewPager(viewPager);
            tabLayout.setupWithViewPager(viewPager);
        }
        return v;
    }
    private void poblarViewPager(ViewPager viewPager){
        AdaptadorSecciones adaptadorSecciones=new AdaptadorSecciones(getFragmentManager());
        adaptadorSecciones.addFragment(Categorias.newInstance(0),"Lugares");
        adaptadorSecciones.addFragment(PromocionesFragment.newInstance(0),"Promociones");
        adaptadorSecciones.addFragment(EventosFragment.newInstance(0),"Eventos");
        viewPager.setAdapter(adaptadorSecciones);
    }

    private void insertarTabs(ViewGroup container) {
        View padre = (View)container.getParent();
        appBar = (AppBarLayout)padre.findViewById(R.id.appbar);
        tabLayout = new TabLayout(getActivity());
        tabLayout.setTabTextColors(Color.parseColor("#ffffff"),Color.parseColor("#ffffff"));
        appBar.addView(tabLayout);
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
}
