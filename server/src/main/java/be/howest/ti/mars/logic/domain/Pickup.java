package be.howest.ti.mars.logic.domain;

import java.time.LocalDate;

public class Pickup {
    private int id;
    private Customer customer;
    private String dateOfRequest;
    private String requestedPickupDate;

    private Status pickupStatus;


    public Pickup(Customer customer, String requestedPickupDate) {
        this.customer = customer;
        this.requestedPickupDate = requestedPickupDate;
        this.dateOfRequest = LocalDate.now().toString();
        this.pickupStatus = Status.PENDING;
    }
    public Pickup(int id, Customer customer, String dateOfRequest, String requestedPickupDate, Status pickupStatus) {
        this(customer, dateOfRequest);
        this.id = id;
        this.customer = customer;
        this.dateOfRequest = dateOfRequest;
        this.requestedPickupDate = requestedPickupDate;
        this.pickupStatus = pickupStatus;
    }

    public int getId() {
        return id;
    }

    public Customer getCustomer() {
        return customer;
    }

    public String getDateOfRequest() {
        return dateOfRequest;
    }

    public String getRequestedPickupDate() {
        return requestedPickupDate;
    }

    public Status getPickupStatus() {
        return pickupStatus;
    }

    public void setId(int id) {
        this.id = id;
    }
}
