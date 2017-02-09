/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package view;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.Insets;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.HBox;
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
    private TextField txtFieldMsg;
    @FXML
    private Button btnSendAttach;
    @FXML
    private Image clips;
    @FXML
    private ListView<HBox> listviewChat;
    @FXML
    private Button btnSendMsg;
    
    ClientView clientView;

    //3amlt deh
    public ChatBoxController() {
        clientView = new ClientView();
        txtAreaChatBox=new TextArea();
    }


    Boolean sendFlag = true;
    Boolean recFlag = true;


    /**
     * Initializes the controller class.
     *
     * @param url
     * @param rb
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
         Platform.runLater(() -> txtFieldMsg.requestFocus());
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


        File f = new File("src/resouces/chatBoxStyle.css");
        listviewChat.getStylesheets().add("file:///" + f.getAbsolutePath().replace("\\", "/"));

        try {

            HBox cell = new HBox();
            if (sendFlag) {
                if (txtFieldMsg.getText() != null) {
                    ImageView img = new ImageView(new Image(getClass().getResource("..//resouces//user_32.png").openStream()));
                    Label sendLabel = new Label(txtFieldMsg.getText());
                    sendLabel.setMaxWidth(300);
                    sendLabel.setWrapText(true);
                    sendLabel.getStyleClass().add("LabelSender");
                    cell.getChildren().addAll(img, sendLabel);
                }
                sendFlag = false;
            } else {
                Label sendLabel = new Label(txtFieldMsg.getText());
                sendLabel.getStyleClass().add("LabelSenderSec");
                sendLabel.setMaxWidth(300);
                sendLabel.setWrapText(true);
                sendLabel.setPadding(Insets.EMPTY);
                cell.getChildren().add(sendLabel);
                cell.setMargin(sendLabel, new Insets(0, 0, 0, 32));
                recFlag = false;
            }
//           
//           if(!recFlag){
//               if(txtFieldMsg.getText()!=null){
//                    ImageView img = new ImageView(new Image(getClass().getResource("..//resouces//user_32.png").openStream()));
//                    
//                    Label recLabel =new Label(txtFieldMsg.getText());
//                    recLabel.setMaxWidth(300);
//                    recLabel.setWrapText(true);
//                    recLabel.getStyleClass().add("LabelRec");
//                    cell.getChildren().addAll(recLabel,img);
//                    cell.setAlignment(Pos.TOP_RIGHT);
//     
//                }
//           }

            listviewChat.getItems().add(cell);
            txtFieldMsg.setText(null);

        } catch (IOException ex) {
            ex.printStackTrace();
        }
        System.out.println("btnSendMsg Action");
    }


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
