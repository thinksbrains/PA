package com.devilucky.placeapp.object;

import android.graphics.Bitmap;

/**
 * Created by Hakos on 09/06/2017.
 */

public class ObjUser {
    String user,fullname,correo,telf,date;
    String perfil;
    int id, type;
    public ObjUser(int id,int type,String user,String fullname,String correo,String telf,String perfil,String date){
        this.id=id;
        this.type=type;
        this.user=user;
        this.fullname=fullname;
        this.correo=correo;
        this.telf=telf;
        this.perfil=perfil;
        this.date=date;
    }
    public String getFullname(){return fullname;}
    public String getCorreo(){return correo;}
    public String getTelf(){return telf;}
    public int getId(){return id;}
    public int getType(){return type;}
    public String getPerfil(){return perfil;}
    public String getUser(){return user;}
    public String getDate(){return date;}
    public String getTypeString(){
        if(type==1){
            return "Usuario de Negocio";
        }else if(type==2){
            return "Admministrador";
        }else return "Normal";
    }
}
