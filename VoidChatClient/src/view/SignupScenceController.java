package view;

import java.io.IOException;
import java.net.URL;
import java.util.Optional;
import java.util.ResourceBundle;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.ComboBox;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;
import model.User;
import utilitez.Checks;

/**
 * FXML Controller class
 *
 * @author Merna
 */
public class SignupScenceController implements Initializable {

    private ClientView clinetView;

    @FXML
    private TextField txtFName;

    @FXML
    private TextField txtLName;

    @FXML
    private TextField txtUserName;

    @FXML
    private TextField txtEmail;

    @FXML
    private PasswordField txtPassword;

    @FXML
    private ComboBox<String> comboboxGender;

    ObservableList<String> genderList = FXCollections.observableArrayList("Female", "Male");

    @FXML
    private ComboBox<String> comboboxCountry;

    ObservableList<String> counrtyList = FXCollections.observableArrayList("Afghanistan", "Albania", "Algeria", "American Samoa", "Andorra", "Angola", "Anguilla", "Antarctica",
            "Antigua and Barbuda", "Argentina", "Armenia", "Aruba", "Australia", "Austria", "Azerbaijan", "Bahamas", "Bahrain", "Bangladesh", "Barbados", "Belarus", "Belgium",
            "Belize", "Benin", "Bermuda", "Bhutan", "Bolivia", "Bosnia and Herzegowina", "Botswana", "Bouvet Island", "Brazil", "British Indian Ocean Territory", "Brunei Darussalam",
            "Bulgaria", "Burkina Faso", "Burundi", "Cambodia", "Cameroon", "Canada", "Cape Verde", "Cayman Islands", "Central African Republic", "Chad", "Chile", "China",
            "Christmas Island", "Cocos (Keeling) Islands", "Colombia", "Comoros", "Congo", "Congo, the Democratic Republic of the", "Cook Islands", "Costa Rica", "Cote d'Ivoire",
            "Croatia (Hrvatska)", "Cuba", "Cyprus", "Czech Republic", "Denmark", "Djibouti", "Dominica", "Dominican Republic", "East Timor", "Ecuador", "Egypt", "El Salvador",
            "Equatorial Guinea", "Eritrea", "Estonia", "Ethiopia", "Falkland Islands (Malvinas)", "Faroe Islands", "Fiji", "Finland", "France", "France Metropolitan", "French Guiana",
            "French Polynesia", "French Southern Territories", "Gabon", "Gambia", "Georgia", "Germany", "Ghana", "Gibraltar", "Greece", "Greenland", "Grenada", "Guadeloupe", "Guam",
            "Guatemala", "Guinea", "Guinea-Bissau", "Guyana", "Haiti", "Heard and Mc Donald Islands", "Holy See (Vatican City State)", "Honduras", "Hong Kong", "Hungary", "Iceland",
            "India", "Indonesia", "Iran (Islamic Republic of)", "Iraq", "Ireland", "Israel", "Italy", "Jamaica", "Japan", "Jordan", "Kazakhstan", "Kenya", "Kiribati", "Korea",
            "Kuwait", "Kyrgyzstan", "Lao, People's Democratic Republic", "Latvia", "Lebanon", "Lesotho", "Liberia", "Libyan Arab Jamahiriya", "Liechtenstein", "Lithuania", "Luxembourg",
            "Macau", "Macedonia, The Former Yugoslav Republic of", "Madagascar", "Malawi", "Malaysia", "Maldives", "Mali", "Malta", "Marshall Islands", "Martinique", "Mauritania",
            "Mauritius", "Mayotte", "Mexico", "Micronesia, Federated States of", "Moldova, Republic of", "Monaco", "Mongolia", "Montserrat", "Morocco", "Mozambique", "Myanmar", "Namibia",
            "Nauru", "Nepal", "Netherlands", "Netherlands Antilles", "New Caledonia", "New Zealand", "Nicaragua", "Niger", "Nigeria", "Niue", "Norfolk Island", "Northern Mariana Islands",
            "Norway", "Oman", "Pakistan", "Palau", "Panama", "Papua New Guinea", "Paraguay", "Peru", "Philippines", "Pitcairn", "Poland", "Portugal", "Puerto Rico", "Qatar", "Reunion",
            "Romania", "Russian Federation", "Rwanda", "Saint Kitts and Nevis", "Saint Lucia", "Saint Vincent and the Grenadines", "Samoa", "San Marino", "Sao Tome and Principe",
            "Saudi Arabia", "Senegal", "Seychelles", "Sierra Leone", "Singapore", "Slovakia (Slovak Republic)", "Slovenia", "Solomon Islands", "Somalia", "South Africa",
            "Spain", "Sri Lanka", "St. Helena", "St. Pierre and Miquelon", "Sudan", "Suriname", "Svalbard and Jan Mayen Islands", "Swaziland", "Sweden", "Switzerland", "Syrian Arab Republic",
            "Taiwan, Province of China", "Tajikistan", "Tanzania, United Republic of", "Thailand", "Togo", "Tokelau", "Tonga", "Trinidad and Tobago", "Tunisia", "Turkey", "Turkmenistan",
            "Turks and Caicos Islands", "Tuvalu", "Uganda", "Ukraine", "United Arab Emirates", "United Kingdom", "United States", "United States Minor Outlying Islands", "Uruguay", "Uzbekistan",
            "Vanuatu", "Venezuela", "Vietnam", "Virgin Islands (British)", "Virgin Islands (U.S.)", "Wallis and Futuna Islands", "Western Sahara", "Yemen", "Yugoslavia", "Zambia", "Zimbabwe");

    @FXML
    private Button btnSignup;

    public SignupScenceController() {
        //get instance form view
        clinetView = ClientView.getInstance();
       
    }

    @FXML
    private void btnSignupAction(ActionEvent event) {

        String errorMsg = "";

        //getting data 
        String fName = txtFName.getText();
        String lName = txtLName.getText();
        String username = txtUserName.getText();
        String email = txtEmail.getText();
        String password = txtPassword.getText();
        String gender = comboboxGender.getValue();
        String country = comboboxCountry.getValue();

        //validate data
        if (!Checks.checkName(fName)) {
            errorMsg += "> invalid first name it must be alphabtic and minimum 3 and maximum 10\n";
        }

        if (!Checks.checkName(lName)) {
            errorMsg += "> invalid last name it must be alphabtic and minimum 3 and maximum 10\n";
        }

        if (!Checks.checkUserName(username)) {
            errorMsg += "> invalid username it must start by characters \nand can contain _ . and from 3 upto 20 characters\n";
        }

        if (!Checks.checkEmail(email)) {
            errorMsg += "> invalid email address\n";
        }

        if (!Checks.checkStringLength(password, 6, 50)) {
            errorMsg += "> password must be at least 6 characters\n";
        }

        if (!errorMsg.equals("")) {
            clinetView.showError("Signup Error", "Signup Error", errorMsg);
            return;
        }

        User user = new User(username, email, fName, lName, password, gender, country);
        boolean flag = false;

        try {
            flag = clinetView.signup(user);
        } catch (Exception ex) {
            clinetView.showError("Signup Error", "Signup Error", "Can't Signup right now ..\n" + ex.getMessage());
            return;
        }

        if (!flag) {
         ////////
            //clinetView.showError("Signup Error", "Signup Error", "Can't Signup right now ..\nplease try again later");
            clinetView.showError("Signup Error", "Signup Error", "this user name is already taken");
        /////
        } else {
            Alert alertSuccess = new Alert(AlertType.INFORMATION);
            alertSuccess.setTitle("Signup Successfully");
            alertSuccess.setHeaderText("Signup Successfully");
            alertSuccess.setContentText("Welcome to Our Community\nplease back and login");
            Optional<ButtonType> result = alertSuccess.showAndWait();
            if (result.get() == ButtonType.OK) {
                try {
                    Parent parent = FXMLLoader.load(getClass().getResource("LoginScene.fxml"));
                    Stage stage = clinetView.getMainStage();
                    Scene scene = new Scene(parent);
                    stage.setScene(scene);
                } catch (IOException ex) {
                    ex.printStackTrace();
                }

            }

        }

    }

    /**
     * Initializes the controller class.
     *
     * @param url
     * @param rb
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        comboboxGender.setItems(genderList);
        comboboxCountry.setItems(counrtyList);
        btnSignup.setDefaultButton(true);
    }

    @FXML
    private void btnBackAction(MouseEvent event) {
        try {
            Parent parent = FXMLLoader.load(getClass().getResource("LoginScene.fxml"));
            Stage stage = clinetView.getMainStage();
            Scene scene = new Scene(parent);
            stage.setScene(scene);
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }

}
