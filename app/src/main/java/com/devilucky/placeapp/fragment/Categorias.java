package com.devilucky.placeapp.fragment;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.devilucky.placeapp.R;
import com.devilucky.placeapp.adapters.AdapterCategorias;
import com.devilucky.placeapp.object.ObjCategorias;

public class Categorias extends Fragment {
    private static final String TAG_INDICE="extra_seccion";
    private RecyclerView recyclerView;
    private ObjCategorias[]categorias;

    public static Categorias newInstance(int indiceSeccion){
        Categorias fragment = new Categorias();
        Bundle arg = new Bundle();
        arg.putInt(TAG_INDICE,indiceSeccion);
        fragment.setArguments(arg);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_categorias,container,false);

        categorias = new ObjCategorias[]{
                new ObjCategorias("Restaurantes",R.drawable.restauran),
                new ObjCategorias("Boliches",R.drawable.discos),
                new ObjCategorias("Cines",R.drawable.cines),
                new ObjCategorias("Bancos",R.drawable.bancos),
                new ObjCategorias("Turismo",R.drawable.turismo),
                new ObjCategorias("Shopping",R.drawable.shopping),
        };
        recyclerView = (RecyclerView)v.findViewById(R.id.reciclador);
        GridLayoutManager gridLayoutManager = new GridLayoutManager(getActivity(),2);
        recyclerView.setLayoutManager(gridLayoutManager);
        AdapterCategorias adapter = new AdapterCategorias(categorias,getActivity());
        recyclerView.setAdapter(adapter);

        return v;
    }
}
