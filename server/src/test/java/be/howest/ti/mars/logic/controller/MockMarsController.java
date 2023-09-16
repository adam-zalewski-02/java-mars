package be.howest.ti.mars.logic.controller;

import be.howest.ti.mars.logic.domain.*;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;

import java.util.HashMap;
import java.util.Map;

public abstract class MockMarsController implements MarsController {
    private static final String SOME_QUOTE = "quote";
    private static final int SOME_ID = 1;
    private static final int SOME_PRODUCTID = 1;
    private static final int SOME_CUSTOMERID = 1;
    private static final String SOME_TRACKNUMBER = "LV0987654321BE";
    private static final Status SOME_ORDERSTATUS = Status.PENDING;
    private static final String SOME_DATE = "2022-11-24";

    private static final String SOME_PRODUCT_NAME = "Iron (Fe)";

    private static final double SOME_PRODUCT_PRICE = 2.50;

    private static final int SOME_PRODUCT_AMOUNT_IN_STOCK = 1500;
    @Override
    public Quote getQuote(int quoteId) {
        return new Quote(quoteId, SOME_QUOTE);
    }

    @Override
    public Quote createQuote(String quote) {
        return new Quote(1, quote);
    }

    @Override
    public Quote updateQuote(int quoteId, String quote) {
        return new Quote(quoteId, quote);
    }

    @Override
    public void deleteQuote(int quoteId) {
    }

    @Override
    public Order getOrder(int id) {
        return new Order(id, getCustomer(SOME_CUSTOMERID), getProduct(SOME_PRODUCTID), 150, SOME_TRACKNUMBER, SOME_ORDERSTATUS, SOME_DATE);
    }

    @Override
    public Order insertOrder(int productId, int customerId, int amount) {
        return new Order(getCustomer(customerId), getProduct(productId), amount);
    }

    @Override
    public Map<Integer, Order> getOrders() {
        Map<Integer, Order> orders = new HashMap<>();
        orders.put(SOME_ID, new Order(SOME_ID, getCustomer(SOME_CUSTOMERID), getProduct(SOME_PRODUCTID), 150, SOME_TRACKNUMBER, SOME_ORDERSTATUS, SOME_DATE));
        return orders;
    }

    @Override
    public Order updateOrder(int id, Status orderStatus) {
        return new Order(id, getCustomer(SOME_CUSTOMERID), getProduct(SOME_PRODUCTID), 150, SOME_TRACKNUMBER, orderStatus, SOME_DATE);
    }

    @Override
    public Product getProduct(int productId) {
        return new Product(productId, SOME_PRODUCT_NAME, SOME_PRODUCT_PRICE, SOME_PRODUCT_AMOUNT_IN_STOCK);
    }

    @Override
    public Map<Integer, Product> getProducts() {
        Map<Integer, Product> productMap = new HashMap<>();
        productMap.put(SOME_PRODUCTID,new Product(SOME_PRODUCTID, SOME_PRODUCT_NAME, SOME_PRODUCT_PRICE, SOME_PRODUCT_AMOUNT_IN_STOCK));
        return productMap;
    }

    @Override
    public Product createProduct(String name, double price, double amountInStock) {
        return new Product(name,price,amountInStock);
    }

    @Override
    public Product updateProduct(int productId, String name, double price, double amountInStock) {
        return new Product(productId, name, price, amountInStock);
    }

    @Override
    public void substractStock(int productId, double amount) {
    }

    @Override
    public Tier getTier(int id) {
        return new Tier(1, "Basic", 1, 1, 1, 1, 1);
    }

    @Override
    public Map<Integer, Tier> getTiers() {
        Map<Integer, Tier> tierMap = new HashMap<>();
        tierMap.put(1, new Tier(1, "Basic", 1, 1, 1, 1, 1));
        return tierMap;
    }

    @Override
    public Tier createTier(String name, int monthlyLimitInKg, double additionalDisposalPerKg, int freePickUps, double pickupFeePerKg, double paymentPerMonth) {
        return new Tier(1, name, monthlyLimitInKg, additionalDisposalPerKg, freePickUps, pickupFeePerKg, paymentPerMonth);
    }

    @Override
    public Tier updateTier(int tierId, String name, int monthlyLimitInKg, double additionalDisposalPerKg, int freePickUps, double pickupFeePerKg, double paymentPerMonth) {
        return new Tier(tierId, name, monthlyLimitInKg, additionalDisposalPerKg, freePickUps, pickupFeePerKg, paymentPerMonth);
    }

    @Override
    public Customer getCustomer(int id) {
        return new Customer(1, "Adam", "Straat", getTier(1), 0, getTier(1).getMonthlyLimitInkg(), getTier(1).getFreePickUps());
    }

    @Override
    public Customer getCustomer(String customerName) {
        return new Customer(1, "Adam", "Straat", getTier(1), 0, getTier(1).getMonthlyLimitInkg(), getTier(1).getFreePickUps());
    }

    @Override
    public Map<Integer, Customer> getCustomers() {
        return new HashMap<>();
    }

    @Override
    public Customer createCustomer(String name, String address) {
        return new Customer(1, name, address, getTier(1), 0, 0, 0);
    }

    @Override
    public Customer updateCustomer(int id, String name, String address, int tierId) {
        return new Customer(id, name, address, getTier(tierId), 0, getTier(1).getMonthlyLimitInkg(), getTier(1).getFreePickUps());
    }

    @Override
    public Customer updateDisposedGarbage(int customerId, Double amountOfGarbage) {
        return getCustomer(customerId);
    }

    @Override
    public void updatePickupsLeft(int customerid) {
    }

    @Override
    public Pickup getPickup(int id) {
        return new Pickup(id, getCustomer(1), SOME_DATE, SOME_DATE, SOME_ORDERSTATUS);
    }

    @Override
    public Map<Integer, Pickup> getPickups() {
        Map<Integer, Pickup> pickupMap = new HashMap<>();
        pickupMap.put(1, new Pickup(1, getCustomer(1), SOME_DATE, SOME_DATE, SOME_ORDERSTATUS));
        return pickupMap;
    }

    @Override
    public Map<Integer, Pickup> getPickups(int customerId) {
        Map<Integer, Pickup> pickupMap = new HashMap<>();
        pickupMap.put(1, new Pickup(1, getCustomer(customerId), SOME_DATE, SOME_DATE, SOME_ORDERSTATUS));
        return pickupMap;
    }

    @Override
    public Map<Integer, Pickup> getPickups(String customerName) {
        Map<Integer, Pickup> pickupMap = new HashMap<>();
        pickupMap.put(1, new Pickup(1, getCustomer(1), SOME_DATE, SOME_DATE, SOME_ORDERSTATUS));
        return pickupMap;
    }

    @Override
    public Pickup createPickup(int customerId, String requestedPickupDate) {
        return new Pickup( getCustomer(customerId), requestedPickupDate);
    }

    @Override
    public Pickup updatePickup(int id, Status status) {
        return new Pickup(id, getCustomer(1), SOME_DATE, SOME_DATE, status);
    }
}
