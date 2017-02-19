/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package view;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import utilitez.Checks;

/**
 * FXML Controller class
 *
 * @author Merna
 */
public class LaunchSceneController implements Initializable {

    @FXML
    private TextField txtFieldHostIP;
    @FXML
    private Button btnConnect;

    ClientView clinetView;

    public LaunchSceneController() {
        clinetView = ClientView.getInstance();
    }

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO
    }

    @FXML
    void btnConnectAction(ActionEvent event) {

        
        String ip = txtFieldHostIP.getText();

        if (!ip.equals("localhost") && !Checks.checkIP(ip)) {
            clinetView.showError("Error", "Wrong Ip address", "please enter a valid ip address");
            return;
        }

        if (!clinetView.conncetToServer(ip)) {
            clinetView.showError("Error", "Server not Exsist", "Server not work or not Exsist on this ip address");
            return;
        }
        try {
            Parent parent = FXMLLoader.load(getClass().getResource("LoginScene.fxml"));
            Stage stage = clinetView.getMainStage();
            Scene scene = new Scene(parent);
            stage.setScene(scene);
        } catch (IOException ex) {
            Logger.getLogger(LaunchSceneController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
}
