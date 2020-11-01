package Entidades;

import Estructuras.TSB_OAHashtable;

import java.util.ArrayList;

public class Region {
    private String cod;
    private String desc;
    private TSB_OAHashtable<String, Contador> htContadores = new TSB_OAHashtable<>();

    Region(String c, String n, ArrayList<Agrupacion> agps) {
        cod = c;
        desc = n;
        crearContadores(agps);
    }

    Region(String c, ArrayList<Agrupacion> agps) {
        cod = c;
        desc = "";
        crearContadores(agps);
    }

    private void crearContadores(ArrayList<Agrupacion> agps) {
        for(Agrupacion a: agps){
            Contador cont = new Contador(a.getCod());
            htContadores.put(cont.getCodAgp(), cont);
        }
    }

    String getCod() {
        return cod;
    }

    String getDesc() {
        return desc;
    }

    void setDesc(String desc) {
        this.desc = desc;
    }

    void actualizarContador(String codAgp, Integer cantVotos) {

        Contador cont =  htContadores.get(codAgp);
        if (cont == null){
            addContador(codAgp);
            cont = htContadores.get(codAgp);
        }

        Integer votos = cont.getVotos();
        votos += cantVotos;
        cont.setVotos(votos);
        htContadores.put(cont.getCodAgp(), cont);
    }

    private void addContador(String codAgp){
        Contador cont = new Contador(codAgp);
        htContadores.put(codAgp, cont);
    }

    public Integer getVotosCont(String codAgp){
        return htContadores.get(codAgp).getVotos();
    }

    //String mostrarVotos(){
    //    return htContadores.toString();
    //}
}
