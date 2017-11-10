package com.devilucky.placeapp.fragment;

import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.media.Image;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.devilucky.placeapp.R;

/**
 * Created by Hakos on 08/06/2017.
 */

public class FragmentoPerfil extends Fragment {

    TextView tx_nombre, tx_correo, tx_telf;
    ImageView arrow;
    private SharedPreferences user;

    public FragmentoPerfil(){this.user=new FragmentoCuenta().user;}

    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle saved){
        View v = inflater.inflate(R.layout.fragment_perfil, container, false);

        tx_nombre=(TextView) v.findViewById(R.id.tx_nombre);
        tx_correo=(TextView) v.findViewById(R.id.tx_correo);
        tx_telf=(TextView) v.findViewById(R.id.tx_telf);
        arrow=(ImageView)v.findViewById(R.id.icono_indicador_derecho);

        tx_nombre.setText(user.getString("fullname",""));
        tx_correo.setText(user.getString("email",""));
        tx_telf.setText(user.getString("telf",""));

        final EditText edit = new EditText(getActivity());
        arrow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final AlertDialog.Builder build = new AlertDialog.Builder(getActivity());
                build.setMessage("Ingrese nueva contraseña");
                build.setView(edit);
                build.setPositiveButton("Cambiar", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Toast.makeText(getActivity(),"Contraseña cambiada",Toast.LENGTH_SHORT).show();
                    }
                });
                build.setNegativeButton("Cancelar", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Toast.makeText(getActivity(),"Cancelado",Toast.LENGTH_SHORT).show();
                    }
                });
                build.show();
            }
        });
        return v;
    }
}
