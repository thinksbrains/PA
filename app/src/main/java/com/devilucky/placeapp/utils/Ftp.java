package com.devilucky.placeapp.utils;

import android.content.Context;
import android.os.Environment;
import android.widget.Toast;

import org.apache.commons.net.ftp.FTP;
import org.apache.commons.net.ftp.FTPClient;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.net.SocketException;

/**
 * Created by Hakos on 25/07/2017.
 */

public class Ftp {
    private static String ip="";
    private static String usuario="";
    private static String password="";
    private FTPClient ftpClient;
    private BufferedInputStream bufferedInputStream;
    private File rutaSD;
    private File rutaCompleta;
    Context context;

    public Ftp(String ip, Context context){
        this.context=context;
        //informacio de conexion
        this.ip=ip;
        this.usuario=null;
        this.password=null;
        //elementos de conexion
        this.ftpClient=null;
        this.bufferedInputStream=null;
        this.rutaSD=null;
        this.rutaCompleta=null;
    }
    public Ftp(String ip,String usuario,String password,Context context){
        this.context=context;
        //informacio de conexion
        this.ip=ip;
        this.usuario=usuario;
        this.password=password;
        //elementos de conexion
        this.ftpClient=null;
        this.bufferedInputStream=null;
        this.rutaSD=null;
        this.rutaCompleta=null;
    }
    //informacion de usuario
    public void setUsuarios(String usuario){this.usuario=usuario;}
    public String getUsuarios(){return usuario;}
    //informacion de contraseña
    public void setPassword(String password){this.password=password;}
    public String getPassword(){return password;}

    public Boolean Login() throws SocketException, IOException{
        Toast.makeText(context, "Conectando . . .", Toast.LENGTH_SHORT).show();

        try{
            ftpClient = new FTPClient();
            ftpClient.user(usuario);
            ftpClient.pass(password);
            ftpClient.connect(ip);
        }catch (Exception e){
            Toast.makeText(context, "Imposible conectar:"+e.getMessage(), Toast.LENGTH_SHORT).show();
            return false;
        }

        //Hace login en el servidor
        if (ftpClient.login(usuario,password)){
            //Informa al usuario
            Toast.makeText(context, "Login correcto . . .", Toast.LENGTH_SHORT).show();
            return true;    //En caso de login correcto
        }else{
            //Informa al usuario
            Toast.makeText(context, "Login incorrecto . . .", Toast.LENGTH_SHORT).show();
            return false;    //En caso de login incorrecto
        }
    }

    public boolean subirArchivo(String nombreArchivo)throws IOException{
        ftpClient.enterLocalActiveMode();
        ftpClient.setFileType(FTP.BINARY_FILE_TYPE);

        //Cambia la carpeta Ftp
        if(ftpClient.changeWorkingDirectory("ServPlaceApp/images")){
            //Informa al usuario
            Toast.makeText(context, "Carpeta ftp cambiada . . .", Toast.LENGTH_SHORT).show();

            //Obtiene la dirección de la ruta sd
            Toast.makeText(context, "Ruta SD obtenida . . .", Toast.LENGTH_SHORT).show();
            rutaSD = Environment.getExternalStorageDirectory();

            //Obtiene la ruta completa donde se encuentra el archivo
            Toast.makeText(context, "Ruta completa archivo obtenida . . .", Toast.LENGTH_SHORT).show();
            rutaCompleta = new File(rutaSD.getAbsolutePath(), nombreArchivo);

            //Crea un buffer hacia el servidor de subida
            bufferedInputStream = new BufferedInputStream(new FileInputStream(rutaCompleta));

            if (ftpClient.storeFile(nombreArchivo, bufferedInputStream)){
                //Informa al usuario
                Toast.makeText(context, "Archivo subido . . .", Toast.LENGTH_SHORT).show();

                bufferedInputStream.close();        //Cierra el bufer
                return true;        //Se ha subido con éxito
            }else{
                //Informa al usuario
                Toast.makeText(context, "Imposible subir archivo . . .", Toast.LENGTH_SHORT).show();
                bufferedInputStream.close();        //Cierra el bufer
                return false;        //No se ha subido
            }
        }else{
            //Informa al usuario
            Toast.makeText(context, "Carpeta ftp imposible cambiar . . .", Toast.LENGTH_SHORT).show();
            return false;
        }
    }
}
