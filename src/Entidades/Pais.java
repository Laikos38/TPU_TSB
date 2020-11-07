package Entidades;

import Estructuras.TSB_OAHashtable;

import java.util.ArrayList;
import java.util.Collection;

public class Pais extends Region {

    private TSB_OAHashtable<String, Distrito> htDistritos = new TSB_OAHashtable<>();

    public Pais(ArrayList<Agrupacion> agps) {
        super("0", agps);
    }

    Distrito getDistrito(String codDist) {
        return htDistritos.get(codDist);
    }

    void addDistrito(String codDist, Distrito distrito) {
        htDistritos.put(codDist, distrito);
    }

    public Collection<Distrito> getAllDistricts(){
        return htDistritos.values();
    }


}
