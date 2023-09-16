package be.howest.ti.mars.web;

import be.howest.ti.mars.logic.controller.MockMarsController;
import be.howest.ti.mars.logic.data.Repositories;
import be.howest.ti.mars.logic.domain.Customer;
import be.howest.ti.mars.web.bridge.MarsOpenApiBridge;
import be.howest.ti.mars.web.bridge.MarsRtcBridge;
import io.vertx.core.Vertx;
import io.vertx.core.json.Json;
import io.vertx.core.json.JsonObject;
import io.vertx.ext.web.client.WebClient;
import io.vertx.junit5.VertxExtension;
import io.vertx.junit5.VertxTestContext;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.platform.commons.util.StringUtils;

import java.util.logging.Logger;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

@ExtendWith(VertxExtension.class)
@SuppressWarnings({"PMD.JUnitTestsShouldIncludeAssert","PMD.AvoidDuplicateLiterals"})
/*
 * PMD.JUnitTestsShouldIncludeAssert: VertxExtension style asserts are marked as false positives.
 * PMD.AvoidDuplicateLiterals: Should all be part of the spec (e.g., urls and names of req/res body properties, ...)
 */
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class OpenAPITest {

    private static final int PORT = 8080;
    private static final String HOST = "localhost";
    public static final String MSG_200_EXPECTED = "If all goes right, we expect a 200 status";
    public static final String MSG_201_EXPECTED = "If a resource is successfully created.";
    public static final String MSG_204_EXPECTED = "If a resource is successfully deleted";
    private Vertx vertx;
    private WebClient webClient;

    @BeforeAll
    void deploy(final VertxTestContext testContext) {
        Repositories.shutdown();
        vertx = Vertx.vertx();

        WebServer webServer = new WebServer(new MarsOpenApiBridge(new MockMarsController() {
        }), new MarsRtcBridge());
        vertx.deployVerticle(
                webServer,
                testContext.succeedingThenComplete()
        );
        webClient = WebClient.create(vertx);
    }

    @AfterAll
    void close(final VertxTestContext testContext) {
        vertx.close(testContext.succeedingThenComplete());
        webClient.close();
        Repositories.shutdown();
    }

    @Test
    void getQuote(final VertxTestContext testContext) {
        webClient.get(PORT, HOST, "/api/quotes/2").send()
                .onFailure(testContext::failNow)
                .onSuccess(response -> testContext.verify(() -> {
                    assertEquals(200, response.statusCode(), MSG_200_EXPECTED);
                    assertTrue(
                            StringUtils.isNotBlank(response.bodyAsJsonObject().getString("value")),
                            "Empty quotes are not allowed"
                    );
                    testContext.completeNow();
                }));
    }

    @Test
    void getQuoteWithInvalidID(final VertxTestContext testContext) {
        webClient.get(PORT, HOST, "/api/quotes/333").send()
                .onFailure(testContext::failNow)
                .onSuccess(response -> testContext.verify(() -> {
                    assertEquals(200, response.statusCode(), MSG_200_EXPECTED);
                    assertTrue(
                            StringUtils.isNotBlank(response.bodyAsJsonObject().getString("value")),
                            "Empty quotes are not allowed"
                    );
                    testContext.completeNow();
                }));
    }

    @Test
    void createQuote(final VertxTestContext testContext) {
        String testQuote = "some value";
        webClient.post(PORT, HOST, "/api/quotes").sendJsonObject(createQuote(testQuote))
                .onFailure(testContext::failNow)
                .onSuccess(response -> testContext.verify(() -> {
                    assertEquals(201, response.statusCode(), MSG_201_EXPECTED);
                    assertEquals(
                            testQuote,
                            response.bodyAsJsonObject().getString("value"),
                            "Quote does not match " + testQuote);
                    testContext.completeNow();
                }));
    }

    @Test
    void updateQuote(final VertxTestContext testContext) {
        String testQuote = "some value";
        webClient.put(PORT, HOST, "/api/quotes/0").sendJsonObject(createQuote(testQuote))
                .onFailure(testContext::failNow)
                .onSuccess(response -> testContext.verify(() -> {
                    assertEquals(200, response.statusCode(), MSG_200_EXPECTED);
                    assertEquals(
                            testQuote,
                            response.bodyAsJsonObject().getString("value"),
                            "Quote does not match " + testQuote);
                    testContext.completeNow();
                }));
    }

    @Test
    void deleteQuote(final VertxTestContext testContext) {
        webClient.delete(PORT, HOST, "/api/quotes/1").send()
                .onFailure(testContext::failNow)
                .onSuccess(response -> testContext.verify(() -> {
                    assertEquals(204, response.statusCode(), MSG_204_EXPECTED);
                    testContext.completeNow();
                }));
    }

    private JsonObject createQuote(String quote) {
        return new JsonObject().put("quote", quote);
    }


    @Test
    void getOrder(final VertxTestContext testContext) {
        webClient.get(PORT, HOST, "/api/orders/1").send()
                .onFailure(testContext::failNow)
                .onSuccess(response -> testContext.verify(() -> {
                    assertEquals(200, response.statusCode(), MSG_200_EXPECTED);
                    testContext.completeNow();
        }));
    }

    @Test
    void getOrders(final VertxTestContext testContext) {
        webClient.get(PORT,HOST, "/api/orders/").send()
                .onFailure(testContext::failNow)
                .onSuccess(response -> testContext.verify(() -> {
                    assertEquals(200, response.statusCode(), MSG_200_EXPECTED);
                    testContext.completeNow();
                }));
    }
    private JsonObject createOrder(int customerId, int productId, int amount) {
        return new JsonObject().put("customerId", customerId)
                .put("productId", productId)
                .put("amountInKg", amount);
    }

    @Test
    void createOrder(final VertxTestContext testContext) {
        webClient.post(PORT, HOST, "/api/orders").sendJsonObject(createOrder(1,1,1500))
                .onFailure(testContext::failNow)
                .onSuccess(response -> testContext.verify(() -> {
                    assertEquals(201, response.statusCode(), MSG_201_EXPECTED);
                    assertEquals(
                            1,
                            response.bodyAsJsonObject().getJsonObject("customer").getInteger("id"),
                            "customerId does not match " + 1);
                    assertEquals(
                            1,
                            response.bodyAsJsonObject().getJsonObject("product").getInteger("id"),
                            "productId does not match " + 1);
                    assertEquals(
                            1500,
                            response.bodyAsJsonObject().getInteger("amountInKg"),
                            "productId does not match " + 1);
                    testContext.completeNow();
                }));
    }

    private JsonObject updateOrder(String orderstatus) {
        return new JsonObject()
                .put("status", orderstatus);
    }

    @Test
    void updateOrderCancelled(final VertxTestContext testContext) {
        webClient.put(PORT, HOST, "/api/orders/1").sendJsonObject(updateOrder("CANCELLED"))
                .onFailure(testContext::failNow)
                .onSuccess(response -> testContext.verify(() -> {
                    assertEquals(200, response.statusCode(), MSG_200_EXPECTED);
                    assertEquals(
                            "CANCELLED",
                            response.bodyAsJsonObject().getString("orderStatus"),
                            "status does not match CANCELLED");
                    testContext.completeNow();
                }));
    }

    @Test
    void updateOrderSucces(final VertxTestContext testContext) {
        webClient.put(PORT, HOST, "/api/orders/1").sendJsonObject(updateOrder("SUCCES"))
                .onFailure(testContext::failNow)
                .onSuccess(response -> testContext.verify(() -> {
                    assertEquals(200, response.statusCode(), MSG_200_EXPECTED);
                    assertEquals(
                            "SUCCES",
                            response.bodyAsJsonObject().getString("orderStatus"),
                            "status does not match SUCCES");
                    testContext.completeNow();
                }));
    }

    @Test
    void updateOrderPending(final VertxTestContext testContext) {
        webClient.put(PORT, HOST, "/api/orders/1").sendJsonObject(updateOrder("PENDING"))
                .onFailure(testContext::failNow)
                .onSuccess(response -> testContext.verify(() -> {
                    assertEquals(200, response.statusCode(), MSG_200_EXPECTED);
                    assertEquals(
                            "PENDING",
                            response.bodyAsJsonObject().getString("orderStatus"),
                            "status does not match PENDING");
                    testContext.completeNow();
                }));
    }

    @Test
    void getProducts(final VertxTestContext testContext) {
        webClient.get(PORT,HOST, "/api/products/").send()
                .onFailure(testContext::failNow)
                .onSuccess(response -> testContext.verify(() -> {
                    assertEquals(200, response.statusCode(), MSG_200_EXPECTED);
                    testContext.completeNow();
                }));
    }

    @Test
    void getProduct(final VertxTestContext testContext) {
        webClient.get(PORT, HOST, "/api/products/1").send()
                .onFailure(testContext::failNow)
                .onSuccess(response -> testContext.verify(() -> {
                    assertEquals(200, response.statusCode(), MSG_200_EXPECTED);
                    testContext.completeNow();
                }));
    }

    private JsonObject createProduct(String name, double price, int amountInStock) {
        JsonObject obj = new JsonObject();
        obj.put("name", name)
                .put("pricePerKg", price)
                .put("amountInStockInKg", amountInStock);
        return obj;
    }
    @Test
    void insertProduct(final VertxTestContext testContext) {
        webClient.post(PORT, HOST, "/api/products").sendJsonObject(createProduct("Iron (Fe)",2.50, 1500))
                .onFailure(testContext::failNow)
                .onSuccess(response -> testContext.verify(() -> {
                    assertEquals(201, response.statusCode(), MSG_201_EXPECTED);
                    assertEquals(
                            "Iron (Fe)",
                            response.bodyAsJsonObject().getString("name"),
                            "name does not match Iron (Fe)");
                    assertEquals(
                            2.50,
                            response.bodyAsJsonObject().getDouble("pricePerKg"),
                            "pricePerKg does not match 2.50");
                    assertEquals(
                            1500,
                            response.bodyAsJsonObject().getInteger("amountInStockInKg"),
                            "amountInStockInKg does not match 1500"
                    );
                    testContext.completeNow();
                }));
    }

    @Test
    void updateProduct(final VertxTestContext testContext) {
        webClient.put(PORT, HOST, "/api/products/1").sendJsonObject(createProduct("Iron (Fe)",2.75, 1500))
                .onFailure(testContext::failNow)
                .onSuccess(response -> testContext.verify(() -> {
                    assertEquals(200, response.statusCode(), MSG_200_EXPECTED);
                    assertEquals(
                            "Iron (Fe)",
                            response.bodyAsJsonObject().getString("name"),
                            "name does not match Iron (Fe)");
                    assertEquals(
                            2.75,
                            response.bodyAsJsonObject().getDouble("pricePerKg"),
                            "pricePerKg does not match 2.50");
                    assertEquals(
                            1500,
                            response.bodyAsJsonObject().getInteger("amountInStockInKg"),
                            "amountInStockInKg does not match 1500"
                    );
                    testContext.completeNow();
                }));
    }

    @Test
    void getTiers(final VertxTestContext testContext) {
        webClient.get(PORT,HOST, "/api/tiers/").send()
                .onFailure(testContext::failNow)
                .onSuccess(response -> testContext.verify(() -> {
                    assertEquals(200, response.statusCode(), MSG_200_EXPECTED);
                    testContext.completeNow();
                }));
    }

    @Test
    void getTier(final VertxTestContext testContext) {
        webClient.get(PORT, HOST, "/api/tiers/1").send()
                .onFailure(testContext::failNow)
                .onSuccess(response -> testContext.verify(() -> {
                    assertEquals(200, response.statusCode(), MSG_200_EXPECTED);
                    testContext.completeNow();
                }));
    }

    private JsonObject createTier(String name, int monthlyLimitInKg, double additionalDisposalPerKg, int freePickUps, double pickupFeePerKg, double paymentPerMonth) {
        JsonObject object = new JsonObject();
        object.put("name", name)
                .put("monthlyLimitInKg", monthlyLimitInKg)
                .put("feeAdditionalDisposalPerKg",additionalDisposalPerKg)
                .put( "freePickups", freePickUps)
                .put("pickupFeePerKg", pickupFeePerKg)
                .put("paymentPerMonth", paymentPerMonth);
        return object;
    }
    @Test
    void createTier(final VertxTestContext testContext) {
        webClient.post(PORT, HOST, "/api/tiers").sendJsonObject(createTier("basic",1,1,1,1,1))
                .onFailure(testContext::failNow)
                .onSuccess(response -> testContext.verify(() -> {
                    assertEquals(201, response.statusCode(), MSG_201_EXPECTED);
                    testContext.completeNow();
                }));
    }

    @Test
    void updateTier(final VertxTestContext testContext) {
        webClient.put(PORT, HOST, "/api/tiers/1").sendJsonObject(createTier("basic",1,1,1,1,1))
                .onFailure(testContext::failNow)
                .onSuccess(response -> testContext.verify(() -> {
                    assertEquals(200, response.statusCode(), MSG_200_EXPECTED);
                    testContext.completeNow();
                }));
    }
    @Test
    void getCustomer(final VertxTestContext testContext) {
        webClient.get(PORT, HOST, "/api/customers/1").send()
                .onFailure(testContext::failNow)
                .onSuccess(response -> testContext.verify(() -> {
                    assertEquals(200, response.statusCode(), MSG_200_EXPECTED);
                    testContext.completeNow();
                }));
    }

    @Test
    void getCustomers(final VertxTestContext testContext) {
        webClient.get(PORT, HOST, "/api/customers").send()
                .onFailure(testContext::failNow)
                .onSuccess(response -> testContext.verify(() -> {
                    assertEquals(200, response.statusCode(), MSG_200_EXPECTED);
                    testContext.completeNow();
                }));
    }

    @Test
    void getCustomerByName(final VertxTestContext testContext) {
        webClient.get(PORT, HOST, "/api/customers?customerName=Adam").send()
                .onFailure(testContext::failNow)
                .onSuccess(response -> testContext.verify(() -> {
                    assertEquals(200, response.statusCode(), MSG_200_EXPECTED);
                    testContext.completeNow();
                }));
    }

    private JsonObject createCustomer(String name, String address) {
        JsonObject obj = new JsonObject();
        obj.put("name", name)
                .put("address", address);
        return obj;
    }

    private JsonObject createCustomer(String name, String address, int tierId) {
        JsonObject obj = new JsonObject();
        obj.put("name", name)
                .put("address", address)
                .put("tierId", tierId);
        return obj;
    }

    @Test
    void createCustomer(final VertxTestContext testContext) {
        webClient.post(PORT, HOST, "/api/customers").sendJsonObject(createCustomer("Iron (Fe)","Street"))
                .onFailure(testContext::failNow)
                .onSuccess(response -> testContext.verify(() -> {
                    assertEquals(201, response.statusCode(), MSG_201_EXPECTED);
                    testContext.completeNow();
                }));
    }

    @Test
    void updateCustomer(final VertxTestContext testContext) {
        webClient.put(PORT, HOST, "/api/customers/1").sendJsonObject(createCustomer("Adam", "Straat",1))
                .onFailure(testContext::failNow)
                .onSuccess(response -> testContext.verify(() -> {
                    assertEquals(200, response.statusCode(), MSG_200_EXPECTED);
                    testContext.completeNow();
                }));
    }

    private JsonObject createGarbage(double amount) {
        JsonObject object = new JsonObject();
        object.put("amountOfGarbage", amount);
        return object;
    }
    @Test
    void updateGarbageDisposed(final VertxTestContext testContext) {
        webClient.put(PORT, HOST, "/api/customers/1/garbageDisposed").sendJsonObject(createGarbage(3.23))
                .onFailure(testContext::failNow)
                .onSuccess(response -> testContext.verify(() -> {
                    assertEquals(200, response.statusCode(), MSG_200_EXPECTED);
                    testContext.completeNow();
                }));
    }

    @Test
    void getPickups(final VertxTestContext testContext) {
        webClient.get(PORT, HOST, "/api/pickups").send()
                .onFailure(testContext::failNow)
                .onSuccess(response -> testContext.verify(() -> {
                    assertEquals(200, response.statusCode(), MSG_200_EXPECTED);
                    testContext.completeNow();
                }));
    }

    @Test
    void getPickupsByCustomerId(final VertxTestContext testContext) {
        webClient.get(PORT, HOST, "/api/pickups?customerId=1").send()
                .onFailure(testContext::failNow)
                .onSuccess(response -> testContext.verify(() -> {
                    assertEquals(200, response.statusCode(), MSG_200_EXPECTED);
                    testContext.completeNow();
                }));
    }

    @Test
    void getPickupsByCustomerName(final VertxTestContext testContext) {
        webClient.get(PORT, HOST, "/api/pickups?customerName=Adam").send()
                .onFailure(testContext::failNow)
                .onSuccess(response -> testContext.verify(() -> {
                    assertEquals(200, response.statusCode(), MSG_200_EXPECTED);
                    testContext.completeNow();
                }));
    }

    @Test
    void getPickup(final VertxTestContext testContext) {
        webClient.get(PORT, HOST, "/api/pickups/1").send()
                .onFailure(testContext::failNow)
                .onSuccess(response -> testContext.verify(() -> {
                    assertEquals(200, response.statusCode(), MSG_200_EXPECTED);
                    testContext.completeNow();
                }));
    }

    private JsonObject createPickup(int customerId, String date) {
        JsonObject jsonObject = new JsonObject();
        jsonObject.put("customerId", customerId)
                .put("requestedPickupDate", date);
        return jsonObject;
    }
    @Test
    void createPickup(final VertxTestContext testContext) {
        webClient.post(PORT, HOST, "/api/pickups").sendJsonObject(createPickup(1,"2022-01-01"))
                .onFailure(testContext::failNow)
                .onSuccess(response -> testContext.verify(() -> {
                    assertEquals(201, response.statusCode(), MSG_201_EXPECTED);
                    testContext.completeNow();
                }));
    }

    private JsonObject updatePickup(String status) {
        JsonObject jsonObject = new JsonObject();
        jsonObject.put("status", status);
        return jsonObject;
    }
    @Test
    void updatePickup(final VertxTestContext testContext) {
        webClient.put(PORT, HOST, "/api/pickups/1").sendJsonObject(updatePickup("SUCCES"))
                .onFailure(testContext::failNow)
                .onSuccess(response -> testContext.verify(() -> {
                    assertEquals(200, response.statusCode(), MSG_200_EXPECTED);
                    testContext.completeNow();
                }));
    }


}