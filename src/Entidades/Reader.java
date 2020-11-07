package Entidades;

import java.io.File;
import java.io.FileNotFoundException;
import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Scanner;

public class Reader {

    public ArrayList<Agrupacion> getAgrupacionesFromFile() throws FileNotFoundException {
        ArrayList<Agrupacion> agrupaciones = new ArrayList<>();
        String fileName = ".\\data\\descripcion_postulaciones.dsv";

        File file = new File(fileName);
        Scanner scanner = null;
        try {
            scanner = new Scanner(file);
        } catch (FileNotFoundException e) {
            throw new FileNotFoundException("Archivo no encontrado.");
        }

        while (scanner.hasNext()) {
            String[] result = scanner.nextLine().split("\\|");
            if (result[0].equals("000100000000000")) {
                Agrupacion agp = new Agrupacion(result[2], result[3], result[0]);
                agrupaciones.add(agp);
            }
        }
        scanner.close();

        return agrupaciones;
    }

    public void getRegionesFromFile(ArrayList<Agrupacion> agrupaciones, Pais p) throws FileNotFoundException {
        String fileName = ".\\data\\descripcion_regiones.dsv";
        ArrayList<String[]> linesDistritos = new ArrayList<String[]>();
        ArrayList<String[]> linesSecciones = new ArrayList<String[]>();
        ArrayList<String[]> linesCircuitos = new ArrayList<String[]>();

        File file = new File(fileName);
        Scanner scanner = null;
        try {
            scanner = new Scanner(file);
        } catch (FileNotFoundException e) {
            throw new FileNotFoundException("Archivo no encontrado.");
        }

        while (scanner.hasNext()) {
            String[] line = scanner.nextLine().split("\\|");
            switch (line[0].length()) {
                case 2:
                    Distrito dist = new Distrito(line[0], line[1], agrupaciones);
                    p.addDistrito(dist.getCod(), dist);
                    break;
                case 5:
                    linesSecciones.add(line);
                    break;
                case 11:
                    linesCircuitos.add(line);
                    break;
            }
        }

        scanner.close();

        for(String[] line: linesSecciones) {
            Distrito dist = p.getDistrito(line[0].substring(0,2));
            Seccion secc = new Seccion(line[0].substring(0,5), line[1], agrupaciones);
            dist.addSeccion(secc.getCod(), secc);
        }

        for(String[] line: linesCircuitos) {
            Distrito dist = p.getDistrito(line[0].substring(0,2));
            Seccion secc = dist.getSeccion(line[0].substring(0,5));
            Circuito circuito = new Circuito(line[0], line[1], agrupaciones);
            secc.addCircuito(circuito.getCod(), circuito);
        }

    }

    public void getMesasFromFile(ArrayList<Agrupacion> agrupaciones, Pais p) throws FileNotFoundException {
        String fileName = ".\\data\\mesas_totales_agrp_politica.dsv";

        File file = new File(fileName);
        Scanner scanner = null;
        try {
            scanner = new Scanner(file);
        } catch (FileNotFoundException e) {
            throw new FileNotFoundException("Archivo no encontrado.");
        }

        boolean firstLine = true;
        while (scanner.hasNext()) {
            if (!firstLine) parseMesa(scanner.nextLine(), agrupaciones, p );
            firstLine = false;
        }

        for(Agrupacion a : agrupaciones) {
            a.setVotosPantalla(p.getVotosCont(a.getCod()));
        }

        scanner.close();
    }

    private void parseMesa(String line, ArrayList<Agrupacion> agrupaciones, Pais p) {
        String[] result = line.split("\\|");
        if (!result[4].equals("000100000000000")){
            return;
        }

        String codCirc = result[2];
        String codSec = codCirc.substring(0,5);
        String codDist =  codCirc.substring(0,2);
        String codMesa = result[3];
        String codAgp = result[5];
        Integer cantVotos = Integer.parseInt(result[6]);

        //Actualizar contadores

        p.actualizarContador(codAgp, cantVotos);

        Distrito dist = p.getDistrito(codDist);
        dist.actualizarContador(codAgp, cantVotos);

        Seccion secc = dist.getSeccion(codSec);
        secc.actualizarContador(codAgp, cantVotos);

        Circuito circ = secc.getCircuito(codCirc);
        circ.actualizarContador(codAgp,cantVotos);

        //Busca la mesa en el circuito correspondiente
        Mesa mesa = circ.getMesa(codMesa);
        if (mesa==null) {
            mesa = new Mesa(codMesa, agrupaciones);
            circ.addMesa(mesa.getCod(), mesa);
            mesa = circ.getMesa(codMesa);
        }
        mesa.actualizarContador(codAgp, cantVotos);
    }
}
