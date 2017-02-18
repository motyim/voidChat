/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package view;

import java.net.URL;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import model.User;

/**
 * FXML Controller class
 *
 * @author Merna
 */
public class SendEmailSceneController implements Initializable {

    @FXML
    private TextArea txtAreaEmail;
    @FXML
    private Label txtFrom;
    @FXML
    private Label txtTo;
    @FXML
    private TextField txtFieldSubject;
    @FXML
    private Button btnSend;
    
    ClientView clientView;
    String from,to;

    public SendEmailSceneController(String from , String to) {
        clientView = ClientView.getInstance();
        this.from=from;
        this.to=to;
    }

    /**
     * Initializes the controller class.
     * @param url
     * @param rb
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        txtFrom.setText(from);
        txtTo.setText(to);
    } 
    
    @FXML
    void btnSendAction(ActionEvent event) {
        String body = "<B>"+txtFieldSubject.getText()+"</B><br/>"+txtAreaEmail.getText();
        if(clientView.sendMail(to, body)){
            clientView.showSuccess("Done", "Send Successfully", "mail sended");
        }else{
            clientView.showError("Error", "Can't Send Email", "an problem occure please try again later");
        }
    }
    
}
