package com.devilucky.placeapp.utils;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.Toast;

import com.android.volley.RequestQueue;
import com.android.volley.toolbox.Volley;
import com.devilucky.placeapp.object.ObjUser;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.net.URL;
import java.util.ArrayList;

/**
 * Created by Hakos on 07/06/2017.
 */

public class LoginMod {
    private String id,type,ident,users,fullname,password,email,telf,url,tag="";
    private Bitmap perfil;
    ObjUser[] usuario;
    SharedPreferences user;
    Context context;
    Activity activity;
    public static boolean b=false;

    public LoginMod(Context context,Activity activity){
        this.activity=activity;
        user=context.getSharedPreferences("user",Context.MODE_PRIVATE);
        this.context=context;
        url = "http://publink.esy.es/placeapp/users.php";
    }

    public void login(String ident,String pass){
        this.ident=ident;
        this.password=pass;
        tag="login";
        if(ident==null || pass==null){this.ident="";password="";}
        new Submit(activity).execute();
    }
    public void register(Bitmap perfil,String usuario,String fullname,String password,String email,String telf){
        this.users=usuario;
        this.fullname=fullname;
        this.password=password;
        this.email=email;
        this.telf=telf;
        this.perfil=perfil;
        new Submit(activity).execute();
    }
    public void gestion(){
        tag="admin";
        new Submit(activity).execute();
    }

    class Submit extends AsyncTask<URL,Void,Void> {
        ProgressDialog pd;
        Boolean dis = false;
        byte[] byteImg;
        String msj="usuario o contraseña incorrectos";
        Activity activity;

        public Submit(Activity activity){this.activity=activity;}

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            pd = new ProgressDialog(context);
            pd.show();
            pd.setTitle("Registrando");
            pd.setMessage("Un momento porfavor...");
            pd.setIndeterminate(false);
            pd.setCancelable(false);
        }

        @Override
        protected Void doInBackground(URL... params) {
            if(perfil!=null) {
                ByteArrayOutputStream oe4 = new ByteArrayOutputStream();
                perfil.compress(Bitmap.CompressFormat.PNG, 40, oe4);
                byteImg = oe4.toByteArray();
            }
            if(tag.equals("login")) {
                ArrayList<NameValuePair> parameters = new ArrayList<NameValuePair>();
                parameters.add(new BasicNameValuePair("flag", tag));
                parameters.add(new BasicNameValuePair("user",ident));
                parameters.add(new BasicNameValuePair("pass",password));
                JSONObject json = JSONParser.makeHttpRequest(url, "POST", parameters);
                try {
                    int success = json.getInt("success");
                    if (success == 1) {
                        JSONArray cali = json.getJSONArray("usuario");
                        JSONObject c = cali.getJSONObject(0);
                        SharedPreferences.Editor edit = user.edit();
                        edit.putInt("status",1);
                        edit.putInt("id",c.getInt("id"));
                        edit.putInt("type",c.getInt("type"));
                        edit.putString("fullname",c.getString("fullname"));
                        edit.putString("email",c.getString("email"));
                        edit.putString("telf",c.getString("telf"));
                        edit.putString("perfil",c.getString("perfil"));
                        edit.commit();
                        msj="Bienvenido "+c.getString("fullname");
                        b=true;
                    }
                } catch (Exception e) {
                    Log.e("Exception", e.getMessage());
                }
            }else if(tag.equals("admin")) {
                ArrayList<NameValuePair> parameters = new ArrayList<NameValuePair>();
                parameters.add(new BasicNameValuePair("flag", tag));
                JSONObject json = JSONParser.makeHttpRequest(url, "POST", parameters);
                try {
                    int success = json.getInt("success");
                    if (success == 1) {
                        JSONArray cali = json.getJSONArray("usuario");
                        usuario = new ObjUser[cali.length()];
                        for(int i=0; i<cali.length();i++){
                            JSONObject c = cali.getJSONObject(i);
                            usuario[i]=new ObjUser(
                                    c.getInt("id"),
                                    c.getInt("type"),
                                    c.getString("user"),
                                    c.getString("fullname"),
                                    c.getString("email"),
                                    c.getString("telf"),
                                    c.getString("perfil"),
                                    c.getString("date")
                            );
                            b = true;
                        }
                    }
                } catch (Exception e) {
                    Log.e("Exception", e.getMessage());
                }
            }else{
                ArrayList<NameValuePair> parameters = new ArrayList<NameValuePair>();
                parameters.add(new BasicNameValuePair("flag", tag));
                parameters.add(new BasicNameValuePair("user",users));
                parameters.add(new BasicNameValuePair("name",fullname));
                parameters.add(new BasicNameValuePair("pass",password));
                parameters.add(new BasicNameValuePair("email",email));
                parameters.add(new BasicNameValuePair("telf",telf));
                parameters.add(new BasicNameValuePair("perfil",""/*Base64.encodeToString(byteImg,Base64.DEFAULT)*/));
                JSONObject json = JSONParser.makeHttpRequest(url, "POST", parameters);
                try {
                    int success = json.getInt("success");
                    if (success == 1) {
                        msj="Subido con exito";
                        dis=true;
                        b=true;
                    }
                } catch (Exception e) {
                    Log.e("Exception", e.getMessage());
                }
            }
            return null;
        }

        @Override
        protected void onPostExecute(Void publi) {
            if (b ) {
                this.activity.finish();
                Toast.makeText(context,msj,Toast.LENGTH_SHORT).show();
            } else{
                Toast.makeText(context,msj,Toast.LENGTH_SHORT).show();
            }
            pd.dismiss();
        }
    }
}
