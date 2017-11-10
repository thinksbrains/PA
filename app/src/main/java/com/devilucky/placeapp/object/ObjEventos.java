package com.devilucky.placeapp.object;

/**
 * Created by Hakos on 09/08/2017.
 */

public class ObjEventos {
    String utlImg,nombre,desc,urlExt,idKey;
    public ObjEventos() {
    }

    public ObjEventos(String utlImg, String nombre, String desc, String urlExt) {
        this.utlImg = utlImg;
        this.nombre = nombre;
        this.desc = desc;
        this.urlExt = urlExt;
    }

    public void setIdKey(String idKey) {
        this.idKey = idKey;
    }

    public String getUtlImg() {
        return utlImg;
    }

    public String getNombre() {
        return nombre;
    }

    public String getDesc() {
        return desc;
    }

    public String getUrlExt() {
        return urlExt;
    }

    public String getIdKey() {
        return idKey;
    }
}
