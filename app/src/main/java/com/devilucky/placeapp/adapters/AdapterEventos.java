package com.devilucky.placeapp.adapters;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.media.Image;
import android.net.Uri;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.devilucky.placeapp.R;
import com.devilucky.placeapp.object.ObjEventos;
import com.devilucky.placeapp.object.ObjPromo;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.List;

/**
 * Created by Hakos on 09/08/2017.
 */

public class AdapterEventos extends RecyclerView.Adapter<AdapterEventos.ViewHolder> {
    private List<ObjEventos> eventos;
    private Context context;

    public AdapterEventos(Context contexts,List<ObjEventos> eventos) {
        this.context=contexts;
        this.eventos=eventos;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_promociones,parent,false);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, final int position) {
        final ObjEventos item = eventos.get(position);
        Glide.with(holder.itemView.getContext())
                .load(item.getUtlImg())
                .asBitmap()
                .fitCenter()
                .into(holder.imagen);

        holder.imagen.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(Intent.ACTION_VIEW);
                i.setData(Uri.parse(item.getUrlExt()));
                context.startActivity(i);
            }
        });

        SharedPreferences admin = context.getSharedPreferences("admin",Context.CONTEXT_RESTRICTED);
        if(admin.getInt("admin",0)==3) {
            holder.bt_delete.setVisibility(View.VISIBLE);

            holder.bt_delete.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    AlertDialog.Builder dialog = new AlertDialog.Builder(context);
                    dialog.setMessage("Quieres eliminar el Evento "+eventos.get(position).getNombre()+" con ID "+eventos.get(position).getIdKey()+"?")
                            .setPositiveButton("Aceptar", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {

                                    DatabaseReference data = FirebaseDatabase.getInstance().getReference("Evento");
                                    data.child(eventos.get(position).getIdKey()).removeValue().addOnSuccessListener(new OnSuccessListener<Void>() {
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

    @Override
    public int getItemCount() {
        return eventos.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder{
        ImageView imagen,bt_delete;
        public ViewHolder(View itemView) {
            super(itemView);
            imagen = (ImageView)itemView.findViewById(R.id.img);
            bt_delete = (ImageView)itemView.findViewById(R.id.bt_delete);
        }
    }
}
