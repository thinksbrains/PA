package com.devilucky.placeapp.fragment;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.devilucky.placeapp.R;
import com.devilucky.placeapp.adapters.AdapterEventos;
import com.devilucky.placeapp.object.ObjEventos;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class EventosFragment extends Fragment {
    private static final String TAG_INDICE="extra_seccion";

    private RecyclerView reciclador;
    private GridLayoutManager gridLayoutManager;
    private AdapterEventos adapter;
    private SwipeRefreshLayout refresh;

    private List<ObjEventos> eventos;

    private DatabaseReference mDatabase;

    public EventosFragment() {
        // Required empty public constructor
    }
    public static EventosFragment newInstance(int indiceSeccion) {
        EventosFragment fragment = new EventosFragment();
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

        eventos=new ArrayList<>();

        refresh=(SwipeRefreshLayout)v.findViewById(R.id.refresh);
        refresh.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                loadData();
            }
        });

        reciclador = (RecyclerView) v.findViewById(R.id.reciclador);
        gridLayoutManager=new GridLayoutManager(getActivity(),1);
        adapter = new AdapterEventos(getActivity(),eventos);
        reciclador.setLayoutManager(gridLayoutManager);
        reciclador.setAdapter(adapter);

        loadData();
        return v;
    }

    private void loadData(){
        mDatabase.child("Evento").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                eventos.removeAll(eventos);
                for (DataSnapshot snapshot:
                        dataSnapshot.getChildren()) {
                    ObjEventos evento=snapshot.getValue(ObjEventos.class);
                    evento.setIdKey(snapshot.getKey());
                    eventos.add(evento);
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
