package com.devilucky.placeapp.object;

/**
 * Created by Hakos on 09/08/2017.
 */

public class ObjPromo {
    private String urlImg,nombre,descript,urlExt,idKey,idLocal;

    public ObjPromo() {
    }

    public ObjPromo(String urlImg, String nombre, String descript, String urlExt) {
        this.urlImg = urlImg;
        this.nombre = nombre;
        this.descript = descript;
        this.urlExt = urlExt;
    }

    public void setIdLocal(String idLocal) {
        this.idLocal = idLocal;
    }

    public String getIdLocal() {
        return idLocal;
    }

    public void setIdKey(String idKey) {
        this.idKey = idKey;
    }

    public String getIdKey() {
        return idKey;
    }

    public String getUrlImg() {
        return urlImg;
    }

    public String getNombre() {
        return nombre;
    }

    public String getDescript() {
        return descript;
    }

    public String getUrlExt() {
        return urlExt;
    }
}
