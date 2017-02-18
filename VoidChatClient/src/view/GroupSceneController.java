/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package view;

import java.net.URL;
import java.util.ArrayList;
import java.util.Date;
import java.util.ResourceBundle;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import javafx.scene.text.Text;
import model.User;

/**
 * FXML Controller class
 *
 * @author Merna
 */
public class GroupSceneController implements Initializable {

    @FXML
    private TextField txtFieldGroupName;
    @FXML
    private ListView<String> listviewGroup;
    @FXML
    private Button btnCreate;
    @FXML
    private Text txtErrorGroupName;

    private ClientView clinetView;

    ArrayList<String> groupMembers = new ArrayList<>();

    public GroupSceneController() {
        //get instance form view
        clinetView = ClientView.getInstance();
    }

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        ArrayList<User> friendsList = clinetView.getContacts();
        ArrayList<String> contactsName = new ArrayList<>();

        if (friendsList != null) {
            for (User contact : friendsList) {
                contactsName.add(contact.getUsername());
            }
        }
        //set it within the last if .. this just for test
        ObservableList<String> data = FXCollections.observableArrayList(contactsName);
        listviewGroup.setItems(data);
        listviewGroup.setCellFactory(listView -> new ListCell<String>() {
            @Override
            protected void updateItem(String item, boolean empty) {
                super.updateItem(item, empty);
                if (empty || item == null) {
                    setText(null);
                    setGraphic(null);
                } else {
                    CheckBox checkbox = new CheckBox(item);
                    checkbox.setOnAction(new EventHandler<ActionEvent>() {
                        @Override
                        public void handle(ActionEvent event) {
                            if (!groupMembers.contains(checkbox.getText())) {
                                groupMembers.add(checkbox.getText());
                            } else {
                                groupMembers.remove(checkbox.getText());
                            }
                        }
                    });
                    setGraphic(checkbox);
                }
            }
        });

        txtErrorGroupName.setVisible(false);
    }

    @FXML
    void btnCreateAction(ActionEvent event) {
////////////////////
//        String groupName = "group##" + clinetView.getUserInformation().getUsername() + "##" + txtFieldGroupName.getText();
         String groupName = "group##" + new Date().toString() + "##" + txtFieldGroupName.getText();
///////////////////        


        clinetView.chatSceneController.createGroup(groupName);
        groupMembers.add(clinetView.getUserInformation().getUsername());
        if (!clinetView.chatSceneController.tabsOpened.containsKey(groupName)) {
            clinetView.createGroup(groupName, groupMembers);
            ((Node) (event.getSource())).getScene().getWindow().hide();
        } else {
            //3ayzen n3ml notification hna 2n fe group b nfs 2l 2sm
            txtErrorGroupName.setVisible(true);
        }

    }

}
