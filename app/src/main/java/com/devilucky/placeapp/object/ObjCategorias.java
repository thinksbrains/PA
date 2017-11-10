package com.devilucky.placeapp.object;

/**
 * Created by Hakos on 30/07/2017.
 */

public class ObjCategorias {
    String titulo;
    int imagen;
    public ObjCategorias(String titulo,int imagen){
        this.titulo=titulo;
        this.imagen=imagen;
    }
    public String getTitulo(){return titulo;}
    public int getImagen(){return imagen;}
}
