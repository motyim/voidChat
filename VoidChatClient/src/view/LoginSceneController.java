/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package view;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Hyperlink;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;
import utilitez.Checks;

/**
 *
 * @author Merna
 */
public class LoginSceneController implements Initializable {

    @FXML
    private TextField txtUserName;

    @FXML
    private PasswordField txtPassword;

    @FXML
    private Button btnLogin;

    @FXML
    private Hyperlink linkCreateAccount;

    private ClientView clinetView;

    public LoginSceneController() {
        //get instance form view
        clinetView = ClientView.getInstance();
        System.out.println("Login connect Client view");
    }

    @FXML
    private void btnLoginAction(ActionEvent event) {
        try {

            String errorMsg = "";

            //getting data 
            String username = txtUserName.getText();
            String password = txtPassword.getText();

            if (!Checks.checkUserName(username)) {
                errorMsg += "> Invalid Username\n";
            }

            if (!Checks.checkStringLength(password, 6, 50)) {
                errorMsg += "> Invalid Password\n";
            }

            if (!errorMsg.equals("")) {
                clinetView.showError("Login Error", "Login Error", errorMsg);

                return;
            }

            //login to server .. 
            if (clinetView.signin(username, password) == null) {
                clinetView.showError("Login Error", "Login Error", "Can't Login right now ..\n"
                        + "maybe wrong username or password..\n"
                        + "please try again later");

                return;
            }

            //login successfully 
            ((Node) (event.getSource())).getScene().getWindow().hide(); //this line to hide login window ..
            Parent parent = FXMLLoader.load(getClass().getResource("ChatScene.fxml"));
            Stage stage = new Stage();
            Scene scene = new Scene(parent);
            stage.setScene(scene);
            stage.setResizable(false);
            stage.setTitle(" ");
            stage.show();
            stage.setOnCloseRequest((WindowEvent ew) -> {
                Platform.exit();
                //TODO : why not close
                System.exit(0);
            });
        } catch (IOException ex) {
            ex.printStackTrace();
        } catch (Exception ex) {
            ex.printStackTrace();
            clinetView.showError("Login Error", "Login Error", "Can't Login right now ..\n" + ex.getMessage());
        }
    }

    @FXML
    private void linkCreatAccountAction(ActionEvent event) {
        try {
            Parent parent = FXMLLoader.load(getClass().getResource("SignupScence.fxml"));
            Stage stage = clinetView.getMainStage();
            Scene scene = new Scene(parent);
            stage.setScene(scene);
            stage.setTitle("Create an account");
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // Request focus on the txtUserName field by default.
        Platform.runLater(() -> txtUserName.requestFocus());
    }

}
