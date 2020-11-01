package Entidades;

import Estructuras.TSB_OAHashtable;

import java.util.ArrayList;
import java.util.Collection;

public class Distrito extends Region {
    private TSB_OAHashtable<String, Seccion> htSecciones = new TSB_OAHashtable<>();


    public Distrito(String c, String n, ArrayList<Agrupacion> agps) {
        super(c, n, agps);
    }

    public Distrito(String c, ArrayList<Agrupacion> agps) {
        super(c, agps);
    }

    Seccion getSeccion(String codSec) {
        return htSecciones.get(codSec);
    }

    void addSeccion(String codSecc, Seccion seccion) {
        htSecciones.put(codSecc, seccion);
    }

    public String toString() {
        String s = "";
        s += this.getCod() + "- " + this.getDesc();
        return s;
    }

    public Collection<Seccion> getAllSections(){
        return htSecciones.values();
    }
}


