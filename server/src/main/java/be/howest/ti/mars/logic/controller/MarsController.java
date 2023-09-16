package be.howest.ti.mars.logic.controller;

import be.howest.ti.mars.logic.domain.*;

import java.util.Map;

public interface MarsController {

    Quote getQuote(int quoteId);

    Quote createQuote(String quote);

    Quote updateQuote(int quoteId, String quote);

    void deleteQuote(int quoteId);

    Order getOrder(int orderId);
    Order insertOrder(int productId, int customerId, int amountInKg);

    Order updateOrder(int orderId, Status orderStatus);

    Map<Integer, Order> getOrders();

    Product getProduct(int productId);

    Map<Integer,Product> getProducts();

    Product createProduct(String name, double pricePerKg, double amountInStockInKg);

    Product updateProduct(int productId, String name, double pricePerKg, double amountInStockInKg);

    void substractStock(int productId, double amount);

    Tier getTier(int id);

    Map<Integer, Tier> getTiers();

    Tier createTier(String name, int monthlyLimitInKg, double additionalDisposalPerKg, int freePickUps, double pickupFeePerKg, double paymentPerMonth);

    Tier updateTier(int tierId, String name, int monthlyLimitInKg, double additionalDisposalPerKg, int freePickUps, double pickupFeePerKg, double paymentPerMonth);

    Customer getCustomer(int customerId);

    Customer getCustomer(String customerName);

    Map<Integer, Customer> getCustomers();

    Customer createCustomer(String name, String address);

    Customer updateCustomer(int id, String name, String address, int tierId);
    Customer updateDisposedGarbage(int customerId, Double amountOfGarbage);

    void updatePickupsLeft(int customerid);

    Pickup getPickup(int id);

    Map<Integer, Pickup> getPickups();

    Map<Integer, Pickup> getPickups(int customerId);

    Map<Integer, Pickup> getPickups(String customerName);

    Pickup createPickup(int customerId, String requestedPickupDate);

    Pickup updatePickup(int id, Status status);


}
