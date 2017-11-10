package com.devilucky.placeapp.object;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Base64;

import java.util.List;

/**
 * Created by Hakos on 08/06/2017.
 */

public class LocalesObj {

    private String idusu,nombre,descripcion,sector,horarios,idKey,info,urlShare;
    private String logo,portada;
    int id,rating,promo;
    private Bitmap []imagenes;
    private List<ProductosOBJ> productos;
    private double lat,lon;

    public LocalesObj() {}

    public LocalesObj(int id, String idusu, String sector, String nombre, String descripcion, int rating,
                      String portada, String logo, String imagenes, double lat, double lon,int promo,String info,String urlShare){
        this.id=id;
        this.idusu=idusu;
        this.sector=sector;
        this.logo=logo;
        this.nombre=nombre;
        this.descripcion=descripcion;
        this.rating=rating;
        this.portada=portada;
        this.lat=lat;
        this.lon=lon;
        this.promo=promo;
        this.info=info;
        this.urlShare=urlShare;
    }

    public String getUrlShare() {
        return urlShare;
    }

    public String getInfo() {
        return info;
    }

    public void setInfo(String info) {
        this.info = info;
    }

    public void setIdKey(String idKey){this.idKey=idKey;}

    public int getId(){return id;}
    public String getIdKey(){return idKey;}
    public String getIdusu(){return idusu;}
    public String getSector(){return sector;}
    public String getLogo(){return logo;}
    public String getNombre(){return nombre;}
    public String getDescripcion(){return descripcion;}
    public int getRating(){return rating;}
    public String getPortada(){return portada;}
    public double getLat(){return lat;}
    public double getLon(){return lon;}
    public int getPromo(){return promo;}
}
