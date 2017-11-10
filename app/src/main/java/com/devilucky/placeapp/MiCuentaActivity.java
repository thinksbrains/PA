package com.devilucky.placeapp;

import android.graphics.BitmapFactory;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;

import de.hdodenhof.circleimageview.CircleImageView;

public class MiCuentaActivity extends AppCompatActivity implements View.OnClickListener{

    private Button bt_paypal,bt_pagonet;
    private CircleImageView perfil;
    private TextView tv_usuario;
    private RecyclerView reciclador;
    private RecyclerView.LayoutManager layoutManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mi_cuenta);

        ActionBar actionBar = getSupportActionBar();
        actionBar.setTitle("Mi Cuenta");
        actionBar.setDisplayHomeAsUpEnabled(true);

        perfil = (CircleImageView)findViewById(R.id.image_perfil);
        tv_usuario = (TextView)findViewById(R.id.tv_usuario);
        bt_pagonet = (Button)findViewById(R.id.bt_pagonet);
        bt_paypal = (Button)findViewById(R.id.bt_paypal);

        if(FirebaseAuth.getInstance().getCurrentUser().getDisplayName()!=null)
            tv_usuario.setText(FirebaseAuth.getInstance().getCurrentUser().getDisplayName());
        else tv_usuario.setText(FirebaseAuth.getInstance().getCurrentUser().getEmail());
        bt_pagonet.setOnClickListener(this);
        bt_paypal.setOnClickListener(this);

    }

    @Override
    public void onClick(View v) {
        int id=v.getId();
        switch(id){
            case R.id.bt_pagonet:
                Toast.makeText(getApplicationContext(),"Pagos Sin Configurar",Toast.LENGTH_SHORT).show();
                break;
            case R.id.bt_paypal:
                Toast.makeText(getApplicationContext(),"Pagos Sin Configurar",Toast.LENGTH_SHORT).show();
                break;
        }
    }
}
