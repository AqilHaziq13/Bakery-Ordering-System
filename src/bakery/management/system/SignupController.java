/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/javafx/FXMLController.java to edit this template
 */
package bakery.management.system;

import java.io.IOException;
import java.net.URL;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import javax.swing.JOptionPane;

/**
 * FXML Controller class
 *
 * @author aqilh
 */
public class SignupController implements Initializable {

    @FXML
    private Button btnSignup;
    @FXML
    private Button btnPrevPage;
    @FXML
    private PasswordField pfPassword;
    @FXML
    private TextField tfAddress;
    @FXML
    private TextField tfEmail;
    @FXML
    private TextField tfName;

    @FXML
    private void handleButtonAction(ActionEvent event) {
        if (areFieldsEmpty()) {
            JOptionPane.showMessageDialog(null, "Please fill in all the fields.");
        } else {
            int confirm = JOptionPane.showConfirmDialog(null, "Are you sure you want to sign up?", "Confirm Signup", JOptionPane.YES_NO_OPTION);
            if (confirm == JOptionPane.YES_OPTION) {
                if (signupUser()) {
                    openLoginPage(event);
                }
            }
        }
    }

    @FXML
    void handlePrevPage(ActionEvent event) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("FXMLDocument.fxml"));
            Parent root = loader.load();
            // Get the current scene and replace it with the previous page
            Scene scene = ((Node) event.getSource()).getScene();
            scene.setRoot(root);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private boolean signupUser() {
        String query = "INSERT INTO customer VALUES ('" + tfName.getText() + "', '" + tfEmail.getText() + "','" + tfAddress.getText() + "','" + pfPassword.getText() + "')";
        if (executeQuery(query)) {
            JOptionPane.showMessageDialog(null, "Signup Successful");
            return true;
        } else {
            JOptionPane.showMessageDialog(null, "Error: Signup Failed");
            return false;
        }
    }

    private boolean executeQuery(String query) {
        try (Connection conn = getConnection();
             Statement st = conn.createStatement()) {
            st.executeUpdate(query);
            return true;
        } catch (SQLException ex) {
            System.out.println("Error in executing the query: " + ex.getMessage());
            return false;
        }
    }

    private Connection getConnection() {
        try {
            return DriverManager.getConnection("jdbc:mysql://localhost:3306/bakery", "root", "");
        } catch (SQLException ex) {
            System.out.println("Error: " + ex.getMessage());
            return null;
        }
    }

    private void closeWindow() {
        Stage stage = (Stage) btnSignup.getScene().getWindow();
        stage.close();
    }

    private boolean areFieldsEmpty() {
        return tfName.getText().isEmpty() || tfEmail.getText().isEmpty() || tfAddress.getText().isEmpty() || pfPassword.getText().isEmpty();
    }

    private void openLoginPage(ActionEvent event) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("FXMLDocument.fxml"));
            Parent root = loader.load();
            // Get the current scene and replace it with the login page
            Scene scene = ((Node) event.getSource()).getScene();
            scene.setRoot(root);
            // Close the current window
            closeWindow();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO
    }    
    
}

