/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package proyectoobd_base;

import BLL.BuscarDispositivos;
import java.net.URL;
import java.util.ArrayList;
import java.util.ResourceBundle;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.ListView;

/**
 *
 * @author practicas
 */
public class FXMLDocumentController implements Initializable {
    
    @FXML
    private Button but;
    
    @FXML
    private Button salir;
    
    @FXML
    private ListView<String> lista;
    
    private ObservableList<String> items = FXCollections.observableArrayList();
    
    @FXML
    private void handleButtonAction(ActionEvent event) {
        items.clear();
        
        BuscarDispositivos buscar = new BuscarDispositivos();
        
        ArrayList dispositivos = buscar.obtenerDispositivos();
        
        for (int i = 0; i < dispositivos.size(); i++) {
            items.add(dispositivos.get(i).toString());
        }
    }
    
    @FXML
    private void salir(ActionEvent event){
        System.exit(0);
    }
    
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO
        lista.setItems(items);
    }    
    
}
