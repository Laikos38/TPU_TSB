package Entidades;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;

public class Seccion extends Region {
    private HashMap<String, Circuito> htCircuitos = new HashMap<>();

    Seccion(String cod, String desc, ArrayList<Agrupacion> agps) {
        super(cod, desc, agps);
    }

    Circuito getCircuito(String codCirc) {
        return htCircuitos.get(codCirc);
    }

    void addCircuito(String codCirc, Circuito circuito) {
        htCircuitos.put(codCirc, circuito);
    }

    public Collection<Circuito> getAllCircuits(){
        return htCircuitos.values();
    }

    public String toString() {

        String s = "";
        s += this.getCod() + "- " + this.getDesc();
        return s;    }
}
