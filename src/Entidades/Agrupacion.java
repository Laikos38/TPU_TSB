package Entidades;

public class Agrupacion {
    private String cod;
    private String nombre;
    private String codCat;
    private Integer votosPantalla = 0;

    public Agrupacion(String c, String n, String cC) {
        this.cod = c;
        this.nombre = n;
        this.codCat = cC;
    }

    public String getCod() {
        return cod;
    }

    public String toString (){
        return cod + " - " + nombre + " - " + codCat + "-" + votosPantalla;
    }

    public Integer getVotosPantalla() {
        return votosPantalla;
    }

    public void setVotosPantalla(Integer votosPantalla) {
        this.votosPantalla = votosPantalla;
    }

    public String getNombre() {
        return nombre;
    }
}
