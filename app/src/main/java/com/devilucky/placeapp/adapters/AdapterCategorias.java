package com.devilucky.placeapp.adapters;

import android.content.Context;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.devilucky.placeapp.FilterActivity;
import com.devilucky.placeapp.R;
import com.devilucky.placeapp.object.ObjCategorias;

/**
 * Created by Hakos on 30/07/2017.
 */

public class AdapterCategorias extends RecyclerView.Adapter<AdapterCategorias.ViewHolder> {
    private Context context;
    ObjCategorias[]categorias;

    public AdapterCategorias(ObjCategorias[] categorias,Context context){
        this.context=context;
        this.categorias=categorias;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.adapter_category,parent,false);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        ObjCategorias item=categorias[position];
        Glide.with(context)
                .load(item.getImagen())
                .into(holder.img_principal);
        final String sector=item.getTitulo();
        holder.img_principal.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(context, FilterActivity.class);
                intent.putExtra("sector",sector);
                context.startActivity(intent);
            }
        });
        holder.tx_titulo.setText(item.getTitulo());
    }

    @Override
    public int getItemCount() {
        return categorias.length;
    }

    public static class ViewHolder extends RecyclerView.ViewHolder{
        TextView tx_titulo;
        ImageView img_principal;
        public ViewHolder(View itemView) {
            super(itemView);
            tx_titulo=(TextView)itemView.findViewById(R.id.tx_titulo);
            img_principal=(ImageView)itemView.findViewById(R.id.image_princ);
        }
    }
}
