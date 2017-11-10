package com.devilucky.placeapp.fragment;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import com.devilucky.placeapp.MainActivity;
import com.devilucky.placeapp.R;
import com.devilucky.placeapp.utils.LoginMod;

import java.lang.reflect.Field;

/**
 * Created by Hakos on 09/06/2017.
 */

public class FragmentoLogin extends Fragment {
    LoginMod logMod;
    String ident,pass;
    EditText tx_usuario,tx_pass;
    Button bt_login;


    public FragmentoLogin(){/*logMod=new LoginMod(context,context);*/}

    public View onCreateView(LayoutInflater inflater, ViewGroup parent, Bundle saved){
        View v=inflater.inflate(R.layout.fragment_login,parent,false);
        tx_usuario = (EditText)v.findViewById(R.id.tx_usuario);
        tx_pass = (EditText)v.findViewById(R.id.tx_pass);
        bt_login = (Button)v.findViewById(R.id.bt_login);
        bt_login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                comprobarUsuarios();
            }
        });
        return v;
    }
    private void comprobarUsuarios(){
        ident = tx_usuario.getText().toString();
        pass = tx_pass.getText().toString();
        logMod.login(ident,pass);
    }
    @Override
    public void onDetach() {
        super.onDetach();

        try {
            Field childFragmentManager = Fragment.class.getDeclaredField("mChildFragmentManager");
            childFragmentManager.setAccessible(true);
            childFragmentManager.set(this, null);

        } catch (NoSuchFieldException e) {
            throw new RuntimeException(e);
        } catch (IllegalAccessException e) {
            throw new RuntimeException(e);
        }
    }
}
