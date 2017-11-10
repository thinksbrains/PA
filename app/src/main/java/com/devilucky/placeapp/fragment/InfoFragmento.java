package com.devilucky.placeapp.fragment;

import android.content.Intent;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.devilucky.placeapp.InfoActivity;
import com.devilucky.placeapp.R;
import com.devilucky.placeapp.SecInfo;
import com.devilucky.placeapp.adapters.AdaptadorComentarios;
import com.devilucky.placeapp.object.ComentOBJ;
import com.devilucky.placeapp.object.LocalesObj;
import com.devilucky.placeapp.object.ObjPromo;
import com.facebook.share.model.ShareLinkContent;
import com.facebook.share.widget.ShareDialog;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Hakos on 09/06/2017.
 */

public class InfoFragmento extends Fragment implements View.OnClickListener{

    ImageView portada,logo;
    TextView nombre,descript;
    RatingBar ration;
    public static LocalesObj local;
    List<ComentOBJ> coments;
    DatabaseReference mDatabase;

    private RecyclerView reciclador;
    private LinearLayoutManager layoutManager;
    private AdaptadorComentarios adapter;
    private LinearLayout cont_images;
    ImageView[]imagesView;
    String[] urlsImages;
    List<String> urlsImg;

    private ImageView bt_productos,bt_comentarios,bt_ubicacion,bt_compartir;

    private TextView tx_masinfo,tx_info;
    private ImageView bt_mas;
    private boolean band=false;

    public InfoFragmento(){}

    public View onCreateView(LayoutInflater inflater, ViewGroup parent, Bundle saved){
        View v = inflater.inflate(R.layout.new_design_moreinfo,parent,false);
        local = new InfoActivity().local;

        portada=(ImageView)v.findViewById(R.id.img_portada);
        logo=(ImageView)v.findViewById(R.id.img_logo);
        nombre=(TextView)v.findViewById(R.id.tx_nombre);
        descript=(TextView)v.findViewById(R.id.tx_descript);
        ration=(RatingBar)v.findViewById(R.id.rating);

        tx_masinfo=(TextView)v.findViewById(R.id.tx_masinfo);
        tx_info=(TextView)v.findViewById(R.id.tx_info);
        bt_mas=(ImageView)v.findViewById(R.id.imagen_flecha);

        tx_masinfo.setOnClickListener(this);
        bt_mas.setOnClickListener(this);


        bt_productos=(ImageView)v.findViewById(R.id.bt_productos);
        bt_comentarios=(ImageView)v.findViewById(R.id.bt_comentarios);
        bt_ubicacion=(ImageView)v.findViewById(R.id.bt_ubicacion);
        bt_compartir=(ImageView)v.findViewById(R.id.bt_compartir);
        cont_images=(LinearLayout)v.findViewById(R.id.cont_images);

        coments=new ArrayList<>();
        urlsImg=new ArrayList<>();
        mDatabase= FirebaseDatabase.getInstance().getReference();

        bt_productos.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(getActivity(), SecInfo.class);
                intent.putExtra("carga","productos");
                getActivity().startActivity(intent);
            }
        });
        bt_comentarios.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(getActivity(), SecInfo.class);
                intent.putExtra("carga","comentarios");
                startActivity(intent);
            }
        });
        bt_ubicacion.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(getActivity(), SecInfo.class);
                intent.putExtra("carga","ubicacion");
                startActivity(intent);
            }
        });
        bt_compartir.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(Uri.parse(local.getUrlShare())!=null) {
                    ShareLinkContent content = new ShareLinkContent.Builder()
                            .setContentUrl(Uri.parse(local.getUrlShare()))
                            .build();
                    ShareDialog dialog = new ShareDialog(getActivity());
                    dialog.show(content, ShareDialog.Mode.AUTOMATIC);
                }else{
                    Toast.makeText(getActivity(), "No puede Compartir", Toast.LENGTH_SHORT).show();
                }
                /*Intent intent=new Intent(getActivity(), SecInfo.class);
                intent.putExtra("carga","compartir");
                startActivity(intent);*/
            }
        });

        loadImages();

        loadData();

        reciclador = (RecyclerView)v.findViewById(R.id.reciclador);
        layoutManager = new LinearLayoutManager(getActivity());
        reciclador.setLayoutManager(layoutManager);


        adapter = new AdaptadorComentarios(coments);
        reciclador.setAdapter(adapter);

        return v;
    }

    private void loadData(){
        Glide.with(getActivity())
                .load(local.getPortada())
                .asBitmap()
                .centerCrop()
                .into(portada);
        Glide.with(getActivity())
                .load(local.getLogo())
                .asBitmap()
                .centerCrop()
                .into(logo);
        nombre.setText(local.getNombre());
        descript.setText(local.getInfo());
        ration.setRating(local.getRating());
        //new Submit().execute();
        loadJson();
    }

    public void loadImages(){
        mDatabase.child("Promocion").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (DataSnapshot snap:
                     dataSnapshot.getChildren()) {
                    ObjPromo promo = snap.getValue(ObjPromo.class);
                    Log.e("explore data base", promo.getIdLocal()+":"+local.getIdKey());
                    if(promo.getIdLocal().equalsIgnoreCase(local.getIdKey())) {
                        Log.e("explore data base", promo.getIdLocal());
                        urlsImg.add(promo.getUrlImg());
                    }
                }
                loadImagesView();
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
        urlsImages=new String[]{"https://firebasestorage.googleapis.com/v0/b/placeapp-f68dd.appspot.com/o/BURGER%20KING%2F0-promo.jpg?alt=media&token=2657abdd-70e4-4384-a8ba-f87f64f87913"};
        loadImagesView();
    }
    public void loadImagesView(){
        imagesView=new ImageView[urlsImg.size()];
        RecyclerView.LayoutParams param = new RecyclerView.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT,250);
        for(int i=0;i<imagesView.length;i++){
            imagesView[i]=new ImageView(getActivity());
            imagesView[i].setLayoutParams(param);
            imagesView[i].setAdjustViewBounds(true);
            imagesView[i].setCropToPadding(true);
            Glide.with(getActivity())
                    .load(urlsImg.get(i))
                    .fitCenter()
                    .into(imagesView[i]);
            cont_images.addView(imagesView[i]);
        }
    }

    public void loadJson(){

        mDatabase.child("comentarios").child(InfoActivity.id).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                coments.removeAll(coments);
                for (DataSnapshot snapshot:
                     dataSnapshot.getChildren()) {
                    ComentOBJ coment=snapshot.getValue(ComentOBJ.class);
                    coments.add(coment);
                }
                adapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

        /*RequestQueue requestQueue = Volley.newRequestQueue(getActivity());
        JsonObjectRequest json = new JsonObjectRequest(
                Request.Method.GET,
                getResources().getString(R.string.enlace) + "comentarios/"+local.getId(),
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            int state=response.getInt("estado");
                            if(state==1){
                                JSONArray cali = response.getJSONArray("comentarios");
                                coments=new ComentOBJ[cali.length()];
                                for(int i=0;i<cali.length();i++){
                                    JSONObject c=cali.getJSONObject(i);
                                    coments[i]=new ComentOBJ(
                                            c.getString("user"),
                                            c.getString("comentario")
                                    );
                                }

                                if(coments!=null) {
                                    adapter = new AdaptadorComentarios(coments);
                                    reciclador.setAdapter(adapter);
                                }else Toast.makeText(getActivity(),"No existen Locales",Toast.LENGTH_SHORT).show();

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

    @Override
    public void onClick(View v) {
        if(band){
            bt_mas.setImageBitmap(BitmapFactory.decodeResource(getResources(),android.R.drawable.arrow_down_float));
            tx_info.setText("");
            band=false;
        }else{
            bt_mas.setImageBitmap(BitmapFactory.decodeResource(getResources(),android.R.drawable.arrow_up_float));
            tx_info.setText(local.getDescripcion());
            band=true;
        }
    }
}
