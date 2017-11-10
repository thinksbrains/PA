package com.devilucky.placeapp.fragment;

import android.Manifest;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresApi;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.util.Base64;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.devilucky.placeapp.R;
import com.devilucky.placeapp.RegistrarLocales;
import com.devilucky.placeapp.object.LocalesObj;
import com.devilucky.placeapp.object.ObjEventos;
import com.devilucky.placeapp.object.ObjPromo;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Hakos on 09/08/2017.
 */

public class FragmentEvenPromo extends Fragment {

    private final int REQUEST_CODE_ASK_PERMISSIONS = 123;

    String []sectores,nombres_locales;
    String uriimg,name_local;
    int pos;

    EditText et_nombre,et_desc,et_links;
    Button bt_registrar;
    ImageView imagen;
    Spinner spin_locales;
    TextView tx;

    private List<LocalesObj> locales;
    DatabaseReference mDatabase;
    StorageReference store;

    int cont;

    public FragmentEvenPromo() {
        sectores=new String[]{"Restaurantes","Boliches","Cines","Bancos","Turismo","Shopping"};
    }

    @Nullable
    @Override
    @RequiresApi(api = Build.VERSION_CODES.M)
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_even_promo,container,false);

        requiredPermisionUbc();

        locales = new ArrayList<>();
        mDatabase = FirebaseDatabase.getInstance().getReference();

        et_nombre = (EditText) v.findViewById(R.id.nombre);
        et_desc = (EditText) v.findViewById(R.id.descript);
        et_links = (EditText) v.findViewById(R.id.links);
        bt_registrar = (Button)v.findViewById(R.id.bt_registrar);
        imagen = (ImageView)v.findViewById(R.id.img_principal);
        tx = (TextView) v.findViewById(R.id.tx_title);
        spin_locales = (Spinner)v.findViewById(R.id.spinner_local);

        cargaLocales(RegistrarLocales.reg);

        bt_registrar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder dialog = new AlertDialog.Builder(getActivity());
                dialog.setMessage("Ingresara el local en la categoria "+RegistrarLocales.reg+" esta seguro??")
                        .setPositiveButton("Aceptar", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                registerData();
                            }
                        }).setNegativeButton("Cancelar", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                });dialog.create();
                dialog.show();
            }
        });
        spin_locales.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                name_local = spin_locales.getItemAtPosition(position).toString();
                pos=position;
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        imagen.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                startActivityForResult(intent, 2);
            }
        });
        return v;
    }

    public void registerData(){

        if(RegistrarLocales.reg.equalsIgnoreCase("promocion")) {
            final ProgressDialog progress = new ProgressDialog(getActivity());
            progress.setTitle("Registrando");
            progress.setMessage("Registrando...");
            progress.setCancelable(false);
            progress.show();

            store = FirebaseStorage.getInstance().getReference().child(name_local + "/" + locales.get(pos).getPromo() + "-promo.jpg");
            Uri file = Uri.fromFile(new File(uriimg));
            UploadTask uploadTask = store.putFile(file);
            uploadTask.addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {

                }
            }).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                    String url = taskSnapshot.getDownloadUrl().toString();

                    ObjPromo local = new ObjPromo(
                            url,
                            et_nombre.getText().toString(),
                            et_desc.getText().toString(),
                            et_links.getText().toString()
                    );
                    local.setIdLocal(locales.get(pos).getIdKey());
                    mDatabase.child(RegistrarLocales.reg).push().setValue(local).addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void aVoid) {
                            cleanEt();
                            Toast.makeText(getActivity(), "Registro Exitoso", Toast.LENGTH_SHORT).show();
                        }
                    });
                    progress.dismiss();
                    mDatabase = FirebaseDatabase.getInstance().getReference();
                    mDatabase.child(locales.get(pos).getSector()).child(locales.get(pos).getIdKey()).child("promo").setValue(locales.get(pos).getPromo()+1);
                }
            });
        }else{
            final ProgressDialog progress = new ProgressDialog(getActivity());
            progress.setTitle("Registrando");
            progress.setMessage("Registrando...");
            progress.setCancelable(false);
            progress.show();

            store = FirebaseStorage.getInstance().getReference().child("eventos/" + cont + "-evento.jpg");
            Uri file = Uri.fromFile(new File(uriimg));
            UploadTask uploadTask = store.putFile(file);
            uploadTask.addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {

                }
            }).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                    String url = taskSnapshot.getDownloadUrl().toString();

                    ObjEventos evento = new ObjEventos(
                            url,
                            et_nombre.getText().toString(),
                            et_desc.getText().toString(),
                            et_links.getText().toString()
                    );
                    mDatabase.child(RegistrarLocales.reg).push().setValue(evento).addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void aVoid) {
                            cleanEt();
                            Toast.makeText(getActivity(), "Registro Exitoso", Toast.LENGTH_SHORT).show();
                        }
                    });
                    progress.dismiss();
                }
            });
        }

    }

    public void cleanEt(){
        et_desc.setText("");
        et_links.setText("");
        et_nombre.setText("");
    }

    public void cargaLocales(String sector){
        if(sector.equalsIgnoreCase("promocion")) {
            for (int i = 0; i < sectores.length; i++) {
                mDatabase.child(sectores[i]).addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        for (DataSnapshot snapshot :
                                dataSnapshot.getChildren()) {
                            LocalesObj local = snapshot.getValue(LocalesObj.class);
                            local.setIdKey(snapshot.getKey());
                            locales.add(local);
                        }
                        loadSpin();
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {
                        Log.e("error update", databaseError.getMessage());
                    }
                });
            }
        }else{
            cont=0;
            spin_locales.setVisibility(View.INVISIBLE);
            tx.setVisibility(View.INVISIBLE);
                mDatabase.child("Evento").addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        for (DataSnapshot snapshot :
                                dataSnapshot.getChildren()) {
                            cont++;
                        }
                        loadSpin();
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {
                        Log.e("error update", databaseError.getMessage());
                    }
                });
        }
    }
    public void loadSpin(){
        nombres_locales=new String[locales.size()];
        for (int i=0;i<nombres_locales.length;i++) {
            Log.e("locales",locales.get(i).getNombre());
            nombres_locales[i] = locales.get(i).getNombre();
        }
        ArrayAdapter adapter=new ArrayAdapter(getActivity(),android.R.layout.simple_spinner_item,nombres_locales);
        spin_locales.setAdapter(adapter);
    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    public void requiredPermisionUbc(){
        int hasWriteContactsPermission = ContextCompat.checkSelfPermission(getActivity(), Manifest.permission.READ_EXTERNAL_STORAGE);
        if (hasWriteContactsPermission != PackageManager.PERMISSION_GRANTED) {

            // request permission

            requestPermissions(new String[] {Manifest.permission.READ_EXTERNAL_STORAGE},
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

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 2) {
            if(data!=null) {
                Uri img = data.getData();
                String[] cols = {MediaStore.Images.Media.DATA};
                Cursor cursor = getActivity().getContentResolver().query(img, cols, null, null, null);
                cursor.moveToFirst();

                int indexCol = cursor.getColumnIndex(cols[0]);
                String imgString = cursor.getString(indexCol);
                cursor.close();

                File file = new File(imgString);
                BitmapFactory.Options option=new BitmapFactory.Options();
                option.inJustDecodeBounds=true;
                BitmapFactory.decodeFile(file.getPath(),option);
                option.inPreferredConfig= Bitmap.Config.ARGB_8888;
                option.inDither=true;
                option.inSampleSize=calculateInSampleSize(option,150,150);
                option.inJustDecodeBounds=false;
                Bitmap imge=BitmapFactory.decodeFile(file.getPath(),option);
                imagen.setImageBitmap(imge);

                uriimg=file.getPath();
            }
        }
    }
    public static int calculateInSampleSize(
            BitmapFactory.Options options, int reqWidth, int reqHeight) {
        // Raw height and width of image
        final int height = options.outHeight;
        final int width = options.outWidth;
        int inSampleSize = 1;

        if (height > reqHeight || width > reqWidth) {

            final int halfHeight = height / 2;
            final int halfWidth = width / 2;

            // Calculate the largest inSampleSize value that is a power of 2 and keeps both
            // height and width larger than the requested height and width.
            while ((halfHeight / inSampleSize) >= reqHeight
                    && (halfWidth / inSampleSize) >= reqWidth) {
                inSampleSize *= 2;
            }
        }

        return inSampleSize;
    }
}
