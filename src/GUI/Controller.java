package GUI;

import Entidades.*;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;

import java.io.FileNotFoundException;
import java.util.ArrayList;

public class Controller {
    @FXML
    public ComboBox cmbDistritos, cmbSecciones, cmbCircuitos;
    @FXML
    public TableView tblVotos;
    @FXML
    public Label lblMensajeCargaDatos;
    @FXML
    public Button btnFiltrar;

    private ArrayList<Agrupacion> agrupaciones = new ArrayList<>();
    private Pais p = new Pais(agrupaciones);

    public void initialize() throws Exception {
        this.habilitarElementosGUI(false);
    }

    public void btnCargarDatosClick(javafx.event.ActionEvent actionEvent) {
        cargarDatos();
    }

    public void botonFiltrarClick(javafx.event.ActionEvent actionEvent) {
        filtrar();
    }

    private void cargarDatos() {
        Reader reader = new Reader();
        try {
            // muestro mensaje de carga
            lblMensajeCargaDatos.setVisible(true);
            lblMensajeCargaDatos.setText("Cargando...");
            // realizo procesamiento de archivos
            this.agrupaciones = reader.getAgrupacionesFromFile();
            reader.getRegionesFromFile(agrupaciones, p);
            reader.getMesasFromFile(agrupaciones, p);
            // oculto mensaje de carga
            lblMensajeCargaDatos.setVisible(true);
            lblMensajeCargaDatos.setText("Archivos cargados y procesados con éxito.");
        } catch (FileNotFoundException e) {
            lblMensajeCargaDatos.setVisible(true);
            lblMensajeCargaDatos.setText("No se pudieron cargar los archivos. Asegúrese de ubicarlos en " +
                    "el directorio data del proyecto");
        } catch (Exception e) {
            lblMensajeCargaDatos.setVisible(true);
            lblMensajeCargaDatos.setText("Ocurrió un error inesperado.");
            return;
        }


        for(Agrupacion as: agrupaciones) {
            System.out.println(as.getNombre() + ": " + as.getVotosPantalla());
        }

        this.cargarCombos();
        this.crearTabla(agrupaciones);
        this.habilitarElementosGUI(true);
    }

    private void habilitarElementosGUI(boolean set) {
        if (!set) {
            cmbSecciones.setDisable(true);
            cmbCircuitos.setDisable(true);
            cmbDistritos.setDisable(true);
            btnFiltrar.setDisable(true);
            tblVotos.setDisable(true);
        } else {
            cmbSecciones.setDisable(false);
            cmbCircuitos.setDisable(false);
            cmbDistritos.setDisable(false);
            btnFiltrar.setDisable(false);
            tblVotos.setDisable(false);
        }
    }

    private void cargarCombos() {
        ObservableList<Distrito> obsDistritos = FXCollections.observableArrayList();
        obsDistritos.addAll(p.getAllDistricts());

        cmbDistritos.getItems().add("Todos");
        cmbDistritos.getItems().addAll(obsDistritos);
        cmbDistritos.getSelectionModel().selectFirst();

        cmbSecciones.getItems().add("Todos");
        cmbSecciones.getSelectionModel().selectFirst();

        cmbCircuitos.getItems().add("Todos");
        cmbCircuitos.getSelectionModel().selectFirst();
    }

    public void cmbDistritosChangeSelection(ActionEvent actionEvent) {
        ObservableList<Seccion> obsSections = FXCollections.observableArrayList();

        if (cmbDistritos.getValue() != null && cmbDistritos.getValue() != "Todos" ){
            Distrito selected = (Distrito) cmbDistritos.getValue();
            obsSections.addAll(selected.getAllSections());
        }

        cmbSecciones.getItems().clear();
        cmbSecciones.getItems().add("Todos");
        cmbSecciones.getItems().addAll(obsSections);
        cmbSecciones.getSelectionModel().selectFirst();
    }


    public void cmbSeccioneschangeSelection(ActionEvent actionEvent) {
        ObservableList<Circuito> circuitos = FXCollections.observableArrayList();

        if (cmbSecciones.getValue() != null && cmbSecciones.getValue() != "Todos") {
            Seccion selected = (Seccion) cmbSecciones.getValue();

            circuitos.addAll(selected.getAllCircuits());
        }

        cmbCircuitos.getItems().clear();
        cmbCircuitos.getItems().add("Todos");
        cmbCircuitos.getItems().addAll(circuitos);
        cmbCircuitos.getSelectionModel().selectFirst();
    }

    private void crearTabla(ArrayList<Agrupacion> agrupaciones) {
        tblVotos.getItems().clear();
        tblVotos.getColumns().clear();
        ObservableList<Agrupacion> obsAgrupaciones = FXCollections.observableArrayList();
        obsAgrupaciones.addAll(agrupaciones);

        TableColumn<Agrupacion, String> codeColumn  = new TableColumn<>("Codigo");
        codeColumn.setCellValueFactory(new PropertyValueFactory<Agrupacion, String>("cod"));

        TableColumn<Agrupacion, String> nameColumn  = new TableColumn<>("Nombre");
        nameColumn.setCellValueFactory(new PropertyValueFactory<Agrupacion, String>("nombre"));

        TableColumn<Agrupacion, String> votesColumn  = new TableColumn<>("Cantidad");
        votesColumn.setCellValueFactory(new PropertyValueFactory<Agrupacion, String>("votosPantalla"));
        tblVotos.getColumns().addAll(codeColumn, nameColumn, votesColumn);
        tblVotos.getItems().addAll(obsAgrupaciones);
    }

    private void filtrar() {
        Distrito distrito = null;
        Seccion seccion = null;
        Circuito circuito = null;
        Mesa mesa = null;
        try {
            distrito = (Distrito) cmbDistritos.getValue();
        } catch (Exception e) {
        }
        try {
            seccion = (Seccion) cmbSecciones.getValue();
        } catch (Exception e) {
        }
        try {
            circuito = (Circuito) cmbCircuitos.getValue();
        } catch (Exception e) {
        }
        ArrayList<Agrupacion> results;

        if (distrito == null) {
            results = setearValores(agrupaciones, p);
            crearTabla(results);
            return;
        }
        if (seccion == null) {
            results = setearValores(agrupaciones, distrito);
            crearTabla(results);
            return;
        }
        if (circuito == null) {
            results = setearValores(agrupaciones, seccion);
            crearTabla(results);
            return;
        }
        if (mesa == null) {
            results = setearValores(agrupaciones, circuito);
            crearTabla(results);
        } else {
            results = setearValores(agrupaciones, mesa);
            crearTabla(results);
        }
    }

    private ArrayList<Agrupacion> setearValores (ArrayList<Agrupacion> agps, Region reg) {
        for (Agrupacion a : agps) {
            a.setVotosPantalla(reg.getVotosCont(a.getCod()));
        }
        return agps;
    }

}
