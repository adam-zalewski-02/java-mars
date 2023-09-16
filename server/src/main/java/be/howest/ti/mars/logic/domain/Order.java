package be.howest.ti.mars.logic.domain;

import java.security.SecureRandom;
import java.time.LocalDate;
import java.util.Random;

public class Order {
    private int id;
    private final Customer customer;
    private final Product product;

    private final int amountInKg;

    private String trackNumber;

    private Status orderStatus;

    private String date;

    private final Random rand = new SecureRandom();

    public Order(Customer customer, Product product, int amountInKg) {
        this.customer = customer;
        this.product = product;
        this.amountInKg = amountInKg;
        this.trackNumber = generateTrackingNumber();
        this.orderStatus = Status.PENDING;
        this.date = LocalDate.now().toString();
    }

    public Order(int id, Customer customer, Product product, int amountInKg, String trackNumber, Status orderStatus, String date) {
        this(customer, product, amountInKg);
        this.id = id;
        this.trackNumber = trackNumber;
        this.orderStatus = orderStatus;
        this.date = date;
    }

    public int getId() {
        return id;
    }

    public Customer getCustomer() {
        return customer;
    }

    public Product getProduct() {
        return product;
    }

    public int getAmountInKg() {
        return amountInKg;
    }

    public String getTrackNumber() {
        return trackNumber;
    }

    public Status getOrderStatus() {
        return orderStatus;
    }

    public String getDate() {
        return date;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String generateTrackingNumber() {
        String[] numbers = {"0", "1", "2", "3", "4", "5", "6", "7", "8", "9"};
        String[] letters = {"A", "B", "C", "D", "E", "F", "G", "H", "I", "J", "K", "L", "M", "N", "O", "P", "Q", "R", "S", "T", "U", "V", "W", "X", "Y", "Z"};
        StringBuilder output = new StringBuilder();
        for (int i = 0; i < 2; i++) {
            output.append(letters[rand.nextInt(letters.length)]);
        }
        for (int i = 0; i <= numbers.length - 1; i++) {
            output.append(numbers[rand.nextInt(numbers.length)]);
        }
        for (int i = 0; i < 2; i++) {
            output.append(letters[rand.nextInt(letters.length)]);
        }
        return output.toString();
    }

}
