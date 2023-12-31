package be.howest.ti.mars.web.bridge;

import be.howest.ti.mars.logic.domain.*;
import io.vertx.core.http.HttpHeaders;
import io.vertx.core.json.Json;
import io.vertx.core.json.JsonObject;
import io.vertx.ext.web.RoutingContext;

import java.util.Map;

/**
 * The Response class is responsible for translating the result of the controller into
 * JSON responses with an appropriate HTTP code.
 */
public class Response {

    private Response() { }

    public static void sendQuote(RoutingContext ctx, Quote quote) {
        sendOkJsonResponse(ctx, JsonObject.mapFrom(quote));
    }

    public static void sendQuoteCreated(RoutingContext ctx, Quote quote) {
        sendJsonResponse(ctx, 201, JsonObject.mapFrom(quote));
    }

    public static void sendQuoteDeleted(RoutingContext ctx) {
        sendEmptyResponse(ctx, 204);
    }

    public static void sendQuoteUpdated(RoutingContext ctx, Quote quote) {
        sendOkJsonResponse(ctx, JsonObject.mapFrom(quote));
    }

    private static void sendOkJsonResponse(RoutingContext ctx, JsonObject response) {
        sendJsonResponse(ctx, 200, response);
    }

    private static void sendCreatedJsonResponse(RoutingContext ctx, JsonObject response) {
        sendJsonResponse(ctx, 201, response);
    }

    private static void sendEmptyResponse(RoutingContext ctx, int statusCode) {
        ctx.response()
                .setStatusCode(statusCode)
                .end();
    }

    private static void sendJsonResponse(RoutingContext ctx, int statusCode, Object response) {
        ctx.response()
                .putHeader(HttpHeaders.CONTENT_TYPE, "application/json")
                .setStatusCode(statusCode)
                .end(Json.encodePrettily(response));
    }

    public static void sendFailure(RoutingContext ctx, int code, String quote) {
        sendJsonResponse(ctx, code, new JsonObject()
                .put("failure", code)
                .put("cause", quote));
    }

    public static void sendCustomer(RoutingContext ctx, Customer customer) {
        sendOkJsonResponse(ctx, JsonObject.mapFrom(customer));
    }

    public static void sendOrder(RoutingContext ctx, Order order) {
        sendOkJsonResponse(ctx, JsonObject.mapFrom(order));
    }

    public static void sendOrders(RoutingContext ctx, Map<Integer, Order> orderMap) {
        sendOkJsonResponse(ctx, JsonObject.mapFrom(orderMap));
    }

    public static void sendOrderUpdated(RoutingContext ctx, Order order) {
        sendOkJsonResponse(ctx, JsonObject.mapFrom(order));
    }

    public static void sendOrderCreated(RoutingContext ctx, Order order) {
        sendJsonResponse(ctx,201, JsonObject.mapFrom(order));
    }

    public static void sendProduct(RoutingContext ctx, Product product) {
        sendOkJsonResponse(ctx, JsonObject.mapFrom(product));
    }

    public static void sendProducts(RoutingContext ctx, Map<Integer, Product> products) {
        sendOkJsonResponse(ctx, JsonObject.mapFrom(products));
    }

    public static void sendProductCreated(RoutingContext ctx, Product product) {
        sendJsonResponse(ctx, 201, JsonObject.mapFrom(product));
    }

    public static void sendProductUpdated(RoutingContext ctx, Product product) {
        sendOkJsonResponse(ctx, JsonObject.mapFrom(product));
    }

    public static void sendTier(RoutingContext ctx, Tier tier) {
        sendOkJsonResponse(ctx, JsonObject.mapFrom(tier));
    }

    public static void sendTiers(RoutingContext ctx, Map<Integer, Tier> tiers) {
        sendOkJsonResponse(ctx, JsonObject.mapFrom(tiers));
    }

    public static void sendCustomers(RoutingContext ctx, Map<Integer, Customer> customerMap) {
        sendOkJsonResponse(ctx, JsonObject.mapFrom(customerMap));
    }

    public static void sendCustomerCreated(RoutingContext ctx, Customer customer) {
        sendCreatedJsonResponse(ctx, JsonObject.mapFrom(customer));
    }

    public static void customerUpdated(RoutingContext ctx, Customer customer) {
        sendOkJsonResponse(ctx, JsonObject.mapFrom(customer));
    }

    public static void sendPickup(RoutingContext ctx, Pickup pickup) {
        sendOkJsonResponse(ctx, JsonObject.mapFrom(pickup));
    }

    public static void sendPickups(RoutingContext ctx, Map<Integer, Pickup> pickupMap) {
        sendOkJsonResponse(ctx, JsonObject.mapFrom(pickupMap));
    }

    public static void sendPickupCreated(RoutingContext ctx, Pickup pickup) {
        sendCreatedJsonResponse(ctx, JsonObject.mapFrom(pickup));
    }

    public static void sendPickupUpdated(RoutingContext ctx, Pickup pickup) {
        sendOkJsonResponse(ctx, JsonObject.mapFrom(pickup));
    }

    public static void sendTierCreated(RoutingContext ctx, Tier tier) {
        sendCreatedJsonResponse(ctx, JsonObject.mapFrom(tier));
    }

    public static void sendTierUpdated(RoutingContext ctx, Tier tier) {
        sendOkJsonResponse(ctx, JsonObject.mapFrom(tier));
    }
}
