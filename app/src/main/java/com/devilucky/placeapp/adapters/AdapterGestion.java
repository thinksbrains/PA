package com.devilucky.placeapp.adapters;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.widget.PopupMenu;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.devilucky.placeapp.MainActivity;
import com.devilucky.placeapp.R;
import com.devilucky.placeapp.SplashActivity;
import com.devilucky.placeapp.object.LocalesObj;
import com.devilucky.placeapp.object.ObjUser;
import com.devilucky.placeapp.utils.JSONParser;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.net.URL;
import java.util.ArrayList;

/**
 * Created by Hakos on 10/06/2017.
 */

public class AdapterGestion extends RecyclerView.Adapter<AdapterGestion.ViewHolder> {

    ObjUser[]users;
    LocalesObj[]locales;
    Context context;

    public static class ViewHolder extends RecyclerView.ViewHolder{
        private TextView nombre,correo,type,date;
        private ImageView options;

        public ViewHolder(View itemView) {
            super(itemView);
            nombre = (TextView)itemView.findViewById(R.id.tx_nombre);
            correo = (TextView)itemView.findViewById(R.id.tx_correo);
            type = (TextView)itemView.findViewById(R.id.tx_type);
            date = (TextView)itemView.findViewById(R.id.tx_date);
            options = (ImageView)itemView.findViewById(R.id.options);
        }
    }

    public AdapterGestion(ObjUser[] users){
        this.users=users;
    }

    public AdapterGestion(ObjUser[] users,Context context){
        this.users=users;
        this.context=context;
    }
    public AdapterGestion(LocalesObj[] locales){
        this.locales=locales;
    }

    public AdapterGestion(LocalesObj[] locales,Context context){
        this.locales=locales;
        this.context=context;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.items_lista_gestiones,parent,false);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, final int position) {
        final int pos=position;
        if(users!=null) {
            ObjUser item = users[position];
            holder.nombre.setText("Nombre de Usuario: " + item.getFullname());
            holder.correo.setText("Correo de Usuario: " + item.getCorreo());
            holder.type.setText("Tipo de Usuario: " + item.getTypeString());
            holder.date.setText("Fecha de creacion: " + item.getDate());
            holder.options.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    PopupMenu popup = new PopupMenu(context,holder.options);
                    popup.getMenuInflater().inflate(R.menu.menu_popup,popup.getMenu());
                    popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                        @Override
                        public boolean onMenuItemClick(MenuItem item) {
                            if (item.getItemId()==R.id.action_eliminar) {
                                deleteData("usuarios",users[position].getId());
                            }else if (item.getItemId()==R.id.action_admin){
                                //new Functions("http://publink.esy.es/placeapp/users.php","update",users[position].getId(),2).execute();
                            }else{
                                //new Functions("http://publink.esy.es/placeapp/users.php","update",users[position].getId(),1).execute();
                            }
                            return true;
                        }
                    });
                    popup.show();
                }
            });
        }else if(locales!=null){
            LocalesObj item = locales[position];
            holder.nombre.setText("Nombre de Local: " + item.getNombre());
            holder.correo.setText("Sector: " + item.getSector());
            holder.type.setText("ID de Usuario: " + item.getIdusu());
            holder.date.setText("ID Local: " + item.getId());
            holder.options.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    PopupMenu popup = new PopupMenu(context,holder.options);
                    popup.getMenuInflater().inflate(R.menu.popup_menu_n,popup.getMenu());
                    popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                        @Override
                        public boolean onMenuItemClick(MenuItem item) {
                            if (item.getItemId()==R.id.action_eliminar) {
                                deleteData("locales",locales[position].getId());
                            }else{

                            }
                            return true;
                        }
                    });
                    popup.show();
                }
            });
        }
    }

    @Override
    public int getItemCount() {
        if(users!=null)
            return users.length;
        else
            return locales.length;
    }

    public void deleteData(String method,int id){
        RequestQueue requestQueue = Volley.newRequestQueue(context);
        JsonObjectRequest json = new JsonObjectRequest(
                Request.Method.DELETE,
                context.getResources().getString(R.string.enlace) + method+"/" + id,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            int status = response.getInt("estado");
                            if (status==1) Toast.makeText(context,response.getString("mensaje"),Toast.LENGTH_SHORT).show();
                            else Toast.makeText(context,response.getString("mensaje"),Toast.LENGTH_SHORT).show();
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(context,"Error en el servidor",Toast.LENGTH_SHORT).show();
                    }
                }
        );
        requestQueue.add(json);
    }
    public void UpdateData(String method,int id){
        RequestQueue requestQueue = Volley.newRequestQueue(context);
        JsonObjectRequest json = new JsonObjectRequest(
                Request.Method.DELETE,
                context.getResources().getString(R.string.enlace) + method+"/" + id,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            int status = response.getInt("estado");
                            if (status==1) Toast.makeText(context,response.getString("mensaje"),Toast.LENGTH_SHORT).show();
                            else Toast.makeText(context,response.getString("mensaje"),Toast.LENGTH_SHORT).show();
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(context,"Error en el servidor",Toast.LENGTH_SHORT).show();
                    }
                }
        );
        requestQueue.add(json);
    }
}
