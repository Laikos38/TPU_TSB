package Entidades;

public class Contador {
    private String codAgp;
    private Integer votos = 0;

    Contador(String codAP){
        this.codAgp = codAP;
    }

    String getCodAgp() {
        return codAgp;
    }

    Integer getVotos() {
        return votos;
    }

    void setVotos(Integer votos) {
        this.votos = votos;
    }

    @Override
    public String toString() {
        String s = "";
        s += " Votos: " + this.votos +" \n";
        return s;
    }
}
