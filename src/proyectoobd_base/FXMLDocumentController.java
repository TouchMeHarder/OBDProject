/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package proyectoobd_base;

import BLL.BuscarDispositivos;
import BLL.NewClass;
import eu.hansolo.medusa.Gauge;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Random;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.animation.AnimationTimer;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.Insets;
import javafx.scene.control.Button;
import javafx.scene.control.ListView;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.layout.CornerRadii;
import javafx.scene.layout.StackPane;
import javax.bluetooth.RemoteDevice;

/**
 *
 * @author practicas
 */
public class FXMLDocumentController implements Initializable {

    private NewClass gauge;
    private long lastTimerCall;
    private AnimationTimer timer;
    private static final Random RND = new Random();
    private ArrayList dispositivos;

    @FXML
    private Button but;

    @FXML
    private Button salir;

    @FXML
    private ListView<String> lista;
    @FXML
    private ListView<String> comandos;

    @FXML
    private StackPane pane;

    private ObservableList<String> items = FXCollections.observableArrayList();
    private ObservableList<String> opciones = FXCollections.observableArrayList("RPM", "TrubleCodes");

    @FXML
    private void handleButtonAction(ActionEvent event) {
        /*items.clear();

        BuscarDispositivos buscar = new BuscarDispositivos();

        ArrayList dispositivos = buscar.obtenerDispositivos();

        for (int i = 0; i < dispositivos.size(); i++) {
            items.add(dispositivos.get(i).toString());
        }*/

        new Thread(new Dispositivos()).start();
        new Thread(new RPM()).start();
    }

    @FXML
    private void salir(ActionEvent event) {
        System.exit(0);
    }

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO
        lista.setItems(items);
        
        comandos.setItems(opciones);

        gauge = new NewClass();

        pane.setBackground(new Background(new BackgroundFill(Gauge.DARK_COLOR, CornerRadii.EMPTY, Insets.EMPTY)));
        pane.getChildren().add(gauge);
    }
    
    public class EjecutarComando implements Runnable {
        @Override
        public void run() {
            int aux = lista.getSelectionModel().getSelectedIndex();
            String codigo = comandos.getSelectionModel().getSelectedItem();
            
            RemoteDevice rd = (RemoteDevice) dispositivos.get(aux);
        }
    }

    public class Dispositivos implements Runnable {

        @Override
        public void run() {
            Platform.runLater(new Runnable() {
                @Override
                public void run() {
                    items.clear();
                }
            });
            BuscarDispositivos buscar = new BuscarDispositivos();

            dispositivos = buscar.obtenerDispositivos();

            for (int i = 0; i < dispositivos.size(); i++) {
                try {
                    RemoteDevice rd = (RemoteDevice) dispositivos.get(i);
                    String nombre = rd.getFriendlyName(false);
                    Thread.sleep(100);
                    Platform.runLater(new Runnable() {
                        @Override
                        public void run() {
                            items.add(nombre);
                        }
                    });
                } catch (InterruptedException ex) {
                    Logger.getLogger(FXMLDocumentController.class.getName()).log(Level.SEVERE, null, ex);
                } catch (IOException ex) {
                    Logger.getLogger(FXMLDocumentController.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        }

    }

    public class RPM implements Runnable {

        @Override
        public void run() {
            Platform.runLater(new Runnable() {
                @Override
                public void run() {
                    gauge.getRpmGauge().setValue(2500);
                    gauge.getTempGauge().setValue(10);
                    gauge.getOilGauge().setValue(50);
                }
            });
        }
    }

}
