/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package view;

import java.awt.Insets;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.ResourceBundle;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonBar.ButtonData;
import javafx.scene.control.ButtonType;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Dialog;
import javafx.scene.control.Label;
import javafx.scene.control.MenuButton;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;
import javafx.scene.text.TextFlow;
import javafx.util.Pair;

/**
 * FXML Controller class
 *
 * @author Merna
 */
public class HomeBoxController implements Initializable {

    @FXML
    private VBox homeBox;
    @FXML
    private Button btnNewFriend;
    @FXML
    private Image clips;
    @FXML
    private TextFlow txtFlowServerMsg;

    private ClientView clinetView ;
    
    
    public HomeBoxController() {
        //get instance form view
        clinetView = ClientView.getInstance();
        System.out.println("home connect Client view");
    }

    
    
    
    /**
     * Initializes the controller class.
     *
     * @param url
     * @param rb
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO
    }

    @FXML
    private void btnNewFriendAction(ActionEvent event) {

        ObservableList<String> options
                = FXCollections.observableArrayList(
                        "Family",
                        "Friends"
                );

        Dialog<Pair<String, String>> dialog = new Dialog<>();
        dialog.setTitle("Add New Friend");

        ButtonType addButtonType = new ButtonType("Add", ButtonData.OK_DONE);
        dialog.getDialogPane().getButtonTypes().addAll(addButtonType, ButtonType.CANCEL);

        GridPane grid = new GridPane();
        grid.setHgap(10);
        grid.setVgap(10);
        grid.setPadding(new javafx.geometry.Insets(20, 150, 10, 10));

        TextField txtFieldUserName = new TextField();
        txtFieldUserName.setPromptText("username");

        ComboBox comboBox = new ComboBox(options);
        comboBox.getSelectionModel().selectFirst();
        
        grid.add(new Label("User Name :"), 0, 0);
        grid.add(txtFieldUserName, 1, 0);

        grid.add(new Label("Category:"), 0, 1);
        grid.add(comboBox, 1, 1);


        dialog.getDialogPane().setContent(grid);

        // Request focus on the txtFieldEmail field by default.
        Platform.runLater(() -> txtFieldUserName.requestFocus());

        dialog.setResultConverter(dialogButton -> {
            if (dialogButton == addButtonType) {
                return new Pair<>(txtFieldUserName.getText(), comboBox.getSelectionModel().getSelectedItem().toString());
            }
            return null;
        });

        Optional<Pair<String, String>> result = dialog.showAndWait();
        result.ifPresent(emailCategory -> {
            System.out.println("user=" + emailCategory.getKey() + ", category=" + emailCategory.getValue());
        });

    }

}
