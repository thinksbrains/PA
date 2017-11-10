package com.devilucky.placeapp.fragment;

import android.Manifest;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
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
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.devilucky.placeapp.MapsRegister;
import com.devilucky.placeapp.R;
import com.devilucky.placeapp.object.LocalesObj;
import com.devilucky.placeapp.utils.JSONParser;
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

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Hakos on 10/06/2017.
 */

public class FregmentoRegistroN extends Fragment {

    Spinner spin_sector,spin_local;
    String[] sectores;
    static EditText et_nombre,et_descript,et_lat,et_lon,et_descBasic,et_compartir;
    Button bt_registrar;
    private static ImageView iv_logo,iv_portada;
    String logo,portada,nombre,desc,idusu,lat,lon,sector;
    ImageButton bt_ubic;
    String urilogo,uriPortada;
    File flogo,fportada;
    TextView tx_local;
    String [] nombres_locales;

    private List<LocalesObj> locales;
    DatabaseReference mDatabase;


    private final int REQUEST_CODE_ASK_PERMISSIONS = 123;

    private DatabaseReference mdatabase;
    private StorageReference storelogo,storeportada;

    public FregmentoRegistroN(){sectores=new String[]{"Restaurantes","Boliches","Cines","Bancos","Turismo","Shopping"};}

    public View onCreateView(LayoutInflater inflater, ViewGroup parent, Bundle saved){
        View v = inflater.inflate(R.layout.fragmento_registro_negocios,parent,false);
        spin_sector = (Spinner) v.findViewById(R.id.spin_sectores);

        mdatabase = FirebaseDatabase.getInstance().getReference();

        ArrayAdapter adapter = new ArrayAdapter(getActivity(),android.R.layout.simple_spinner_item,sectores);
        spin_sector.setAdapter(adapter);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            requiredPermisionUbc();
        }

        et_nombre = (EditText)v.findViewById(R.id.tx_nombre);
        et_descBasic = (EditText)v.findViewById(R.id.tx_descrip_basic);
        et_descript = (EditText)v.findViewById(R.id.tx_descripcion);
        et_compartir= (EditText)v.findViewById(R.id.et_compartir);
        et_lat = (EditText)v.findViewById(R.id.tx_latitud);
        et_lon = (EditText)v.findViewById(R.id.tx_longitud);
        iv_logo = (ImageView)v.findViewById(R.id.iv_logo);
        iv_portada = (ImageView)v.findViewById(R.id.iv_portada);
        bt_registrar = (Button)v.findViewById(R.id.bt_registrar);
        bt_ubic=(ImageButton)v.findViewById(R.id.img_ubi);
        bt_ubic.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getActivity(), MapsRegister.class));
            }
        });
        bt_registrar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                loadData();
                AlertDialog.Builder dialog = new AlertDialog.Builder(getActivity());
                dialog.setMessage("Ingresara el local en la categoria "+sector+" esta seguro??")
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
                //new Submit(getActivity()).execute();
            }
        });
        iv_logo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                startActivityForResult(intent, 2);
            }
        });
        iv_portada.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                startActivityForResult(intent, 3);
            }
        });
        spin_sector.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                sector = spin_sector.getItemAtPosition(position).toString();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        return v;
    }

    public void cargaLocales(){
        for(int i=0;i<sectores.length-2;i++) {
            mDatabase.child(sectores[i]).addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    for (DataSnapshot snapshot :
                            dataSnapshot.getChildren()) {
                        LocalesObj local = snapshot.getValue(LocalesObj.class);
                        local.setIdKey(snapshot.getKey());
                        locales.add(local);
                    }
                }

                @Override
                public void onCancelled(DatabaseError databaseError) {
                    Log.e("error update", databaseError.getMessage());
                }
            });
        }
        nombres_locales=new String[locales.size()];
        for (int i=0;i<nombres_locales.length;i++)
            nombres_locales[i]=locales.get(i).getNombre();
    }

    public void registerData(){

        final ProgressDialog progress = new ProgressDialog(getActivity());
        progress.setTitle("Registrando");
        progress.setMessage("Registrando...");
        progress.setCancelable(false);
        progress.show();

        storelogo = FirebaseStorage.getInstance().getReference().child(nombre+"-logo.jpg");
        Uri filelogo = Uri.fromFile(new File(urilogo));
        final UploadTask uploadTasklogo = storelogo.putFile(filelogo);

        storeportada = FirebaseStorage.getInstance().getReference().child(nombre+"-portada.jpg");
        Uri file = Uri.fromFile(new File(uriPortada));
        UploadTask uploadTask = storeportada.putFile(file);
        uploadTask.addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {

            }
        }).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                final String url_portada = taskSnapshot.getDownloadUrl().toString();
                uploadTasklogo.addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {

                    }
                }).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                        String url_logo = taskSnapshot.getDownloadUrl().toString();

                        LocalesObj local = new LocalesObj(
                                0,
                                FirebaseAuth.getInstance().getCurrentUser().getUid(),
                                sector,
                                nombre,
                                desc,
                                0,
                                url_portada,
                                url_logo,
                                "",
                                Double.parseDouble(lat),
                                Double.parseDouble(lon),
                                0,
                                et_descBasic.getText().toString(),
                                et_compartir.getText().toString()
                        );
                        mdatabase.child(sector).push().setValue(local);
                        cleanEt();
                        Toast.makeText(getActivity(),"Registro Exitoso",Toast.LENGTH_SHORT).show();
                        progress.dismiss();
                    }
                });
            }
        });

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

    public void cargaLatLon(double lat, double lon){
        et_lat.setText(String.valueOf(lat));
        et_lon.setText(String.valueOf(lon));
    }

    private void loadData(){
        nombre = et_nombre.getText().toString();
        desc = et_descript.getText().toString();
        lat = et_lat.getText().toString();
        lon = et_lon.getText().toString();
    }

    private void cleanEt(){
        et_nombre.setText("");
        et_descript.setText("");
        et_lat.setText("");
        et_lon.setText("");
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
                iv_logo.setImageBitmap(imge);

                ByteArrayOutputStream oe4 = new ByteArrayOutputStream();
                imge.compress(Bitmap.CompressFormat.PNG, 50, oe4);
                byte[] byteArray4 = oe4.toByteArray();
                logo=Base64.encodeToString(byteArray4,Base64.DEFAULT);
                Log.e("imagen-logo",file.getPath());
                urilogo=file.getPath();
            }
        }else if (requestCode == 3) {
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
                option.inSampleSize=calculateInSampleSize(option,600,400);
                option.inJustDecodeBounds=false;
                Bitmap imge=BitmapFactory.decodeFile(file.getPath(),option);
                iv_portada.setImageBitmap(imge);

                ByteArrayOutputStream oe4 = new ByteArrayOutputStream();
                imge.compress(Bitmap.CompressFormat.PNG, 60, oe4);
                byte[] byteArray4 = oe4.toByteArray();
                portada=Base64.encodeToString(byteArray4,Base64.DEFAULT);
                Log.e("imagen-portada",imgString);
                uriPortada=file.getPath();
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

    class Submit extends AsyncTask<URL,Void,Void> {
        ProgressDialog pd;
        Boolean dis = false,bimg=false;
        byte[] byteImg;
        String msj="error al registrar";
        Activity activity;

        public Submit(Activity activity){this.activity=activity;}
        String url;
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            url="http://publink.esy.es/placeapp/negocios.php";
            pd = new ProgressDialog(getActivity());
            pd.show();
            pd.setTitle("Registrando Negocio");
            pd.setMessage("Un momento porfavor...");
            pd.setIndeterminate(false);
            pd.setCancelable(false);
            loadData();
        }

        @Override
        protected Void doInBackground(URL... params) {
            ArrayList<NameValuePair> parameters = new ArrayList<NameValuePair>();
            parameters.add(new BasicNameValuePair("flag", ""));
            parameters.add(new BasicNameValuePair("id_usu",idusu));
            parameters.add(new BasicNameValuePair("sector",sector));
            parameters.add(new BasicNameValuePair("nombre",nombre));
            parameters.add(new BasicNameValuePair("descripcion",desc));
            parameters.add(new BasicNameValuePair("rating","0"));
            parameters.add(new BasicNameValuePair("lat",lat));
            parameters.add(new BasicNameValuePair("lon",lon));
            JSONObject json = JSONParser.makeHttpRequest(url, "POST", parameters);
            try {
                int success = json.getInt("success");
                if (success == 1) {
                    msj="Registro Exitoso";
                    dis=true;
                    bimg=true;
                }
            } catch (Exception e) {
                Log.e("Exception", e.getMessage());
            }
            if(bimg){
                ArrayList<NameValuePair> parameterss = new ArrayList<NameValuePair>();
                parameterss.add(new BasicNameValuePair("flag", "images"));
                parameterss.add(new BasicNameValuePair("nombre",nombre));
                parameterss.add(new BasicNameValuePair("portada",portada));
                parameterss.add(new BasicNameValuePair("logo",logo));
                parameterss.add(new BasicNameValuePair("imagenes",""));
                json = JSONParser.makeHttpRequest(url, "POST", parameterss);
                try {
                    int success = json.getInt("success");
                    if (success == 1) {
                        msj="Registro Completo Exitoso";
                        dis=true;
                    }
                } catch (Exception e) {
                    Log.e("Exception", e.getMessage());
                }
            }
            return null;
        }

        @Override
        protected void onPostExecute(Void publi) {
            if (dis) {
                Toast.makeText(getActivity(),msj,Toast.LENGTH_SHORT).show();
                cleanEt();
            } else{
                Toast.makeText(getActivity(),msj,Toast.LENGTH_SHORT).show();
            }
            pd.dismiss();
        }
    }
}
