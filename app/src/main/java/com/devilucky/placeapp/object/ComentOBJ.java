package com.devilucky.placeapp.object;

/**
 * Created by Hakos on 11/06/2017.
 */

public class ComentOBJ {
    String id_local,usuario,comentario;
    public ComentOBJ(String usuario,String comentario){
        this.usuario=usuario;
        this.comentario=comentario;
        this.id_local=id_local;
    }

    public ComentOBJ() {
    }

    public String getUsuario(){return usuario;}
    public String getComentario(){return comentario;}
    public String getId_local(){return id_local;}
}
