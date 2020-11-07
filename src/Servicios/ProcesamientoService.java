package Servicios;

import Entidades.Agrupacion;
import Entidades.Pais;
import Entidades.Reader;
import javafx.concurrent.Service;
import javafx.concurrent.Task;

import java.io.FileNotFoundException;
import java.util.ArrayList;

public class ProcesamientoService extends Service<ArrayList<Object>> {
    @Override
    protected Task<ArrayList<Object>> createTask() {
        return new Task<ArrayList<Object>>() {
            @Override
            protected ArrayList<Object> call() throws FileNotFoundException, Exception {
                ArrayList<Object> result = new ArrayList<>();
                Reader reader = new Reader();
                ArrayList<Agrupacion> agrupaciones = new ArrayList<>();
                Pais p = new Pais(agrupaciones);
                agrupaciones = reader.getAgrupacionesFromFile();
                reader.getRegionesFromFile(agrupaciones, p);
                reader.getMesasFromFile(agrupaciones, p);
                result.add(agrupaciones);
                result.add(p);
                return result;
            }
        };
    }
}
