package be.howest.ti.mars.logic.controller;

import be.howest.ti.mars.logic.data.Repositories;
import be.howest.ti.mars.logic.domain.*;
import org.apache.commons.lang3.StringUtils;

import java.util.Map;
import java.util.NoSuchElementException;

/**
 * DefaultMarsController is the default implementation for the MarsController interface.
 * The controller shouldn't even know that it is used in an API context..
 *
 * This class and all other classes in the logic-package (or future sub-packages)
 * should use 100% plain old Java Objects (POJOs). The use of Json, JsonObject or
 * Strings that contain encoded/json data should be avoided here.
 * Keep libraries and frameworks out of the logic packages as much as possible.
 * Do not be afraid to create your own Java classes if needed.
 */
public class DefaultMarsController implements MarsController {
    private static final String MSG_QUOTE_ID_UNKNOWN = "No quote with id: %d";

    @Override
    public Quote getQuote(int quoteId) {
        Quote quote = Repositories.getH2Repo().getQuote(quoteId);
        if (null == quote)
            throw new NoSuchElementException(String.format(MSG_QUOTE_ID_UNKNOWN, quoteId));

        return quote;
    }

    @Override
    public Quote createQuote(String quote) {
        if (StringUtils.isBlank(quote))
            throw new IllegalArgumentException("An empty quote is not allowed.");

        return Repositories.getH2Repo().insertQuote(quote);
    }

    @Override
    public Quote updateQuote(int quoteId, String quote) {
        if (StringUtils.isBlank(quote))
            throw new IllegalArgumentException("No quote provided!");

        if (quoteId < 0)
            throw new IllegalArgumentException("No valid quote ID provided");

        if (null == Repositories.getH2Repo().getQuote(quoteId))
            throw new NoSuchElementException(String.format(MSG_QUOTE_ID_UNKNOWN, quoteId));

        return Repositories.getH2Repo().updateQuote(quoteId, quote);
    }

    @Override
    public void deleteQuote(int quoteId) {
        if (null == Repositories.getH2Repo().getQuote(quoteId))
            throw new NoSuchElementException(String.format(MSG_QUOTE_ID_UNKNOWN, quoteId));

        Repositories.getH2Repo().deleteQuote(quoteId);
    }

    @Override
    public Order getOrder(int orderId) {
        return Repositories.getH2Repo().getOrder(orderId);
    }

    @Override
    public Map<Integer, Order> getOrders() {
        return Repositories.getH2Repo().getOrders();
    }

    @Override
    public Order insertOrder(int productId, int customerId, int amountInKg) {
        return Repositories.getH2Repo().insertOrder(productId, customerId, amountInKg);
    }

    @Override
    public Order updateOrder(int orderId, Status orderStatus) {
        return Repositories.getH2Repo().updateOrderStatus(orderId, orderStatus);
    }

    @Override
    public Product getProduct(int productId) {
        return Repositories.getH2Repo().getProduct(productId);
    }

    @Override
    public Map<Integer,Product> getProducts() {
        return Repositories.getH2Repo().getProducts();
    }

    @Override
    public Product createProduct(String name, double pricePerKg, double amountInStockInKg) {
        return Repositories.getH2Repo().insertProduct(name, pricePerKg, amountInStockInKg);
    }

    @Override
    public Product updateProduct(int productId, String name, double pricePerKg, double amountInStockInKg) {
        return Repositories.getH2Repo().updateProduct(productId, name, pricePerKg, amountInStockInKg);
    }

    @Override
    public void substractStock(int productId, double amount) {
        Repositories.getH2Repo().substractStock(productId,amount);
    }

    @Override
    public Tier getTier(int id) {
        return Repositories.getH2Repo().getTier(id);
    }

    @Override
    public Map<Integer, Tier> getTiers() {
        return Repositories.getH2Repo().getTiers();
    }

    @Override
    public Tier createTier(String name, int monthlyLimitInKg, double additionalDisposalPerKg, int freePickUps, double pickupFeePerKg, double paymentPerMonth) {
        return Repositories.getH2Repo().insertTier(name, monthlyLimitInKg, additionalDisposalPerKg, freePickUps, pickupFeePerKg, paymentPerMonth);
    }

    @Override
    public Tier updateTier(int tierId, String name, int monthlyLimitInKg, double additionalDisposalPerKg, int freePickUps, double pickupFeePerKg, double paymentPerMonth) {
        return Repositories.getH2Repo().updateTier(tierId, name, monthlyLimitInKg, additionalDisposalPerKg, freePickUps, pickupFeePerKg, paymentPerMonth);
    }

    @Override
    public Customer getCustomer(int customerId) {
        return Repositories.getH2Repo().getCustomer(customerId);
    }

    @Override
    public Customer getCustomer(String customerName) {
        return Repositories.getH2Repo().getCustomer(customerName);
    }

    @Override
    public Map<Integer, Customer> getCustomers() {
        return Repositories.getH2Repo().getCustomers();
    }

    @Override
    public Customer createCustomer(String name, String address) {
        return Repositories.getH2Repo().insertCustomer(name, address);
    }

    @Override
    public Customer updateCustomer(int id, String name, String address, int tierId) {
        return Repositories.getH2Repo().updateCustomer(id, name, address, tierId);
    }

    @Override
    public Customer updateDisposedGarbage(int customerId, Double amountOfGarbage) {
        return Repositories.getH2Repo().updateDisposedGarbage(customerId, amountOfGarbage);
    }

    @Override
    public void updatePickupsLeft(int customerid) {
        Repositories.getH2Repo().updatePickupsLeft(customerid);
    }

    @Override
    public Pickup getPickup(int id) {
        return Repositories.getH2Repo().getPickup(id);
    }


    @Override
    public Map<Integer, Pickup> getPickups() {
        return Repositories.getH2Repo().getPickups();
    }

    @Override
    public Map<Integer, Pickup> getPickups(int customerId) {
        return Repositories.getH2Repo().getPickups(customerId);
    }

    @Override
    public Map<Integer, Pickup> getPickups(String customerName) {
        return Repositories.getH2Repo().getPickups(customerName);
    }

    @Override
    public Pickup createPickup(int customerId, String requestedPickupDate) {
        return Repositories.getH2Repo().insertPickup(customerId, requestedPickupDate);
    }

    @Override
    public Pickup updatePickup(int id, Status status) {
        return  Repositories.getH2Repo().updatePickupStatus(id, status);
    }

}