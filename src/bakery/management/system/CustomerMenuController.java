/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/javafx/FXMLController.java to edit this template
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
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.stage.Stage;

/**
 * FXML Controller class
 *
 * @author aqilh
 */
public class CustomerMenuController implements Initializable {
    private StringProperty customerEmail = new SimpleStringProperty();
    
    @FXML
    private Button btnPurchaseHistory;

    @FXML
    private Button btnOrder;
    
    @FXML
    private Button btnLogout;
    
    @FXML
    void handleButtonAction(ActionEvent event) {
        
        logoutOrder ();

    }
    
    public String getCustomerEmail() {
        return customerEmail.get();
    }

    public void setCustomerEmail(String email) {
        customerEmail.set(email);
    }

    public StringProperty customerEmailProperty() {
        return customerEmail;
    }

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO
    }    
    
    private void logoutOrder() {
    Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
    alert.setTitle("Confirmation");
    alert.setHeaderText("Confirm Logout");
    alert.setContentText("Are you sure you want to log out?");

    // Customizing the buttons in the confirmation dialog
    ButtonType confirmButtonType = new ButtonType("Yes");
    alert.getButtonTypes().setAll(confirmButtonType, ButtonType.CANCEL);

    // Handling the user's choice
    alert.showAndWait().ifPresent(buttonType -> {
        if (buttonType == confirmButtonType) {
            // User clicked "Yes", perform logout
            try {
                // Load the login screen (FXMLDocument.fxml)
                FXMLLoader loader = new FXMLLoader(getClass().getResource("FXMLDocument.fxml"));
                Parent root = loader.load();

                // Create a new scene with the login screen
                Scene scene = new Scene(root);

                // Get the current stage and set the scene to the login screen
                Stage stage = (Stage) btnLogout.getScene().getWindow();
                stage.setScene(scene);
                stage.show();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    });
}
    
    @FXML
    void handlePurchaseHistoryButtonAction(ActionEvent event) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("PurchaseHistory.fxml"));
            Parent root = loader.load();
            
            // Get the controller instance and set the customer email
            PurchaseHistoryController controller = loader.getController();
            controller.customerEmailProperty().set(getCustomerEmail());
            controller.setCustomerEmail(getCustomerEmail());
            controller.showOrder(getCustomerEmail());

            // Get the current scene and replace it with the next page
            Scene scene = ((Node)event.getSource()).getScene();
            scene.setRoot(root);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    
    private String getCustomerE(String email) {
    String email2 = null;
    try (Connection conn = getConnection();
         PreparedStatement stmt = conn.prepareStatement("SELECT email FROM orders WHERE email = ?")) {
        stmt.setString(1, email);
        ResultSet rs = stmt.executeQuery();
        if (rs.next()) {
            email2 = rs.getString("email");
        }
    } catch (SQLException e) {
        e.printStackTrace();
    }
    return email2;
}
    
    private Connection getConnection() {
    try {
        return DriverManager.getConnection("jdbc:mysql://localhost:3306/bakery", "root", "");
    } catch (SQLException ex) {
        System.out.println("Error: " + ex.getMessage());
        return null;
    }
}

    @FXML
    void handleOrderButtonAction(ActionEvent event) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("Customer.fxml"));
            Parent root = loader.load();
            
            // Get the controller instance and set the customer email
            CustomerController controller = loader.getController();
            controller.customerEmailProperty().set(getCustomerEmail());

            // Get the current scene and replace it with the next page
            Scene scene = ((Node)event.getSource()).getScene();
            scene.setRoot(root);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    
}
