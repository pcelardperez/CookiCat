package com.example.pedro.cookicat.dummy;

import android.database.Cursor;

/**
 * Created by pedro on 20/02/15.
 */
public class Ingrediente {

    private String nombre;
    private String tipo;

    public Ingrediente(){}

    public Ingrediente(String idNombre, String idTipo){
        super();
        this.nombre=idNombre;
        this.tipo=idTipo;
    }

    public Ingrediente(String nombre) {
        this.nombre=nombre;
    }
}
