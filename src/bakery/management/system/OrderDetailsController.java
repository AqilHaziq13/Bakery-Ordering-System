/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/javafx/FXMLController.java to edit this template
 */
package bakery.management.system;

import java.net.URL;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ResourceBundle;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.DatePicker;
import javafx.scene.control.RadioButton;
import javafx.scene.control.TextField;
import javafx.scene.control.ToggleGroup;

/**
 * FXML Controller class
 *
 * @author aqilh
 */
public class OrderDetailsController implements Initializable {
    private StringProperty customerEmail = new SimpleStringProperty();

    @FXML
    private DatePicker dpDate;

    @FXML
    private TextField tfAddress;

    @FXML
    private TextField tfEmail;

    @FXML
    private TextField tfItem;

    @FXML
    private TextField tfName;

    @FXML
    private TextField tfPrice;

    @FXML
    private RadioButton rbPickedup;

    @FXML
    private RadioButton rbDelivered;
    
    @FXML
    private RadioButton rbPreparing;

    @FXML
    private ToggleGroup statusOption;
    
    @FXML
    private Button btnOrder;
    
    @FXML
    private RadioButton rbPickup;

    @FXML
    private RadioButton rbDelivery;

    @FXML
    private ToggleGroup deliveryOption;

    private String itemName;
    private String itemPrice;
    
    private String customerName;
    private String customerAddress;

    @FXML
    private void handleButtonAction(ActionEvent event) {
        addOrder();
    }

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // Initialize status field with "Pickedup"
        rbPreparing.setSelected(true);
        
    }    

    private void executeQuery(String query) {
        try (Connection conn = getConnection();
             Statement st = conn.createStatement()) {
            st.executeUpdate(query);
        } catch (SQLException ex) {
            System.out.println("Error in executing the query: " + ex.getMessage());
        }
    }
    

    private void addOrder() {
    // Check if any of the required fields are blank
    if (tfName.getText().isEmpty() || tfEmail.getText().isEmpty() || tfAddress.getText().isEmpty() || dpDate.getValue() == null || tfItem.getText().isEmpty() || tfPrice.getText().isEmpty()) {
        showAlert("Error", "Please fill in all the required fields.");
        return; // Exit the method if any field is blank
    }

    String deliveryOption = getSelectedDeliveryOption();
    String statusOption = getSelectedStatusOption();

    // Show confirmation dialog
    Alert confirmation = new Alert(AlertType.CONFIRMATION);
    confirmation.setTitle("Confirm Order");
    confirmation.setHeaderText(null);
    confirmation.setContentText("Please confirm your order:\n\nName: " + tfName.getText() + "\nEmail: " + tfEmail.getText() + "\nAddress: " + tfAddress.getText() + "\nDate: " + dpDate.getValue().toString() + "\nItem: " + tfItem.getText() + "\nPrice: " + tfPrice.getText() + "\nDelivery Option: " + deliveryOption + "\n\nNote: Once you place the order, you cannot cancel or adjust it.");

    if (confirmation.showAndWait().get() == javafx.scene.control.ButtonType.OK) {
        String query = "INSERT INTO orders (name, email, address, date, item, price, status, deliveryOption) VALUES ('" + tfName.getText() + "','" + tfEmail.getText() + "','" + tfAddress.getText() + "','" + dpDate.getValue() + "','" + tfItem.getText() + "','" + tfPrice.getText() + "','" + statusOption + "','" + deliveryOption + "')";

        executeQuery(query);

        // Show pop-up message
        showAlert("Order Placed", "Your order has been successfully placed.");

        // Close the current window
        ((javafx.stage.Stage)(btnOrder.getScene().getWindow())).close();
    }
}
    
    public String getCustomerEmail() {
        return customerEmail.get();
    }

    public void setCustomerEmail(String email) {
    customerEmail.set(email);
    tfEmail.setText(email);
}

    public StringProperty customerEmailProperty() {
        return customerEmail;
    }

private void showAlert(String title, String message) {
    Alert alert = new Alert(AlertType.INFORMATION);
    alert.setTitle(title);
    alert.setHeaderText(null);
    alert.setContentText(message);
    alert.showAndWait();
}


    public Connection getConnection() {
        try {
            return DriverManager.getConnection("jdbc:mysql://localhost:3306/bakery", "root", "");
        } catch (SQLException ex) {
            System.out.println("Error: " + ex.getMessage());
            return null;
        }
    }
    
    // Method to set item and price from CustomerController
    public void setItemAndPrice(String itemName, String itemPrice) {
        tfItem.setText(itemName);
        tfPrice.setText(itemPrice);
    }
    
    private String getSelectedDeliveryOption() {
        if (rbPickup.isSelected()) {
            return "Pickup";
        } else if (rbDelivery.isSelected()) {
            return "Delivery";
        } else {
            return null; // or throw an exception if no option is selected
        }
    }
    
    private String getSelectedStatusOption() {
        if (rbPickedup.isSelected()) {
            return "Pickedup";
        } else if (rbDelivered.isSelected()) {
            return "Delivered";
        } else if (rbPreparing.isSelected()) {
            return "Preparing";
        } else {
            return null; // or throw an exception if no option is selected
        }
    }
    
    public void setCustomerName(String name) {
    this.customerName = name;
    tfName.setText(name);
}

    public void setCustomerAddress(String address) {
        this.customerAddress = address;
        tfAddress.setText(address);
    }

}
