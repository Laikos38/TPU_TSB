package GUI;

import Entidades.*;
import Servicios.ProcesamientoService;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.concurrent.WorkerStateEvent;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
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
    @FXML
    private ProgressIndicator progressIndicator;

    private ArrayList<Agrupacion> agrupaciones = new ArrayList<>();
    private Pais p = new Pais(agrupaciones);

    public void initialize() throws Exception {
        this.habilitarElementosGUI(false);
        progressIndicator.setVisible(false);
    }

    public void btnCargarDatosClick(javafx.event.ActionEvent actionEvent) {
        cargarDatos();
    }

    public void botonFiltrarClick(javafx.event.ActionEvent actionEvent) {
        filtrar();
    }

    private void cargarDatos() {
        Reader reader = new Reader();
        final ProcesamientoService service = new ProcesamientoService();

        // muestro mensaje de carga
        lblMensajeCargaDatos.setVisible(true);
        lblMensajeCargaDatos.setText("Cargando...");

        // realizo procesamiento de archivos
        progressIndicator.setVisible(true);
        service.setOnSucceeded(new EventHandler<WorkerStateEvent>() {
            @Override
            public void handle(WorkerStateEvent workerStateEvent) {
                ArrayList<Object> result = service.getValue();
                agrupaciones = (ArrayList<Agrupacion>) result.get(0);
                p = (Pais) result.get(1);

                cargarCombos();
                crearTabla(agrupaciones);
                habilitarElementosGUI(true);

                // oculto mensaje de carga
                progressIndicator.setVisible(false);
                lblMensajeCargaDatos.setVisible(true);
                lblMensajeCargaDatos.setText("Archivos cargados y procesados con éxito.");

            }
        });

        service.setOnFailed(new EventHandler<WorkerStateEvent>() {
            @Override
            public void handle(WorkerStateEvent workerStateEvent) {
                String e = service.getException().getMessage();
                if (e.equals("Archivo no encontrado.")) {
                    lblMensajeCargaDatos.setVisible(true);
                    lblMensajeCargaDatos.setText("No se pudieron cargar los archivos.\nAsegúrese de ubicarlos en " +
                            "el directorio data del proyecto");
                } else {
                    lblMensajeCargaDatos.setVisible(true);
                    lblMensajeCargaDatos.setText("Ocurrió un error inesperado.");
                }
                progressIndicator.setVisible(false);
            }
        });

        service.restart();
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
        //cmbDistritos.getSelectionModel().selectFirst();

        cmbSecciones.getItems().add("Todos");
        //cmbSecciones.getSelectionModel().selectFirst();

        cmbCircuitos.getItems().add("Todos");
        //cmbCircuitos.getSelectionModel().selectFirst();
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
        codeColumn.setStyle( "-fx-alignment: CENTER;");

        TableColumn<Agrupacion, String> nameColumn  = new TableColumn<>("Nombre");
        nameColumn.setCellValueFactory(new PropertyValueFactory<Agrupacion, String>("nombre"));
        nameColumn.setStyle( "-fx-alignment: CENTER;");

        TableColumn<Agrupacion, String> votesColumn  = new TableColumn<>("Cantidad");
        votesColumn.setCellValueFactory(new PropertyValueFactory<Agrupacion, String>("votosPantalla"));
        votesColumn.setStyle( "-fx-alignment: CENTER;");
        tblVotos.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);
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
