package com.devilucky.placeapp;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
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
import com.devilucky.placeapp.fragment.FragmentoCuenta;
import com.devilucky.placeapp.fragment.InfoFragmento;
import com.devilucky.placeapp.utils.LoginMod;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;

public class CuentaActivity extends AppCompatActivity implements View.OnClickListener{

    EditText tx_iden,tx_pass;
    LoginMod loginMod;
    Button bt_login;
    TextView tx_registro;
    private static SharedPreferences user;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cuenta);
        user=getSharedPreferences("user",CONTEXT_RESTRICTED);
        loginMod=new LoginMod(this,this);

        tx_iden = (EditText)findViewById(R.id.tx_iden);
        tx_pass = (EditText)findViewById(R.id.tx_pass);
        bt_login = (Button)findViewById(R.id.bt_ingresar);
        tx_registro=(TextView)findViewById(R.id.tx_registro);
        bt_login.setOnClickListener(this);
        tx_registro.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(CuentaActivity.this,ActivityRegister.class));
            }
        });
    }

    protected void onResume(){
        super.onResume();
    }

    @Override
    public void onClick(View v) {
        //loginMod.login(tx_iden.getText().toString(),tx_pass.getText().toString());

        HashMap<String,String> params=new HashMap<>();
        params.put("user",tx_iden.getText().toString());
        params.put("pass",tx_pass.getText().toString());

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
}
