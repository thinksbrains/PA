package com.devilucky.placeapp.adapters;

import android.graphics.Bitmap;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.devilucky.placeapp.R;
import com.devilucky.placeapp.object.ComentOBJ;
import com.devilucky.placeapp.object.RecomendadoOBJ;

import java.io.ByteArrayOutputStream;
import java.util.List;

/**
 * Created by Hakos on 11/06/2017.
 */

public class AdaptadorComentarios extends RecyclerView.Adapter<AdaptadorComentarios.ViewHolder> {
    List<ComentOBJ> coment;

    public static class ViewHolder extends RecyclerView.ViewHolder{
        private TextView usuario,comentario;
        public ViewHolder(View itemView) {
            super(itemView);
            usuario = (TextView)itemView.findViewById(R.id.tx_user);
            comentario = (TextView)itemView.findViewById(R.id.tx_comment);
        }
    }

    public AdaptadorComentarios(List<ComentOBJ> coment){
        this.coment=coment;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.items_lista_comment,parent,false);
        return new AdaptadorComentarios.ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        ComentOBJ item = coment.get(position);

        holder.usuario.setText(item.getUsuario());
        holder.comentario.setText(item.getComentario());
    }

    @Override
    public int getItemCount() {
        return coment.size();
    }
}
