/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/javafx/FXML2.java to edit this template
 */
package bakery.management.system;

import java.io.IOException;
import java.net.URL;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import javax.swing.JOptionPane;

public class FXMLDocumentController implements Initializable {

    @FXML
    private TextField txtname;

    @FXML
    private TextField txtpassword;

    @FXML
    private Button btnLogin;
    
    @FXML
    private String customerEmail;

    Connection con;
    PreparedStatement pst;
    ResultSet rs;

    @FXML
    void Login(ActionEvent event) {
        String username = txtname.getText();
        String password = txtpassword.getText();

        if (username.equals("") || password.equals("")) {
            JOptionPane.showMessageDialog(null, "Email or Password Blank");
        } else {
            try {
                Class.forName("com.mysql.jdbc.Driver");
                con = DriverManager.getConnection("jdbc:mysql://localhost/bakery", "root", "");

                // Check if the user is an admin
                pst = con.prepareStatement("SELECT * FROM admin where email = ? and password = ? ");
                pst.setString(1, username);
                pst.setString(2, password);
                rs = pst.executeQuery();

                if (rs.next()) {
                    // Admin login
                    JOptionPane.showMessageDialog(null, "Admin Login Success");
                    // Open admin page
                    openAdminPage();
                    return; // Exit the method to prevent checking for customer login
                }

                // Check if the user is a customer
                pst = con.prepareStatement("SELECT * FROM customer where email = ? and password = ? ");
                pst.setString(1, username);
                pst.setString(2, password);
                rs = pst.executeQuery();

                if (rs.next()) {
                    // Customer login
                    JOptionPane.showMessageDialog(null, "Customer Login Success");
                    customerEmail = username; // Store the email
                    // Open customer page
                    openCustomerPage();
                    return; // Exit the method
                }

                // If neither admin nor customer found
                JOptionPane.showMessageDialog(null, "Invalid Email or Password");

                // Clear the text fields
                txtname.setText("");
                txtpassword.setText("");

            } catch (ClassNotFoundException | SQLException ex) {
                Logger.getLogger(FXMLDocumentController.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }

    // Method to open the admin page
    @FXML
    private void openAdminPage() {
        // Open admin page here
        try {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("Admin.fxml"));
        Parent root = loader.load();
        Scene scene = new Scene(root);
        Stage stage = new Stage();
        stage.setScene(scene);
        stage.show();

        // Close the current login window
        Stage loginStage = (Stage) btnLogin.getScene().getWindow();
        loginStage.close();
    } catch (IOException e) {
        e.printStackTrace();
    }
    }

    // Method to open the customer page
    @FXML
    private void openCustomerPage() {
        // Open customer page here
        try {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("CustomerMenu.fxml"));
        Parent root = loader.load();
        
        // Get the controller instance and set the customer email
        CustomerMenuController controller = loader.getController();
        controller.setCustomerEmail(customerEmail);

        
        Scene scene = new Scene(root);
        Stage stage = new Stage();
        stage.setScene(scene);
        stage.show();

        // Close the current login window
        Stage loginStage = (Stage) btnLogin.getScene().getWindow();
        loginStage.close();
    } catch (IOException e) {
        e.printStackTrace();
    }
    }


    @FXML
    private void goToSignUp(ActionEvent event) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("Signup.fxml"));
            Parent root = loader.load();

            // Get the current scene and replace it with the previous page
            Scene scene = ((Node)event.getSource()).getScene();
            scene.setRoot(root);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // Initialization code goes here
        txtname.setText("");
    txtpassword.setText("");
    
    // You can also register event handlers for UI components
    btnLogin.setOnAction(this::loginButtonClicked);
    }
    
    private void loginButtonClicked(ActionEvent event) {
    // This method will be called when the login button is clicked
    Login(event);
}
}
