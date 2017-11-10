package com.devilucky.placeapp.adapters;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.devilucky.placeapp.R;
import com.devilucky.placeapp.fragment.FragmentoProductos;
import com.devilucky.placeapp.object.ComentOBJ;
import com.paypal.android.sdk.payments.PayPalConfiguration;
import com.paypal.android.sdk.payments.PayPalPayment;
import com.paypal.android.sdk.payments.PayPalService;
import com.paypal.android.sdk.payments.PaymentActivity;

import java.math.BigDecimal;

/**
 * Created by Hakos on 11/06/2017.
 */

public class AdaptadorProductos extends RecyclerView.Adapter<AdaptadorProductos.ViewHolder> {
    Context context;

    public static class ViewHolder extends RecyclerView.ViewHolder{
        Button bt_comprar;
        public ViewHolder(View itemView) {
            super(itemView);
            bt_comprar = (Button)itemView.findViewById(R.id.bt_pedir);
        }
    }

    public AdaptadorProductos(Context context){
        this.context=context;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_lista_productos,parent,false);
        return new AdaptadorProductos.ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        holder.bt_comprar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new FragmentoProductos().comprarProducto();
            }
        });
        /*ComentOBJ item = coment[position];

        holder.usuario.setText(item.getUsuario());
        holder.comentario.setText(item.getComentario());*/
    }

    @Override
    public int getItemCount() {
        return 2;
    }
}
