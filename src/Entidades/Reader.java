package Entidades;

import java.io.File;
import java.io.FileNotFoundException;
import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Scanner;

public class Reader {

    //Método que obtiene todas las agrupaciones del archivo.
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


    //Método que obtiene todas las agrupaciones del archivo.
    public void getRegionesFromFile(ArrayList<Agrupacion> agrupaciones, Pais p) throws FileNotFoundException {
        String fileName = ".\\data\\descripcion_regiones.dsv";

        File file = new File(fileName);
        //chequear esto
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
                    Distrito dist = p.getDistrito(line[0]);

                    if(dist !=null && dist.getDesc().equals("")){
                        dist.setDesc(line[1]);
                        p.addDistrito(dist.getCod(), dist);
                        break;
                    }
                    dist = new Distrito(line[0], line[1], agrupaciones);
                    p.addDistrito(dist.getCod(), dist);

                    break;
                case 5:
                    agregarSeccion(line[0], line[1], agrupaciones, p);
                    break;
                case 11:
                    agregarCircuito(line[0], line[1], agrupaciones, p);
                    break;
                default:
                    break;
            }
        }

        scanner.close();

    }
    //Método que obtiene las mesas totales del archivo.
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
    //Método que parsea las mesas.
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

    private void agregarSeccion(String codSec, String descSec, ArrayList<Agrupacion> agps, Pais p) {

        String codDist = codSec.substring(0,2);
        Seccion secc;

        Distrito dist = p.getDistrito(codDist);

        if (dist != null) {
            secc = dist.getSeccion(codSec);
            //Chequea si la sección había sido creada anteriormente con desc vacía, si es así le setea su desc.
            if (secc!=null && secc.getDesc().equals("")) {
                secc.setDesc(descSec);
                dist.addSeccion(secc.getCod(), secc);
                return;
            }
            secc = new Seccion(codSec, descSec, agps);
            dist.addSeccion(secc.getCod(), secc);
            return;
        }
        dist = new Distrito(codDist, agps);
        p.addDistrito(dist.getCod(), dist);
        secc = new Seccion(codSec, descSec, agps);
        dist.addSeccion(secc.getCod(), secc);
    }

    private void agregarCircuito(String codCirc, String descCirc, ArrayList<Agrupacion> agps, Pais p) {

        Circuito circ = new Circuito(codCirc, descCirc, agps);
        String codSec = circ.getCod().substring(0,5);
        String codDist =  circ.getCod().substring(0,2);

        Seccion seccion;
        Distrito distrito = p.getDistrito(codDist);

        if(distrito == null) {
            agregarSeccion(codSec, "", agps, p);
            distrito = p.getDistrito(codDist);
            seccion = distrito.getSeccion(codSec);
            seccion.addCircuito(circ.getCod(), circ);
        }
        else {
            seccion = distrito.getSeccion(codSec);
            if (seccion == null) {
                agregarSeccion(codSec, "", agps, p);
                seccion = distrito.getSeccion(codSec);
                seccion.addCircuito(circ.getCod(), circ);
            }
            else {
                seccion.addCircuito(circ.getCod(), circ);
            }
        }
    }
}

