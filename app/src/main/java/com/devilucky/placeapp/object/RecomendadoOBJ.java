package com.devilucky.placeapp.object;

import android.graphics.Bitmap;

/**
 * Created by Hakos on 08/06/2017.
 */

public class RecomendadoOBJ {
    private Bitmap imagen;
    private String nombre;
    private int rating;
    public RecomendadoOBJ(Bitmap imagen,String nombre,int rating){
        this.imagen=imagen;
        this.nombre=nombre;
        this.rating=rating;
    }
    public Bitmap getImagen(){return imagen;}
    public String getNombre(){return nombre;}
    public int getRating(){return rating;}
}
