/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package bakery.management.system;

import java.time.LocalDate;

/**
 *
 * @author aqilh
 */
public class Order {
    
    private int id;
    private String name;
    private String email;
    private String address;
    private LocalDate date;
    private String item;
    private double price;
    private String status;
    private String deliveryOption;

    
    public Order(int id, String name, String email, String address, LocalDate date, String item, double price, String status, String deliveryOption) {
        this.id = id;
        this.name = name;
        this.email = email;
        this.address = address;
        this.date = date;
        this.item = item;
        this.price = price;
        this.status = status;
        this.deliveryOption = deliveryOption; // Assign the delivery option
    }
    
    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getEmail() {
        return email;
    }

    public String getAddress() {
        return address;
    }

    public LocalDate getDate() {
        return date;
    }

    public String getItem() {
        return item;
    }

    public double getPrice() {
        return price;
    }

    public String getStatus() {
        return status;
    }
    
    public String getDeliveryOption() {
        return deliveryOption; // Getter for the delivery option
    }
}
