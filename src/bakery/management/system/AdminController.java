/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/javafx/FXMLController.java to edit this template
 */
package bakery.management.system;

import java.io.IOException;
import java.net.URL;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.time.LocalDate;
import java.util.ResourceBundle;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
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
import javafx.scene.control.DatePicker;
import javafx.scene.control.RadioButton;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.ToggleGroup;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;

/**
 * FXML Controller class
 *
 * @author aqilh
 */
public class AdminController implements Initializable {

    @FXML
    private Button btnDelete;

    @FXML
    private Button btnReset;

    @FXML
    private Button btnSearch;

    @FXML
    private Button btnUpdate;
    
    @FXML
    private Button btnLogout;

    @FXML
    private TableColumn<Order, Integer> colID;

    @FXML
    private TableColumn<Order, String> colName;

    @FXML
    private TableColumn<Order, String> colEmail;

    @FXML
    private TableColumn<Order, String> colAddress;

    @FXML
    private TableColumn<Order, LocalDate> colDate;

    @FXML
    private TableColumn<Order, String> colItem;

    @FXML
    private TableColumn<Order, Double> colPrice;

    @FXML
    private TableColumn<Order, String> colStatus;
    
    @FXML
    private TableColumn<Order, String> colDeliveryOption;

    @FXML
    private RadioButton rbPickup;

    @FXML
    private RadioButton rbDelivery;

    @FXML
    private ToggleGroup deliveryOption;
    
    @FXML
    private RadioButton rbPickedup;

    @FXML
    private RadioButton rbDelivered;
    
    @FXML
    private RadioButton rbPreparing;
    
    @FXML
    private RadioButton rbDelivering;
    
    @FXML
    private RadioButton rbPreparedForPickup;

    @FXML
    private ToggleGroup statusOption;

    @FXML
    private DatePicker dpDate;

    @FXML
    private TextField searchField;

    @FXML
    private TextField tfAddress;

    @FXML
    private TextField tfEmail;

    @FXML
    private TextField tfID;

    @FXML
    private TextField tfItem;

    @FXML
    private TextField tfName;

    @FXML
    private TextField tfPrice;

    /*@FXML
    private TextField tfStatus;*/

    @FXML
    private TableView<Order> tvOrder;
    
    @FXML
    private void handleButtonAction(ActionEvent event) {
        if (event.getSource() == btnReset) {
            resetOrder();
        } else if (event.getSource() == btnUpdate) {
            updateOrder();
        } else if (event.getSource() == btnDelete) {
            deleteOrder();
        } else if (event.getSource () == btnSearch) {
            searchOrder ();
        } else if (event.getSource () == btnLogout) {
            logoutOrder ();
        }
    }
    
    @FXML
    void handleMouseAction(MouseEvent event) {
        Order orders = tvOrder.getSelectionModel().getSelectedItem();
        tfID.setText(String.valueOf(orders.getId()));
        tfName.setText(orders.getName());
        tfEmail.setText(orders.getEmail());
        tfAddress.setText(orders.getAddress());
        dpDate.setValue(orders.getDate()); // Assuming dpDate is a DatePicker
        tfItem.setText(orders.getItem());
        tfPrice.setText(String.valueOf(orders.getPrice()));

        // Set the delivery option based on the selected order
        if (orders.getDeliveryOption().equals("Pickup")) {
            rbPickup.setSelected(true);
        } else if (orders.getDeliveryOption().equals("Delivery")) {
            rbDelivery.setSelected(true);
        }

        // Set the status option based on the selected order
        if (orders.getStatus().equals("Pickedup")) {
            rbPickedup.setSelected(true);
        } else if (orders.getStatus().equals("Delivered")) {
            rbDelivered.setSelected(true);
        } else if (orders.getStatus().equals("Preparing")) {
            rbPreparing.setSelected(true);
        } else if (orders.getStatus().equals("Delivering")) {
            rbDelivering.setSelected(true);
        } else if (orders.getStatus().equals("Ready For Delivery/Pickup")) {
            rbPreparedForPickup.setSelected(true);
        }
    }

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO
        showOrder();
    }

    private void executeQuery(String query) {
        try (Connection conn = getConnection();
             Statement st = conn.createStatement()) {
            st.executeUpdate(query);
        } catch (SQLException ex) {
            System.out.println("Error in executing the query: " + ex.getMessage());
        }
    }

    private void updateOrder() {
        LocalDate selectedDate = dpDate.getValue();
        String dateString = selectedDate.toString();
        String deliveryOption = getSelectedDeliveryOption();
        String statusOption = getSelectedStatusOption();

        String query = "UPDATE `orders` SET name = '" + tfName.getText() + "', email = '" + tfEmail.getText()
                + "', address = '" + tfAddress.getText() + "', date = '" + dateString + "', item = '" + tfItem.getText() + "', price = " + tfPrice.getText() + ", status = '" + statusOption + "', deliveryOption = '" + deliveryOption + "' WHERE id = " + tfID.getText();

        // Show a confirmation dialog before updating the order
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Confirm Update");
        alert.setHeaderText("Confirm Order Update");
        alert.setContentText("Are you sure you want to update this order?");

        alert.showAndWait().ifPresent(buttonType -> {
            if (buttonType == ButtonType.OK) {
                // Perform the update
                try (Connection conn = getConnection();
                     Statement st = conn.createStatement()) {
                    st.executeUpdate(query);
                    System.out.println("Order updated successfully.");
                } catch (SQLException ex) {
                    System.out.println("Error updating order: " + ex.getMessage());
                }

                showOrder(); // Refresh the TableView after updating the record
            }
        });
    }

    private void deleteOrder() {
        String query = "DELETE FROM `orders` WHERE id = " + tfID.getText();
        // Show a warning dialog before deleting the order
        Alert alert = new Alert(Alert.AlertType.WARNING);
        alert.setTitle("Confirm Delete");
        alert.setHeaderText("Warning: Delete Order");
        alert.setContentText("Are you sure you want to delete this order?");

        alert.showAndWait().ifPresent(buttonType -> {
            if (buttonType == ButtonType.OK) {
                executeQuery(query);
                showOrder();
            }
        });
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
        } else if (rbDelivering.isSelected()) {
            return "Delivering";
        } else if (rbPreparedForPickup.isSelected()) {
            return "Ready For Delivery/Pickup";
        } else {
            return null; // or throw an exception if no option is selected
        }
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

    

    private void searchOrder() {

        ObservableList<Order> filteredList = FXCollections.observableArrayList();
        String searchText = searchField.getText().trim().toLowerCase();

        if (searchText.isEmpty()) {
            // If search text is empty, show all records
            showOrder();
            return;
        }

        ObservableList<Order> ordersList = getOrderList();

        // Loop through each record and check if it matches the search criteria
        for (Order orders : ordersList) {
            if (orders.getName().toLowerCase().contains(searchText) ||
                    orders.getEmail().toLowerCase().contains(searchText) ||
                    orders.getAddress().toLowerCase().contains(searchText) ||
                    orders.getDeliveryOption().toLowerCase().contains(searchText) ||
                    orders.getItem().toLowerCase().contains(searchText) ||
                    orders.getStatus().toLowerCase().contains(searchText)) {
                // Add the record to the filtered list if it matches the search criteria
                filteredList.add(orders);
            }
        }

        // Update the TableView with the filtered list
        tvOrder.setItems(filteredList);
    }

    private void resetOrder() {

        // Clear all text fields
        tfID.clear();
        tfName.clear();
        tfEmail.clear();
        tfAddress.clear();
        dpDate.setValue(null);
        tfItem.clear();
        tfPrice.clear();
        //tfStatus.clear();
        searchField.clear();

        // Clear the search field as well
        searchField.clear();

        // Reload all records
        showOrder();

    }

    public Connection getConnection() {
        try {
            return DriverManager.getConnection("jdbc:mysql://localhost:3306/bakery", "root", "");
        } catch (SQLException ex) {
            System.out.println("Error: " + ex.getMessage());
            return null;
        }
    }

    public ObservableList<Order> getOrderList() {
        ObservableList<Order> ordersList = FXCollections.observableArrayList();
        String query = "SELECT * FROM `orders`";
        try (Connection conn = getConnection();
             Statement st = conn.createStatement();
             ResultSet rs = st.executeQuery(query)) {
            while (rs.next()) {
                int id = rs.getInt("id");
                String name = rs.getString("name");
                String email = rs.getString("email");
                String address = rs.getString("address");
                LocalDate date = rs.getDate("date").toLocalDate();
                String item = rs.getString("item");
                double price = rs.getDouble("price");
                String status = rs.getString("status");
                String deliveryOption = rs.getString("deliveryOption");

                Order orders = new Order(id, name, email, address, date, item, price, status, deliveryOption);
                ordersList.add(orders);
            }
        } catch (SQLException ex) {
            System.out.println("Error: " + ex.getMessage());
        }
        return ordersList;
    }

    public void showOrder() {
        ObservableList<Order> ordersList = getOrderList();
        colID.setCellValueFactory(new PropertyValueFactory<>("id"));
        colName.setCellValueFactory(new PropertyValueFactory<>("name"));
        colEmail.setCellValueFactory(new PropertyValueFactory<>("email"));
        colAddress.setCellValueFactory(new PropertyValueFactory<>("address"));
        colDate.setCellValueFactory(new PropertyValueFactory<>("date"));
        colItem.setCellValueFactory(new PropertyValueFactory<>("item"));
        colPrice.setCellValueFactory(new PropertyValueFactory<>("price"));
        colStatus.setCellValueFactory(new PropertyValueFactory<>("status"));
        colDeliveryOption.setCellValueFactory(new PropertyValueFactory<>("deliveryOption"));
        tvOrder.setItems(ordersList);
    }
}
