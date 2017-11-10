package com.devilucky.placeapp;

import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.facebook.AccessToken;
import com.facebook.login.LoginManager;
import com.google.firebase.auth.FirebaseAuth;

public class ConfigActivity extends AppCompatActivity implements View.OnClickListener{

    ImageView perfil;
    TextView tx_usuario,bt_micuenta,bt_mismsj,bt_config,bt_logout,bt_about;
    SharedPreferences user;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_config);

        user=getSharedPreferences("admin",CONTEXT_RESTRICTED);
        tx_usuario=(TextView)findViewById(R.id.tv_usuario);
        if(FirebaseAuth.getInstance().getCurrentUser().getDisplayName()!=null)
        tx_usuario.setText(FirebaseAuth.getInstance().getCurrentUser().getDisplayName());
        else tx_usuario.setText(FirebaseAuth.getInstance().getCurrentUser().getEmail());

        bt_logout=(TextView)findViewById(R.id.bt_logout);
        bt_logout.setOnClickListener(this);

        bt_micuenta = (TextView)findViewById(R.id.bt_micuenta);
        bt_micuenta.setOnClickListener(this);

        bt_about = (TextView)findViewById(R.id.bt_about);
        bt_about.setOnClickListener(this);

        bt_mismsj = (TextView)findViewById(R.id.bt_mismsj);
        bt_mismsj.setOnClickListener(this);

        bt_config = (TextView)findViewById(R.id.bt_config);
        bt_config.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        int id=v.getId();
        switch (id){
            case R.id.bt_logout:
                SharedPreferences.Editor edit=user.edit();
                edit.putInt("status",0);
                edit.putInt("admin",0);
                edit.commit();
                FirebaseAuth.getInstance().signOut();
                LoginManager.getInstance().logOut();
                startActivity(new Intent(ConfigActivity.this,SplashActivity.class));
                finish();
                break;
            case R.id.bt_micuenta:
                startActivity(new Intent(ConfigActivity.this,MiCuentaActivity.class));
                break;
            case R.id.bt_about:
                startActivity(new Intent(ConfigActivity.this,AboutActivity.class));
                break;
            case R.id.bt_mismsj:
                Toast.makeText(getApplicationContext(),"boton no habilitado",Toast.LENGTH_SHORT).show();
                break;
            case R.id.bt_config:
                Toast.makeText(getApplicationContext(),"boton no habilitado",Toast.LENGTH_SHORT).show();
                break;
        }
    }
}
