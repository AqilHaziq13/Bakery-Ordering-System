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
import javafx.beans.property.StringProperty;
import javafx.beans.property.SimpleStringProperty;

public class CustomerController implements Initializable {
    private StringProperty customerEmail = new SimpleStringProperty();

    @FXML
    private Button btnCheeseCakeOrder;

    @FXML
    private Button btnChocolateCakeOrder;
    
    @FXML
    private Button btnBrowniesOrder;

    @FXML
    private Button btnMuffinsOrder;

    @FXML
    private Button btnCroissantOrder;

    @FXML
    private Button btnGarlicBreadOrder;

    @FXML
    private Button btnKekBatikOrder;

    @FXML
    private Button btnLogout;
    
    @FXML
    private Button btnNextPage;
    
    @FXML
    private Button btnPrevPage;

    @FXML
    private Button btnRedVelvetOrder;
    
    //@FXML
    //private String customerEmail;
    
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
    
    @FXML
    void handleNextPage(ActionEvent event) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("Customer2.fxml"));
            Parent root = loader.load();
            
            // Get the controller instance and set the customer email
            Customer2Controller controller = loader.getController();
            controller.customerEmailProperty().set(getCustomerEmail());

            // Get the current scene and replace it with the next page
            Scene scene = ((Node)event.getSource()).getScene();
            scene.setRoot(root);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    
    @FXML
    void handlePrevPage(ActionEvent event) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("CustomerMenu.fxml"));
            Parent root = loader.load();
            
            // Get the controller instance and set the customer email
            CustomerMenuController controller = loader.getController();
            controller.customerEmailProperty().set(getCustomerEmail());

            // Get the current scene and replace it with the previous page
            Scene scene = ((Node)event.getSource()).getScene();
            scene.setRoot(root);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        
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

    // Method to handle opening the OrderDetails.fxml
    private void openOrderDetails(String itemName, String itemPrice) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("OrderDetails.fxml"));
            Parent root = loader.load();

            // Pass item name and price to the controller of OrderDetails.fxml
            OrderDetailsController controller = loader.getController();
            controller.setItemAndPrice(itemName, itemPrice);
            // Get the controller instance and set the customer email, name, and address
            controller.customerEmailProperty().set(getCustomerEmail());
            controller.setCustomerName(getCustomerName(getCustomerEmail()));
            controller.setCustomerAddress(getCustomerAddress(getCustomerEmail()));
            controller.setCustomerEmail(getCustomerEmail());

            Stage stage = new Stage();
            stage.setScene(new Scene(root));
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    // Method to handle the "Order" button action for Croissant
    @FXML
    private void orderCroissant(ActionEvent event) {
        openOrderDetails("Croissant", "3.90");
    }
    
    @FXML
    private void orderChocolateCake(ActionEvent event) {
        openOrderDetails("Chocolate Cake", "6.90");
    }
    
    @FXML
    private void orderBrownies(ActionEvent event) {
        openOrderDetails("Brownies", "10.90");
    }
    
    @FXML
    private void orderMuffins(ActionEvent event) {
        openOrderDetails("Muffins", "3.90");
    }
    
    @FXML
    private void orderGarlicBread(ActionEvent event) {
        openOrderDetails("Garlic Bread", "5.90");
    }
    
    @FXML
    private void orderCheeseCake(ActionEvent event) {
        openOrderDetails("Cheesecake", "6.90");
    }
    
    @FXML
    private void orderRedVelvet(ActionEvent event) {
        openOrderDetails("Red Velvet", "6.90");
    }
    
    @FXML
    private void orderKekBatik(ActionEvent event) {
        openOrderDetails("Kek Batik", "10.90");
    }
    
    private String getCustomerName(String email) {
    String name = null;
    try (Connection conn = getConnection();
         PreparedStatement stmt = conn.prepareStatement("SELECT name FROM customer WHERE email = ?")) {
        stmt.setString(1, email);
        ResultSet rs = stmt.executeQuery();
        if (rs.next()) {
            name = rs.getString("name");
        }
    } catch (SQLException e) {
        e.printStackTrace();
    }
    return name;
}

private String getCustomerAddress(String email) {
    String address = null;
    try (Connection conn = getConnection();
         PreparedStatement stmt = conn.prepareStatement("SELECT address FROM customer WHERE email = ?")) {
        stmt.setString(1, email);
        ResultSet rs = stmt.executeQuery();
        if (rs.next()) {
            address = rs.getString("address");
        }
    } catch (SQLException e) {
        e.printStackTrace();
    }
    return address;
}

private Connection getConnection() {
    try {
        return DriverManager.getConnection("jdbc:mysql://localhost:3306/bakery", "root", "");
    } catch (SQLException ex) {
        System.out.println("Error: " + ex.getMessage());
        return null;
    }
}
}
