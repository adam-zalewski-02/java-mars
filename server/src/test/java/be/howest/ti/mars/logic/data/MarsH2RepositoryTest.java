package be.howest.ti.mars.logic.data;

import be.howest.ti.mars.logic.domain.*;
import io.netty.util.internal.StringUtil;
import io.vertx.core.json.JsonObject;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Map;

class MarsH2RepositoryTest {
    private static final String URL = "jdbc:h2:./db-08";

    @BeforeEach
    void setupTestSuite() {
        Repositories.shutdown();
        JsonObject dbProperties = new JsonObject(Map.of("url",URL,
                "username", "",
                "password", "",
                "webconsole.port", 9000 ));
        Repositories.configure(dbProperties);
    }

    @Test
    void getQuote() {
        // Arrange
        int id = 1;

        // Act
        Quote quote = Repositories.getH2Repo().getQuote(id);

        // Assert
        Assertions.assertTrue(quote != null && !StringUtil.isNullOrEmpty(quote.getValue()));
    }

    @Test
    void updateQuote() {
        // Arrange
        int id = 1;
        String quoteValue = "some value";

        // Act
        Quote quote = Repositories.getH2Repo().updateQuote(id, quoteValue);

        // Assert
        Assertions.assertNotNull(quote);
        Assertions.assertEquals(quoteValue, quote.getValue());
    }

    @Test
    void insertQuote() {
        // Arrange
        String quoteValue = "some value";

        // Act
        Quote quote = Repositories.getH2Repo().insertQuote(quoteValue);

        // Assert
        Assertions.assertNotNull(quote);
        Assertions.assertEquals(quoteValue, quote.getValue());
    }

    @Test
    void deleteQuote() {
        // Arrange
        int id = 1;

        // Act
        Repositories.getH2Repo().deleteQuote(id);

        // Assert
        Assertions.assertNull(Repositories.getH2Repo().getQuote(id));
    }

    @Test
    void getOrder() {
        int id = 1;
        Order order = Repositories.getH2Repo().getOrder(id);
        Assertions.assertNotNull(order);
    }

    @Test
    void getOrders() {
        Map<Integer, Order> orders = Repositories.getH2Repo().getOrders();
        Assertions.assertNotNull(orders);
    }

    @Test
    void insertOrder() {
        int customerId = 1;
        int productId = 1;
        int amountInKg = 20;

        Order order = Repositories.getH2Repo().insertOrder(productId,customerId,amountInKg);
        Assertions.assertNotNull(order);
        Assertions.assertEquals(amountInKg, order.getAmountInKg());
    }

    @Test
    void updateOrder() {
        int orderId = 1;
        Status orderStatus = Status.SUCCES;
        Order order = Repositories.getH2Repo().updateOrderStatus(orderId, orderStatus);

        Assertions.assertNotNull(order);
        Assertions.assertEquals(orderStatus, order.getOrderStatus());
    }

    @Test
    void getProduct() {
        int productId = 1;
        Product product = Repositories.getH2Repo().getProduct(productId);
        Assertions.assertNotNull(product);
    }

    @Test
    void getProducts() {
        Map<Integer, Product> products = Repositories.getH2Repo().getProducts();
        Assertions.assertNotNull(products);
    }

    @Test
    void substractStock() {
        int productId = 1;
        double amount = 1500;
        Repositories.getH2Repo().substractStock(productId,amount);
        Product product = Repositories.getH2Repo().getProduct(productId);
        Assertions.assertEquals(0, product.getAmountInStockInKg());
    }

    @Test
    void insertProduct() {
        String name = "Iron";
        int amountInStock = 150;
        double pricePerKg = 2.50;
        Product product = Repositories.getH2Repo().insertProduct(name, pricePerKg, amountInStock);
        Assertions.assertNotNull(product);
        Assertions.assertEquals(amountInStock, product.getAmountInStockInKg());
    }

    @Test
    void updateProduct() {
        int id = 1;
        String name = "Iron";
        int amountInStock = 150;
        double pricePerKg = 2.50;
        Product product = Repositories.getH2Repo().updateProduct(id, name, pricePerKg, amountInStock);

        Assertions.assertNotNull(product);
        Assertions.assertEquals(name, product.getName());
    }

    @Test
    void getCustomerById() {
        int id = 1;
        Customer customer = Repositories.getH2Repo().getCustomer(id);
        Assertions.assertNotNull(customer);
    }

    @Test
    void getCustomerByName() {
        String name = "Adam";
        Customer customer = Repositories.getH2Repo().getCustomer(name);
        Assertions.assertNotNull(customer);
    }

    @Test
    void getCustomers() {
        Map<Integer, Customer> customers = Repositories.getH2Repo().getCustomers();
        Assertions.assertNotNull(customers);
    }

    @Test
    void insertCustomer() {
        String name = "Test";
        String address = "Address";
        Customer customer = Repositories.getH2Repo().insertCustomer(name, address);
        Assertions.assertNotNull(customer);
    }

    @Test
    void updateCustomer() {
        int id = 1;
        int tierId = 1;
        String name = "Name";
        String address = "Address";
        Customer customer = Repositories.getH2Repo().updateCustomer(id,name,address,tierId);

        Assertions.assertNotNull(customer);
        Assertions.assertEquals(name, customer.getName());
    }

    @Test
    void addGarbage() {
        int id = 1;
        double amount = 25;
        Customer customer = Repositories.getH2Repo().updateDisposedGarbage(id, amount);
        Assertions.assertNotNull(customer);
        Assertions.assertEquals(175, customer.getAmountLeftToDispose());
    }

    @Test
    void updatePickupsLeft() {
        int customerId = 2;
        String date = "2022-01-01";
        Repositories.getH2Repo().updatePickupsLeft(customerId);
        Pickup pickup = Repositories.getH2Repo().insertPickup(customerId, date);
        Customer customer = Repositories.getH2Repo().getCustomer(customerId);

        Assertions.assertNotNull(pickup);
        Assertions.assertNotNull(customer);
        Assertions.assertEquals(4, customer.getPickupsLeft());

    }

    @Test
    void getTier() {
        int id = 1;
        Tier tier = Repositories.getH2Repo().getTier(id);

        Assertions.assertNotNull(tier);
    }

    @Test
    void getTiers() {
        Map<Integer, Tier> tiers = Repositories.getH2Repo().getTiers();
        Assertions.assertNotNull(tiers);
    }

    @Test
    void createTier() {
        String name = "Racoon";
        int limit = 200;
        double additonalFee = 1.5;
        int freePickups = 0;
        double pickupFee = 2;
        double paymentPerMonth = 99;

        Tier tier = Repositories.getH2Repo().insertTier(name,limit,additonalFee,freePickups,pickupFee,paymentPerMonth);
        Assertions.assertNotNull(tier);
        Assertions.assertEquals(name, tier.getName());
        Assertions.assertEquals(limit, tier.getMonthlyLimitInkg());
        Assertions.assertEquals(additonalFee, tier.getAdditionalDisposalPerKg());
        Assertions.assertEquals(freePickups, tier.getFreePickUps());
        Assertions.assertEquals(pickupFee, tier.getPickupFeePerKg());
        Assertions.assertEquals(paymentPerMonth, tier.getPaymentPerMonth());
    }

    @Test
    void updateTier() {
        int tierId = 1;
        String name = "Racoon";
        int limit = 200;
        double additonalFee = 1.5;
        int freePickups = 0;
        double pickupFee = 2;
        double paymentPerMonth = 99;

        Tier tier = Repositories.getH2Repo().updateTier(tierId,name,limit,additonalFee,freePickups,pickupFee,paymentPerMonth);
        Assertions.assertNotNull(tier);
        Assertions.assertEquals(tierId, tier.getId());
        Assertions.assertEquals(name, tier.getName());
        Assertions.assertEquals(limit, tier.getMonthlyLimitInkg());
        Assertions.assertEquals(additonalFee, tier.getAdditionalDisposalPerKg());
        Assertions.assertEquals(freePickups, tier.getFreePickUps());
        Assertions.assertEquals(pickupFee, tier.getPickupFeePerKg());
        Assertions.assertEquals(paymentPerMonth, tier.getPaymentPerMonth());
    }

    @Test
    void getPickups() {
        Map<Integer, Pickup> pickupMap = Repositories.getH2Repo().getPickups();
        Assertions.assertNotNull(pickupMap);
    }

    @Test
    void getPickupsByCustomerId() {
        int customerId = 1;
        Map<Integer, Pickup> pickupMap = Repositories.getH2Repo().getPickups(customerId);
        Assertions.assertNotNull(pickupMap);
    }
    @Test
    void getPickupsByCustomerName() {
        String name = "Adam";
        Map<Integer, Pickup> pickupMap = Repositories.getH2Repo().getPickups(name);
        Assertions.assertNotNull(pickupMap);
    }

    @Test
    void getPickup() {
        int id = 1;
        Pickup pickup = Repositories.getH2Repo().getPickup(id);
        Assertions.assertNotNull(pickup);
    }

    @Test
    void insertPickup() {
        int customerId = 1;
        String requestedPickupDate = "2022-01-01";
        Pickup pickup = Repositories.getH2Repo().insertPickup(customerId, requestedPickupDate);
        Assertions.assertNotNull(pickup);
    }

    @Test
    void updatePickup() {
        int pickupId = 1;
        Status status = Status.SUCCES;
        Pickup pickup = Repositories.getH2Repo().updatePickupStatus(pickupId, status);
        Assertions.assertNotNull(pickup);
    }

}
