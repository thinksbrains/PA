package com.devilucky.placeapp;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.devilucky.placeapp.object.ObjUser;
import com.devilucky.placeapp.utils.LoginMod;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;

public class ActivityRegister extends AppCompatActivity {
    EditText tx_usuario,tx_fullname,tx_correo,tx_telf,tx_pass;
    Button bt_registrar;
    LoginMod loginMod;

    FirebaseAuth mAuth;
    FirebaseAuth.AuthStateListener mListener;
    DatabaseReference mDatabase;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        loginMod=new LoginMod(this,this);

        tx_usuario=(EditText)findViewById(R.id.tx_usuario);
        tx_fullname=(EditText)findViewById(R.id.tx_fullname);
        tx_pass=(EditText)findViewById(R.id.tx_pass);
        tx_correo=(EditText)findViewById(R.id.tx_correo);
        tx_telf=(EditText)findViewById(R.id.tx_telfo);
        bt_registrar=(Button)findViewById(R.id.bt_registrar);

        mAuth=FirebaseAuth.getInstance();
        mDatabase = FirebaseDatabase.getInstance().getReference();



        bt_registrar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                /*loginMod.register(null,tx_usuario.getText().toString(),tx_fullname.getText().toString(),
                        tx_pass.getText().toString(),tx_correo.getText().toString(),tx_telf.getText().toString());*/
                registrar();
            }
        });

        mListener = new FirebaseAuth.AuthStateListener(){

            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                FirebaseUser user=firebaseAuth.getCurrentUser();
                if(user!=null){
                    startActivity(new Intent(ActivityRegister.this, Categorias.class));
                    finish();
                }
            }
        } ;
    }

    @Override
    protected void onStart() {
        super.onStart();
        mAuth.addAuthStateListener(mListener);
    }

    @Override
    protected void onStop() {
        super.onStop();
        mAuth.removeAuthStateListener(mListener);
    }

    private void registrar(){
        FirebaseDatabase database=FirebaseDatabase.getInstance();
        final DatabaseReference dtRef=database.getReference("placeapp");
        final String user=tx_usuario.getText().toString();
        final String fullname=tx_fullname.getText().toString();
        final String correo=tx_correo.getText().toString();
        final String pass=tx_pass.getText().toString();
        final String telf=tx_telf.getText().toString();
        final String perfil="";
        final String fecha="";

        mAuth.createUserWithEmailAndPassword(correo,pass).addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if(task.isSuccessful()){
                    //ObjUser usuario=new ObjUser(1,0,user,fullname,correo,telf,"perfil","fecha");
                    //dtRef.child("user").push().setValue(usuario);
                    iniciarSesion(correo,pass);
                }
            }
        });
    }

    private void iniciarSesion(String correo, String pass){
        FirebaseAuth.getInstance().signInWithEmailAndPassword(correo,pass);
    }
    public void register(){
        HashMap<String,String> params=new HashMap<>();
        params.put("type","0");
        params.put("user",tx_usuario.getText().toString());
        params.put("fullname",tx_fullname.getText().toString());
        params.put("pass",tx_pass.getText().toString());
        params.put("email",tx_correo.getText().toString());
        params.put("telf",tx_telf.getText().toString());
        params.put("perfil","0");

        RequestQueue requestQueue= Volley.newRequestQueue(this);
        JsonObjectRequest jsonObjectRequest=new JsonObjectRequest(
                Request.Method.POST,
                getResources().getString(R.string.enlace) + "usuarios/registro",
                new JSONObject(params),
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        int state = 0;
                        try {
                            state = response.getInt("estado");
                            if(state==1){
                                Toast.makeText(getApplicationContext(),response.getString("mensaje"),Toast.LENGTH_SHORT).show();
                                finish();
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.e("error", "Error Respuesta en JSON: " + error.getMessage());
                    }
                }
        );
        requestQueue.add(jsonObjectRequest);
    }
}
