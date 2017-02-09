/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package view;

import java.net.URL;
import java.util.ResourceBundle;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.text.Text;
import model.User;

/**
 * FXML Controller class
 *
 * @author MY-PC
 */
public class UserInfoController implements Initializable {
   
    User user;
    
    @FXML
    private Text txtUserName;

    @FXML
    private Text txtEmail;

    @FXML
    private Text txtFullName;

    @FXML
    private Text txtCountry;

    @FXML
    private Text txtGender;

    @FXML
    private Text txtStatus;

    public UserInfoController(User user) {
        this.user=user;
    }
    
    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        txtUserName.setText(user.getUsername());
        txtEmail.setText(user.getEmail());
        txtFullName.setText(user.getFname()+" "+user.getLname());
        txtCountry.setText(user.getCountry());
        txtGender.setText(user.getGender());
        txtStatus.setText(user.getStatus());
    }    
    
}
