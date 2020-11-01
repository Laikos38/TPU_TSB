package GUI;

import Entidades.Agrupacion;
import Entidades.Pais;
import Entidades.Reader;

import java.util.ArrayList;

public class Controller {
    private ArrayList<Agrupacion> agrupaciones = new ArrayList<>();
    private Pais p = new Pais(agrupaciones);

    public void initialize() throws Exception {
        Reader reader = new Reader();
        this.agrupaciones = reader.getAgrupacionesFromFile();
        reader.getRegionesFromFile(agrupaciones, p);
        reader.getMesasFromFile(agrupaciones, p);

        for(Agrupacion as: agrupaciones) {
            System.out.println(as.getNombre() + ": " + as.getVotosPantalla());
        }

    }

}
