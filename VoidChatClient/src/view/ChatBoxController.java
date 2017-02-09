/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package view;

import java.net.URL;
import java.util.ResourceBundle;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.VBox;
import tray.notification.TrayNotification;

/**
 * FXML Controller class
 *
 * @author Merna
 */
public class ChatBoxController implements Initializable {

    @FXML
    private VBox chatBox;
    @FXML
    private Label labelFriendName;
    @FXML
    private ImageView imgFriendStatus;
    @FXML
    private Label labelFriendStatus;
    @FXML
    private ImageView iconCloseChat;
    @FXML
    private TextArea txtAreaChatBox;
    @FXML
    private TextField txtFieldMsg;
    @FXML
    private Button btnSendAttach;
    @FXML
    private Image clips;
    @FXML
    private Button btnSendMsg;
    
    ClientView clientView;

    //3amlt deh
    public ChatBoxController() {
        clientView = new ClientView();
        txtAreaChatBox=new TextArea();
    }

    
    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO
    }    

    @FXML
    private void iconCloseChatAction(MouseEvent event) {
        
    }

    @FXML
    private void btnSendAttachAction(ActionEvent event) {
         System.out.println("btnSendAttach Action");
    }

    @FXML
    private void btnSendMsgAction(ActionEvent event) {
        System.out.println("btnsend");
         //System.out.println(labelFriendName+" "+txtFieldMsg.getText());
       clientView.sendMsg("Mero",txtFieldMsg.getText());
    }
    
    
    public void reciveMsg(String msg){
        System.out.println("in chatboxcontroller");
       
        
        //Platform.runLater(() -> {
            
        txtAreaChatBox.appendText(msg);
       // });
              
    }
    
    
}
