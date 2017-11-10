package com.devilucky.placeapp.adapters;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.os.Parcelable;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.devilucky.placeapp.InfoActivity;
import com.devilucky.placeapp.MainActivity;
import com.devilucky.placeapp.R;
import com.devilucky.placeapp.object.LocalesObj;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.io.ByteArrayOutputStream;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Hakos on 08/06/2017.
 */

public class AdaptadorComidas extends RecyclerView.Adapter<AdaptadorComidas.ViewHolder> {
    public List<LocalesObj> locales;
    Context context;
    String tag="";
    int cont;
    int[] posisionesR;

    public static class ViewHolder extends RecyclerView.ViewHolder{
        TextView nombre,tx_desc,tx_dis;
        ImageView imagen,bt_fav,bt_delete;
        RatingBar ration;
        RelativeLayout click;
        public ViewHolder(View itemView) {
            super(itemView);
            nombre = (TextView)itemView.findViewById(R.id.tx_titulo);
            tx_desc = (TextView)itemView.findViewById(R.id.tx_desc);
            tx_dis = (TextView)itemView.findViewById(R.id.tx_dis);
            imagen = (ImageView)itemView.findViewById(R.id.miniatura_local);
            bt_fav = (ImageView)itemView.findViewById(R.id.bt_fav);
            ration = (RatingBar)itemView.findViewById(R.id.rating);
            click = (RelativeLayout)itemView.findViewById(R.id.click);
            bt_delete = (ImageView)itemView.findViewById(R.id.bt_delete);
        }
    }

    public AdaptadorComidas(List<LocalesObj> locales, Context context, String tag){
        this.locales=locales;
        this.context=context;
        this.tag=tag;
        //this.cont=cont;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_lista_comida,parent,false);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, final int position) {
        LocalesObj item = locales.get(position);
        Log.e("adapter",tag+":"+item.getSector());
        if (item.getSector().equals(tag)) {
            Glide.with(holder.itemView.getContext())
                    .load(item.getLogo())
                    .asBitmap()
                    .centerCrop()
                    .into(holder.imagen);
            holder.nombre.setText(item.getNombre());
            /*holder.nombre.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(context, InfoActivity.class);
                    intent.putExtra("id", locales[position].getId());
                    //intent.putExtra("position", posisionesR[pos]);
                    context.startActivity(intent);
                }
            });*/
            //holder.views.setText("0");
            holder.tx_desc.setText(item.getInfo());
            holder.ration.setRating(item.getRating());

            /*holder.imagen.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(context, InfoActivity.class);
                    intent.putExtra("id", locales[position].getId());
                    //intent.putExtra("position", posisionesR[pos]);
                    context.startActivity(intent);
                }
            });*/
            holder.click.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(context, InfoActivity.class);
                    intent.putExtra("id", locales.get(position).getIdKey());
                    intent.putExtra("sector", locales.get(position).getSector());
                    //intent.putExtra("position", posisionesR[pos]);
                    context.startActivity(intent);
                }
            });
            SharedPreferences admin = context.getSharedPreferences("admin",Context.CONTEXT_RESTRICTED);
            if(admin.getInt("admin",0)==3) {
                holder.bt_delete.setVisibility(View.VISIBLE);
                holder.bt_delete.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        AlertDialog.Builder dialog = new AlertDialog.Builder(context);
                        dialog.setMessage("Quieres eliminar local "+locales.get(position).getNombre()+" con ID "+locales.get(position).getIdKey()+"?")
                                .setPositiveButton("Aceptar", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {

                                        DatabaseReference data = FirebaseDatabase.getInstance().getReference(locales.get(position).getSector());
                                        data.child(locales.get(position).getIdKey()).removeValue().addOnSuccessListener(new OnSuccessListener<Void>() {
                                            @Override
                                            public void onSuccess(Void aVoid) {
                                                Toast.makeText(context,"Local eliminado con Exito",Toast.LENGTH_SHORT).show();
                                            }
                                        });

                                    }
                                }).setNegativeButton("Cancelar", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.dismiss();
                            }
                        });dialog.create();
                        dialog.show();
                    }
                });
            }
        }
    }

    @Override
    public int getItemCount() {
        return locales.size();
    }

    public void filter(ArrayList<LocalesObj> newlist){
        locales = new ArrayList<>();
        locales.addAll(newlist);
        notifyDataSetChanged();
    }

}
