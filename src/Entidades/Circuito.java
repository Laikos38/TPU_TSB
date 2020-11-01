package Entidades;

import Estructuras.TSB_OAHashtable;

import java.util.ArrayList;
import java.util.Collection;

public class Circuito extends Region {
    private TSB_OAHashtable<String, Mesa> htMesas = new TSB_OAHashtable<>();

    public Circuito(String cod, String desc, ArrayList<Agrupacion> agps){
        super(cod, desc, agps);
    }

    Mesa getMesa(String codMesa){
        return htMesas.get(codMesa);
    }

    void addMesa(String codMesa, Mesa mesa){
        htMesas.put(codMesa, mesa);
    }

    public String toString() {
        String s = "";
        s += this.getCod() + "- " + this.getDesc();
        return s;    }

    public Collection<Mesa> getAllMesas(){
        return htMesas.values();
    }

}
