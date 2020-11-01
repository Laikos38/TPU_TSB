package Entidades;

import java.util.ArrayList;

public class Mesa extends  Region{

    Mesa(String cod, ArrayList<Agrupacion> agps) {
        super(cod, agps);
    }

    public String toString (){
        String s = "";
        s += this.getCod() + "- " + this.getDesc();
        return s;
    }

}
