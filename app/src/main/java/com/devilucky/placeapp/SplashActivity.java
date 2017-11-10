package com.devilucky.placeapp;

import android.Manifest;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.Signature;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Message;
import android.support.annotation.NonNull;
import android.support.annotation.RequiresApi;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.CardView;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.devilucky.placeapp.object.LocalesObj;
import com.devilucky.placeapp.utils.JSONParser;
import com.facebook.AccessToken;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.FacebookSdk;
import com.facebook.Profile;
import com.facebook.ProfileTracker;
import com.facebook.login.LoginManager;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FacebookAuthProvider;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.net.URL;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;

import android.os.Handler;

public class SplashActivity extends AppCompatActivity {
    public static LocalesObj[] locales;
    private final int REQUEST_CODE_ASK_PERMISSIONS = 123;
    Button bt_login;
    CardView bt_gmail,bt_register;
    TextView tx_usuario,tx_pass;
    private static SharedPreferences user;
    private LoginButton loginButton;
    private CallbackManager callbackManager;
    private ProfileTracker profileTracker;

    private String usuario,correo;

    private FirebaseAuth mAuth;
    private FirebaseAuth.AuthStateListener mListener;

    private DatabaseReference mDatabase;

    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        user=getSharedPreferences("admin",CONTEXT_RESTRICTED);

        //firebase
        mAuth=FirebaseAuth.getInstance();
        mListener=new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                FirebaseUser user = firebaseAuth.getCurrentUser();
                if(user!=null){
                    usuario=user.getDisplayName();
                    correo=user.getEmail();
                    verificarAdmin(user.getUid());
                }
            }
        };
        mDatabase = FirebaseDatabase.getInstance().getReference();

        //facebook
        FacebookSdk.sdkInitialize(getApplicationContext());
        loginButton = (LoginButton) findViewById(R.id.login_button);
        loginButton.setReadPermissions("email", "public_profile");
        callbackManager=CallbackManager.Factory.create();
        loginButton.registerCallback(callbackManager, new FacebookCallback<LoginResult>() {
            @Override
            public void onSuccess(LoginResult loginResult) {
                facebookAccessToken(loginResult.getAccessToken());
                /*if(Profile.getCurrentProfile()==null) {
                    final String token=loginResult.getAccessToken().getUserId();
                    profileTracker = new ProfileTracker() {
                        @Override
                        protected void onCurrentProfileChanged(Profile oldProfile, Profile currentProfile) {
                            if(currentProfile!=null) {
                                obtenerUsuario(currentProfile.getName(), token);
                                startActivity(new Intent(SplashActivity.this, Categorias.class));
                                finish();
                            }
                        }
                    };
                }else{
                    Profile profile = Profile.getCurrentProfile();
                    obtenerUsuario(profile.getName(), loginResult.getAccessToken().getUserId());
                    startActivity(new Intent(SplashActivity.this, Categorias.class));
                    finish();
                }*/
            }

            @Override
            public void onCancel() {
                // App code
            }

            @Override
            public void onError(FacebookException exception) {
                // App code
            }
        });

        requiredPermisionUbc();


        if(user.getInt("status",0)==1 || AccessToken.getCurrentAccessToken()!=null){
            startActivity(new Intent(SplashActivity.this,Categorias.class));
            finish();
        }

        tx_usuario = (EditText)findViewById(R.id.et_usuario);
        tx_pass = (EditText)findViewById(R.id.et_pass);
        bt_register=(CardView)findViewById(R.id.bt_register);
        bt_login=(Button)findViewById(R.id.bt_login);
        bt_login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                loginUser();
            }
        });
        bt_register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(SplashActivity.this,ActivityRegister.class));
            }
        });
    }

    private void verificarAdmin(final String userID){
        mDatabase.child("admin").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if(dataSnapshot.child(userID).exists()) {
                    SharedPreferences.Editor edit = user.edit();
                    edit.putInt("status", 1);
                    edit.putInt("admin", 3);
                    edit.commit();
                    if(usuario==null)Toast.makeText(getApplicationContext(),"Bienvenido "+correo,Toast.LENGTH_SHORT).show();
                    else Toast.makeText(getApplicationContext(),"Bienvenido "+usuario,Toast.LENGTH_SHORT).show();
                    startActivity(new Intent(SplashActivity.this,Categorias.class));
                }else{
                    SharedPreferences.Editor edit = user.edit();
                    edit.putInt("status", 1);
                    edit.putInt("admin", 0);
                    edit.commit();
                    if(usuario==null)Toast.makeText(getApplicationContext(),"Bienvenido "+correo,Toast.LENGTH_SHORT).show();
                    else Toast.makeText(getApplicationContext(),"Bienvenido "+usuario,Toast.LENGTH_SHORT).show();
                    startActivity(new Intent(SplashActivity.this,Categorias.class));
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    private void facebookAccessToken(AccessToken token){
        AuthCredential credential = FacebookAuthProvider.getCredential(token.getToken());
        mAuth.signInWithCredential(credential).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if(task.isSuccessful()){
                    FirebaseUser user = mAuth.getCurrentUser();
                }else{
                    Toast.makeText(getApplicationContext(), "Authentication failed.",
                            Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    public void updateUI(){

    }

    public void register(final String fullname, final String userid, String correo){
        Log.e("entro registro","fullname:"+fullname+"/userid:"+userid);
        HashMap<String,String> params=new HashMap<>();
        params.put("type","0");
        params.put("user",fullname);
        params.put("fullname",fullname);
        params.put("pass",userid);
        params.put("email",correo);
        params.put("telf","");
        params.put("perfil","0");
        Log.e("entro registro","lleno datos");

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
                                Log.e("entro registro","entro");
                                obtenerUsuario(fullname,userid);
                                Toast.makeText(getApplicationContext(),response.getString("mensaje"),Toast.LENGTH_SHORT).show();
                                finish();
                            }else Log.e("register error",response.getString("mensaje"));
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

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        callbackManager.onActivityResult(requestCode, resultCode, data);
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

    private void obtenerUsuario(final String fullname, final String pass){
        HashMap<String,String> params=new HashMap<>();
        params.put("user",fullname);
        params.put("pass",pass);

        RequestQueue requestQueue= Volley.newRequestQueue(this);
        JsonObjectRequest json=new JsonObjectRequest(
                Request.Method.POST,
                getResources().getString(R.string.enlace) + "usuarios/login",
                new JSONObject(params),
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            int status=response.getInt("estado");
                            if(status==1){
                                JSONObject c=response.getJSONObject("usuario");
                                SharedPreferences.Editor edit = user.edit();
                                edit.putInt("status",1);
                                edit.putInt("id",c.getInt("id"));
                                edit.putInt("type",c.getInt("type"));
                                edit.putString("fullname",c.getString("fullname"));
                                edit.putString("email",c.getString("email"));
                                edit.putString("telf",c.getString("telf"));
                                edit.putString("perfil",c.getString("perfil"));
                                edit.commit();
                                Toast.makeText(getApplicationContext(),"Usuario Correcto",Toast.LENGTH_SHORT).show();
                                Toast.makeText(getApplicationContext(),"Bienvenido "+c.getString("fullname"),Toast.LENGTH_SHORT).show();
                                finish();
                            }else{
                                register(fullname,pass,"");
                                Toast.makeText(getApplicationContext(),response.getString("mensaje"),Toast.LENGTH_SHORT).show();
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
        );requestQueue.add(json);
    }
    private void loginUser(){
        String correo = tx_usuario.getText().toString();
        String pass = tx_pass.getText().toString();

        mAuth.signInWithEmailAndPassword(correo,pass);
    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    public void requiredPermisionUbc(){
        int hasWriteContactsPermission = ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION);
        if (hasWriteContactsPermission != PackageManager.PERMISSION_GRANTED) {

            // request permission

            requestPermissions(new String[] {Manifest.permission.ACCESS_COARSE_LOCATION},
                    REQUEST_CODE_ASK_PERMISSIONS);

        }else if (hasWriteContactsPermission == PackageManager.PERMISSION_GRANTED){

        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        if(REQUEST_CODE_ASK_PERMISSIONS == requestCode) {
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                // OK Do something with..
            } else {
                // The user does not grant permissions
            }
        }else{
            super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        }
    }
}
