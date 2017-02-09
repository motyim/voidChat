/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package view;

import java.net.URL;
import java.util.ArrayList;
import java.util.ResourceBundle;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
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

    private ClientView clinetView;

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
        if (friendsList != null) {
            ArrayList<String> contactsName = new ArrayList<>();
            for (User contact : friendsList) {
                contactsName.add(contact.getUsername());
            }
        }
        //set it within the last if .. this just for test
        ObservableList<String> data = FXCollections.observableArrayList("Merna Ismail", "Roma Attia");
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
                    setGraphic(checkbox);
                }
            }
        });
    }

    @FXML
    void btnCreateAction(ActionEvent event) {

    }

}
