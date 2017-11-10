package com.devilucky.placeapp.adapters;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.bumptech.glide.Glide;
import com.devilucky.placeapp.InfoActivity;
import com.devilucky.placeapp.R;
import com.devilucky.placeapp.object.LocalesObj;
import com.devilucky.placeapp.object.RecomendadoOBJ;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;

/**
 * Created by Hakos on 08/06/2017.
 */

public class AdaptadorInicio extends RecyclerView.Adapter<AdaptadorInicio.ViewHolder> {
    LocalesObj[] locales;
    Context context;
    public static class ViewHolder extends RecyclerView.ViewHolder{
        private ImageView mini;
        private TextView title;
        public ViewHolder(View itemView) {
            super(itemView);
            mini = (ImageView)itemView.findViewById(R.id.mini_img);
            title = (TextView)itemView.findViewById(R.id.tx_nombre);
        }
    }

    public AdaptadorInicio(LocalesObj[] locales, final Context context){
        this.locales=locales;
        this.context=context;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_lista_inicio,parent,false);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, final int position) {
        LocalesObj item = locales[position];
        Glide.with(holder.itemView.getContext())
                .load(context.getResources().getString(R.string.enlaceimages)+item.getPortada())
                .asBitmap()
                .centerCrop()
                .into(holder.mini);
        holder.mini.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, InfoActivity.class);
                intent.putExtra("id", locales[position].getId());
                intent.putExtra("position", position);
                context.startActivity(intent);
            }
        });
        holder.title.setText(item.getNombre());
    }

    @Override
    public int getItemCount() {
        if(locales.length<5)return locales.length;
        else return 5;
    }
}
