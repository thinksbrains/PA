package com.devilucky.placeapp.fragment;

import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.devilucky.placeapp.InfoActivity;
import com.devilucky.placeapp.MainActivity;
import com.devilucky.placeapp.R;
import com.devilucky.placeapp.object.ComentOBJ;
import com.devilucky.placeapp.object.LocalesObj;
import com.devilucky.placeapp.utils.JSONParser;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import org.apache.http.NameValuePair;
import org.apache.http.entity.StringEntity;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by Hakos on 11/06/2017.
 */

public class FragmentoComentar extends Fragment {

    EditText tx_comentario;
    Button enviar;
    String idusu,idneg,comment="";

    DatabaseReference mDatabase;

    public FragmentoComentar(){}

    public View onCreateView(LayoutInflater inflater, ViewGroup parent, Bundle saved){
        View v = inflater.inflate(R.layout.fragmento_comentar,parent,false);

        mDatabase = FirebaseDatabase.getInstance().getReference();

        tx_comentario=(EditText)v.findViewById(R.id.tx_comentario);
        enviar=(Button)v.findViewById(R.id.bt_comentar);
        enviar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (TextUtils.isEmpty(tx_comentario.getText().toString()))
                    Toast.makeText(getActivity(), "Escriba un comentario", Toast.LENGTH_SHORT).show();
                else comentar();
            }
        });
        return v;
    }

    private void loadData(){
        idusu= String.valueOf(new MainActivity().user.getInt("id",0));
        idneg= String.valueOf(new InfoFragmento().local.getId());
        comment=""+ tx_comentario.getText().toString();
    }

    public void comentar(){
        ComentOBJ comentario = new ComentOBJ(FirebaseAuth.getInstance().getCurrentUser().getDisplayName(),tx_comentario.getText().toString());
        mDatabase.child("comentarios").child(InfoActivity.id).push().setValue(comentario).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
                Toast.makeText(getActivity(), "Gracias por Comentar", Toast.LENGTH_SHORT).show();
                tx_comentario.setText("");
            }
        });
    }
}
