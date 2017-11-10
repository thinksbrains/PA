package com.devilucky.placeapp.fragment;

import android.content.Context;
import android.net.Uri;
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

import com.devilucky.placeapp.R;
import com.devilucky.placeapp.adapters.AdapterPromo;
import com.devilucky.placeapp.object.LocalesObj;
import com.devilucky.placeapp.object.ObjPromo;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class PromocionesFragment extends Fragment {
    private static final String TAG_INDICE="extra_seccion";

    private RecyclerView reciclador;
    private GridLayoutManager gridLayoutManager;
    private AdapterPromo adapter;
    private SwipeRefreshLayout refresh;

    private List<ObjPromo> promociones;

    private DatabaseReference mDatabase;

    public PromocionesFragment() {
        // Required empty public constructor
    }

    public static PromocionesFragment newInstance(int indiceSeccion) {
        PromocionesFragment fragment = new PromocionesFragment();
        Bundle arg = new Bundle();
        arg.putInt(TAG_INDICE,indiceSeccion);
        fragment.setArguments(arg);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mDatabase = FirebaseDatabase.getInstance().getReference();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_promociones, container, false);

        promociones=new ArrayList<>();

        refresh=(SwipeRefreshLayout)v.findViewById(R.id.refresh);
        refresh.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                loadData();
            }
        });

        reciclador = (RecyclerView) v.findViewById(R.id.reciclador);
        gridLayoutManager=new GridLayoutManager(getActivity(),1);
        adapter = new AdapterPromo(getActivity(),promociones);
        reciclador.setLayoutManager(gridLayoutManager);
        reciclador.setAdapter(adapter);

        loadData();

        return v;
    }

    private void loadData(){
        mDatabase.child("Promocion").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                promociones.removeAll(promociones);
                for (DataSnapshot snapshot:
                        dataSnapshot.getChildren()) {
                    ObjPromo promo=snapshot.getValue(ObjPromo.class);
                    promo.setIdKey(snapshot.getKey());
                    promociones.add(promo);
                }
                adapter.notifyDataSetChanged();
                refresh.setRefreshing(false);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }
}
