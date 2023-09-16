package be.howest.ti.mars.web.bridge;

import be.howest.ti.mars.logic.controller.DefaultMarsController;
import be.howest.ti.mars.logic.controller.MarsController;
import be.howest.ti.mars.logic.domain.*;
import be.howest.ti.mars.web.exceptions.MalformedRequestException;
import io.vertx.core.http.HttpMethod;
import io.vertx.ext.web.Router;
import io.vertx.ext.web.RoutingContext;
import io.vertx.ext.web.handler.CorsHandler;
import io.vertx.ext.web.openapi.RouterBuilder;

import java.util.Map;
import java.util.NoSuchElementException;
import java.util.Objects;
import java.util.logging.Level;
import java.util.logging.Logger;


/**
 * In the MarsOpenApiBridge class you will create one handler-method per API operation.
 * The job of the "bridge" is to bridge between JSON (request and response) and Java (the controller).
 * <p>
 * For each API operation you should get the required data from the `Request` class.
 * The Request class will turn the HTTP request data into the desired Java types (int, String, Custom class,...)
 * This desired type is then passed to the controller.
 * The return value of the controller is turned to Json or another Web data type in the `Response` class.
 */
public class MarsOpenApiBridge {
    private static final Logger LOGGER = Logger.getLogger(MarsOpenApiBridge.class.getName());
    private final MarsController controller;

    public Router buildRouter(RouterBuilder routerBuilder) {
        LOGGER.log(Level.INFO, "Installing cors handlers");
        routerBuilder.rootHandler(createCorsHandler());

        LOGGER.log(Level.INFO, "Installing failure handlers for all operations");
        routerBuilder.operations().forEach(op -> op.failureHandler(this::onFailedRequest));

        LOGGER.log(Level.INFO, "Installing handler for: getQuote");
        routerBuilder.operation("getQuote").handler(this::getQuote);

        LOGGER.log(Level.INFO, "Installing handler for: createQuote");
        routerBuilder.operation("createQuote").handler(this::createQuote);

        LOGGER.log(Level.INFO, "Installing handler for: updateQuote");
        routerBuilder.operation("updateQuote").handler(this::updateQuote);

        LOGGER.log(Level.INFO, "Installing handler for: deleteQuote");
        routerBuilder.operation("deleteQuote").handler(this::deleteQuote);

        LOGGER.log(Level.INFO, "Installing handler for: getOrder");
        routerBuilder.operation("getOrder").handler(this::getOrder);

        LOGGER.log(Level.INFO, "Installing handler for: getOrders");
        routerBuilder.operation("getOrders").handler(this::getOrders);

        LOGGER.log(Level.INFO, "Installing handler for: createOrder");
        routerBuilder.operation("createOrder").handler(this::createOrder);

        LOGGER.log(Level.INFO, "Installing handler for: updateOrder");
        routerBuilder.operation("updateOrder").handler(this::updateOrder);

        LOGGER.log(Level.INFO, "Installing handler for: getProduct");
        routerBuilder.operation("getProduct").handler(this::getProduct);

        LOGGER.log(Level.INFO, "Installing handler for: getProducts");
        routerBuilder.operation("getProducts").handler(this::getProducts);

        LOGGER.log(Level.INFO, "Installing handler for: createProduct");
        routerBuilder.operation("createProduct").handler(this::createProduct);

        LOGGER.log(Level.INFO, "Installing handler for: updateProduct");
        routerBuilder.operation("updateProduct").handler(this::updateProduct);

        LOGGER.log(Level.INFO, "Installing handler for: getTier");
        routerBuilder.operation("getTier").handler(this::getTier);

        LOGGER.log(Level.INFO, "Installing handler for: getTiers");
        routerBuilder.operation("getTiers").handler(this::getTiers);

        LOGGER.log(Level.INFO, "Installing handler for: createTier");
        routerBuilder.operation("createTier").handler(this::createTier);

        LOGGER.log(Level.INFO, "Installing handler for: updateTier");
        routerBuilder.operation("updateTier").handler(this::updateTier);

        LOGGER.log(Level.INFO, "Installing handler for: getCustomer");
        routerBuilder.operation("getCustomer").handler(this::getCustomer);

        LOGGER.log(Level.INFO, "Installing handler for: getCustomers");
        routerBuilder.operation("getCustomers").handler(this::getCustomers);

        LOGGER.log(Level.INFO, "Installing handler for: createCustomer");
        routerBuilder.operation("createCustomer").handler(this::createCustomer);

        LOGGER.log(Level.INFO, "Installing handler for: updateCustomer");
        routerBuilder.operation("updateCustomer").handler(this::updateCustomer);

        LOGGER.log(Level.INFO, "Installing handler for: addGarbage");
        routerBuilder.operation("addGarbage").handler(this::addGarbage);

        LOGGER.log(Level.INFO, "Installing handler for: getPickups");
        routerBuilder.operation("getPickups").handler(this::getPickups);

        LOGGER.log(Level.INFO, "Installing handler for: getPickup");
        routerBuilder.operation("getPickup").handler(this::getPickup);

        LOGGER.log(Level.INFO, "Installing handler for: createPickup");
        routerBuilder.operation("createPickup").handler(this::createPickup);

        LOGGER.log(Level.INFO, "Installing handler for: updatePickup");
        routerBuilder.operation("updatePickup").handler(this::updatePickup);

        LOGGER.log(Level.INFO, "All handlers are installed, creating router.");
        return routerBuilder.createRouter();
    }

    public MarsOpenApiBridge() {
        this.controller = new DefaultMarsController();
    }

    public MarsOpenApiBridge(MarsController controller) {
        this.controller = controller;
    }

    public void getQuote(RoutingContext ctx) {
        Quote quote = controller.getQuote(Request.from(ctx).getQuoteId());

        Response.sendQuote(ctx, quote);
    }

    public void createQuote(RoutingContext ctx) {
        String quote = Request.from(ctx).getQuote();

        Response.sendQuoteCreated(ctx, controller.createQuote(quote));
    }

    public void updateQuote(RoutingContext ctx) {
        Request request = Request.from(ctx);
        String quoteValue = request.getQuote();
        int quoteId = request.getQuoteId();

        Response.sendQuoteUpdated(ctx, controller.updateQuote(quoteId, quoteValue));
    }

    public void deleteQuote(RoutingContext ctx) {
        int quoteId = Request.from(ctx).getQuoteId();

        controller.deleteQuote(quoteId);

        Response.sendQuoteDeleted(ctx);
    }

    public void getOrder(RoutingContext ctx) {
        Order order = controller.getOrder(Request.from(ctx).getOrderId());

        Response.sendOrder(ctx, order);
    }

    public void getOrders(RoutingContext ctx) {
        Map<Integer, Order> orderMap = controller.getOrders();
        Response.sendOrders(ctx, orderMap);
    }

    public void createOrder(RoutingContext ctx) {
        int productId = Request.from(ctx).getProductIdFromBody();
        int customerId = Request.from(ctx).getCustomerIdFromBody();
        int amountInKg = Request.from(ctx).getAmountInKgFromBody();
        controller.substractStock(productId, amountInKg);
        Order order = controller.insertOrder(productId, customerId, amountInKg);
        Response.sendOrderCreated(ctx, order);
    }

    public void updateOrder(RoutingContext ctx) {
        int orderId = Request.from(ctx).getOrderId();
        Status orderStatus = Request.from(ctx).getOrderStatusFromBody();
        Order order = controller.updateOrder(orderId, orderStatus);

        Response.sendOrderUpdated(ctx, order);
    }

    public void getProduct(RoutingContext ctx) {
        Product product = controller.getProduct(Request.from(ctx).getProductId());
        Response.sendProduct(ctx, product);
    }

    public void getProducts(RoutingContext ctx) {
        Map<Integer,Product> products = controller.getProducts();
        Response.sendProducts(ctx, products);
    }

    public void createProduct(RoutingContext ctx) {
        String name = Request.from(ctx).getNameFromBody();
        double pricePerKg = Request.from(ctx).getPricePerKgFromBody();
        double amountInStockInKg = Request.from(ctx).getAmountInStockInKgFromBody();
        Response.sendProductCreated(ctx, controller.createProduct(name, pricePerKg, amountInStockInKg));
    }

    public void updateProduct(RoutingContext ctx) {
        int id = Request.from(ctx).getProductId();
        String name = Request.from(ctx).getNameFromBody();
        double pricePerKg = Request.from(ctx).getPricePerKgFromBody();
        double amountInStockInKg = Request.from(ctx).getAmountInStockInKgFromBody();

        Response.sendProductUpdated(ctx, controller.updateProduct(id, name, pricePerKg, amountInStockInKg));
    }

    public void getTier(RoutingContext ctx) {
        int id = Request.from(ctx).getTierId();
        Tier tier = controller.getTier(id);
        Response.sendTier(ctx, tier);
    }

    public void getTiers(RoutingContext ctx) {
        Map<Integer, Tier> tiers = controller.getTiers();
        Response.sendTiers(ctx, tiers);
    }

    public void createTier(RoutingContext ctx) {
        String name = Request.from(ctx).getNameFromBody();
        int monthlyLimitInKg = Request.from(ctx).getMonthlyLimitInKgFromBody();
        double feeAdditionalDisposalPerKg = Request.from(ctx).getFeeAdditionalDisposalPerKgFromBody();
        int freePickups = Request.from(ctx).getFreePickupsFromBody();
        double pickupFeePerKg = Request.from(ctx).getPickupFeePerKgFromBody();
        int paymentPerMonth = Request.from(ctx).getPaymentPerMonthFromBody();

        Tier tier = controller.createTier(name, monthlyLimitInKg, feeAdditionalDisposalPerKg, freePickups, pickupFeePerKg, paymentPerMonth);

        Response.sendTierCreated(ctx, tier);
    }

    public void updateTier(RoutingContext ctx) {
        int id = Request.from(ctx).getTierId();
        String name = Request.from(ctx).getNameFromBody();
        int monthlyLimitInKg = Request.from(ctx).getMonthlyLimitInKgFromBody();
        double feeAdditionalDisposalPerKg = Request.from(ctx).getFeeAdditionalDisposalPerKgFromBody();
        int freePickups = Request.from(ctx).getFreePickupsFromBody();
        double pickupFeePerKg = Request.from(ctx).getPickupFeePerKgFromBody();
        int paymentPerMonth = Request.from(ctx).getPaymentPerMonthFromBody();

        Tier tier = controller.updateTier(id, name, monthlyLimitInKg, feeAdditionalDisposalPerKg, freePickups, pickupFeePerKg, paymentPerMonth);

        Response.sendTierUpdated(ctx, tier);
    }

    public void getCustomer(RoutingContext ctx) {
        int id = Request.from(ctx).getCustomerId();
        Customer customer = controller.getCustomer(id);

        Response.sendCustomer(ctx, customer);
    }

    public void getCustomers(RoutingContext ctx) {
        if (Request.from(ctx).isCustomerName()) {
            String customerName = Request.from(ctx).getCustomerName();
            Customer customer = controller.getCustomer(customerName);
            Response.sendCustomer(ctx, customer);
        } else {
            Map<Integer, Customer> customerMap = controller.getCustomers();
            Response.sendCustomers(ctx, customerMap);
        }
    }

    public void createCustomer(RoutingContext ctx) {
        String name = Request.from(ctx).getNameFromBody();
        String address = Request.from(ctx).getCustomerAddressFromBody();
        Customer customer = controller.createCustomer(name, address);
        Response.sendCustomerCreated(ctx, customer);
    }

    public void updateCustomer(RoutingContext ctx) {
        int id = Request.from(ctx).getCustomerId();
        String name = Request.from(ctx).getNameFromBody();
        String address = Request.from(ctx).getCustomerAddressFromBody();
        int tierId = Request.from(ctx).getTierIdFromBody();
        Customer customer = controller.updateCustomer(id, name, address, tierId);
        Response.customerUpdated(ctx, customer);
    }
    public void addGarbage(RoutingContext ctx) {
        int customerId = Request.from(ctx).getCustomerId();
        Double amountOfGarbage = Request.from(ctx).getAmountOfGarbageFromBody();

        Customer customer = controller.updateDisposedGarbage(customerId, amountOfGarbage);
        Response.customerUpdated(ctx, customer);
    }
    private void getPickup(RoutingContext ctx) {
        int id = Request.from(ctx).getPickupId();
        Pickup pickup = controller.getPickup(id);
        Response.sendPickup(ctx, pickup);
    }
    private void getPickups(RoutingContext ctx) {
        Map<Integer, Pickup> pickupMap;

        if (Request.from(ctx).isCustomerId()) {
            int customerId = Request.from(ctx).getCustomerIdFromQuery();
            pickupMap = controller.getPickups(customerId);

        } else if (Request.from(ctx).isCustomerName()){
            String customerName = Request.from(ctx).getCustomerName();
            pickupMap = controller.getPickups(customerName);

        } else pickupMap = controller.getPickups();

        Response.sendPickups(ctx, pickupMap);
    }
    private void createPickup(RoutingContext ctx) {
        int customerId = Request.from(ctx).getCustomerIdFromBody();
        String requestedPickupDate = Request.from(ctx).getRequestedPickupDateFromBody();
        controller.updatePickupsLeft(customerId);
        Pickup pickup = controller.createPickup(customerId,requestedPickupDate);
        Response.sendPickupCreated(ctx, pickup);
    }
    private void updatePickup(RoutingContext ctx) {
        int pickupId = Request.from(ctx).getPickupId();
        Status status = Request.from(ctx).getOrderStatusFromBody();
        Pickup pickup = controller.updatePickup(pickupId, status);
        Response.sendPickupUpdated(ctx, pickup);
    }

    private void onFailedRequest(RoutingContext ctx) {
        Throwable cause = ctx.failure();
        int code = ctx.statusCode();
        String quote = Objects.isNull(cause) ? "" + code : cause.getMessage();

        // Map custom runtime exceptions to a HTTP status code.
        LOGGER.log(Level.INFO, "Failed request", cause);
        if (cause instanceof IllegalArgumentException) {
            code = 400;
        } else if (cause instanceof MalformedRequestException) {
            code = 400;
        } else if (cause instanceof NoSuchElementException) {
            code = 404;
        } else {
            LOGGER.log(Level.WARNING, "Failed request", cause);
        }

        Response.sendFailure(ctx, code, quote);
    }

    private CorsHandler createCorsHandler() {
        return CorsHandler.create(".*.")
                .allowedHeader("x-requested-with")
                .allowedHeader("Access-Control-Allow-Origin")
                .allowedHeader("Access-Control-Allow-Credentials")
                .allowCredentials(true)
                .allowedHeader("origin")
                .allowedHeader("Content-Type")
                .allowedHeader("Authorization")
                .allowedHeader("accept")
                .allowedMethod(HttpMethod.HEAD)
                .allowedMethod(HttpMethod.GET)
                .allowedMethod(HttpMethod.POST)
                .allowedMethod(HttpMethod.OPTIONS)
                .allowedMethod(HttpMethod.PATCH)
                .allowedMethod(HttpMethod.DELETE)
                .allowedMethod(HttpMethod.PUT);
    }
}
