package be.howest.ti.mars.web.bridge;

import be.howest.ti.mars.logic.domain.Status;
import be.howest.ti.mars.web.exceptions.MalformedRequestException;
import io.vertx.ext.web.RoutingContext;
import io.vertx.ext.web.validation.RequestParameters;
import io.vertx.ext.web.validation.ValidationHandler;

import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * The Request class is responsible for translating information that is part of the
 * request into Java.
 *
 * For every piece of information that you need from the request, you should provide a method here.
 * You can find information in:
 * - the request path: params.pathParameter("some-param-name")
 * - the query-string: params.queryParameter("some-param-name")
 * Both return a `RequestParameter`, which can contain a string or an integer in our case.
 * The actual data can be retrieved using `getInteger()` or `getString()`, respectively.
 * You can check if it is an integer (or not) using `isNumber()`.
 *
 * Finally, some requests have a body. If present, the body will always be in the json format.
 * You can acces this body using: `params.body().getJsonObject()`.
 *
 * **TIP:** Make sure that al your methods have a unique name. For instance, there is a request
 * that consists of more than one "player name". You cannot use the method `getPlayerName()` for both,
 * you will need a second one with a different name.
 */
public class Request {
    private static final Logger LOGGER = Logger.getLogger(Request.class.getName());
    public static final String SPEC_QUOTE_ID = "quoteId";
    public static final String SPEC_CUSTOMER_ID = "customerId";
    public static final String SPEC_QUOTE = "quote";
    private static final String SPEC_ORDER_ID = "orderId";
    private static final String SPEC_PRODUCT_ID = "productId";
    private static final String SPEC_NAME = "name";
    private static final String SPEC_PRODUCT_PRICE_PER_KG = "pricePerKg";
    private static final String SPEC_PRODUCT_AMOUNT_IN_STOCK_IN_KG = "amountInStockInKg";
    private static final String SPEC_TIER_ID = "tierId";
    private static final String SPEC_CUSTOMER_ADDRESS = "address";
    private static final String STATUS = "status";
    private static final String SPEC_PICKUP_ID = "pickupId";
    private static final String SPEC_CUSTOMER_NAME = "customerName";
    private static final String SPEC_PICKUP_REQUESTED_PICKUP_DATE = "requestedPickupDate";
    private static final String SPEC_AMOUNT_OF_GARBAGE = "amountOfGarbage";
    private static final String SPEC_MONTHLY_LIMIT_IN_KG = "monthlyLimitInKg";
    private static final String SPEC_FEE_ADDITIONAL_DISPOSAL_PER_KG = "feeAdditionalDisposalPerKg";
    private static final String SPEC_FREE_PICKUPS = "freePickups";
    private static final String SPEC_PICKUP_FEE_PER_KG = "pickupFeePerKg";
    private static final String SPEC_PAYMENT_PER_MONTH = "paymentPerMonth";
    private static final String LOG_MESSAGE = "Unable to decipher the data in the body";
    private static final String EXCEPTION = "Unable to decipher the data in the request body. See logs for details.";
    private final RequestParameters params;

    public static Request from(RoutingContext ctx) {
        return new Request(ctx);
    }

    private Request(RoutingContext ctx) {
        this.params = ctx.get(ValidationHandler.REQUEST_CONTEXT_KEY);
    }

    public int getQuoteId() {
        return params.pathParameter(SPEC_QUOTE_ID).getInteger();
    }

    public String getQuote() {
        try {
            if (params.body().isJsonObject())
                return params.body().getJsonObject().getString(SPEC_QUOTE);
            return params.body().get().toString();
        } catch (IllegalArgumentException ex) {
            LOGGER.log(Level.INFO, LOG_MESSAGE, ex);
            throw new MalformedRequestException(EXCEPTION);
        }
    }

    public int getCustomerId() {
        return params.pathParameter(SPEC_CUSTOMER_ID).getInteger();
    }

    public int getOrderId() {
        return params.pathParameter(SPEC_ORDER_ID).getInteger();
    }

    public int getProductId() {
        return params.pathParameter(SPEC_PRODUCT_ID).getInteger();
    }

    public int getCustomerIdFromBody() {
        try {
            return params.body().getJsonObject().getInteger(SPEC_CUSTOMER_ID);
        } catch (IllegalArgumentException ex) {
            LOGGER.log(Level.INFO, LOG_MESSAGE, ex);
            throw new MalformedRequestException(EXCEPTION);
        }
    }

    public int getProductIdFromBody() {
        try {
            return params.body().getJsonObject().getInteger(SPEC_PRODUCT_ID);
        } catch (IllegalArgumentException ex) {
            LOGGER.log(Level.INFO, LOG_MESSAGE, ex);
            throw new MalformedRequestException(EXCEPTION);
        }
    }

    public String getNameFromBody() {
        try {
            return params.body().getJsonObject().getString(SPEC_NAME);
        } catch (IllegalArgumentException ex) {
            LOGGER.log(Level.INFO, LOG_MESSAGE, ex);
            throw new MalformedRequestException(EXCEPTION);
        }
    }

    public double getPricePerKgFromBody() {
        try {
            return params.body().getJsonObject().getDouble(SPEC_PRODUCT_PRICE_PER_KG);
        } catch (IllegalArgumentException ex) {
            LOGGER.log(Level.INFO, LOG_MESSAGE, ex);
            throw new MalformedRequestException(EXCEPTION);
        }
    }

    public double getAmountInStockInKgFromBody() {
        try {
            return params.body().getJsonObject().getDouble(SPEC_PRODUCT_AMOUNT_IN_STOCK_IN_KG);
        } catch (IllegalArgumentException ex) {
            LOGGER.log(Level.INFO, LOG_MESSAGE, ex);
            throw new MalformedRequestException(EXCEPTION);
        }
    }

    public int getTierId() {
        return params.pathParameter(SPEC_TIER_ID).getInteger();
    }

    public String getCustomerAddressFromBody() {
        try {
            return params.body().getJsonObject().getString(SPEC_CUSTOMER_ADDRESS);
        } catch (IllegalArgumentException ex) {
            LOGGER.log(Level.INFO, LOG_MESSAGE, ex);
            throw new MalformedRequestException(EXCEPTION);
        }
    }

    public int getTierIdFromBody() {
        try {
            return params.body().getJsonObject().getInteger(SPEC_TIER_ID);
        } catch (IllegalArgumentException ex) {
            LOGGER.log(Level.INFO, LOG_MESSAGE, ex);
            throw new MalformedRequestException(EXCEPTION);
        }
    }

    public Status getOrderStatusFromBody() {
        try {
            String status = params.body().getJsonObject().getString(STATUS);
            return Status.valueOf(status);
        } catch (IllegalArgumentException ex) {
            LOGGER.log(Level.INFO, LOG_MESSAGE, ex);
            throw new MalformedRequestException(EXCEPTION);
        }
    }

    public int getAmountInKgFromBody() {
        try {
            return params.body().getJsonObject().getInteger("amountInKg");
        } catch (IllegalArgumentException ex) {
            LOGGER.log(Level.INFO, LOG_MESSAGE, ex);
            throw new MalformedRequestException(EXCEPTION);
        }
    }

    public int getPickupId() {
        return params.pathParameter(SPEC_PICKUP_ID).getInteger();
    }

    public boolean isCustomerId() {
        if (params.queryParameter(SPEC_CUSTOMER_ID) != null) {
            return params.queryParameter(SPEC_CUSTOMER_ID).isNumber();
        }
        return false;
    }

    public boolean isParamId() {
        if (params.pathParameter(SPEC_CUSTOMER_ID) != null) {
            return params.pathParameter(SPEC_CUSTOMER_ID).isNumber();
        }
        return false;
    }
    public boolean isCustomerName() {
        if (params.queryParameter(SPEC_CUSTOMER_NAME) != null) {
            return params.queryParameter(SPEC_CUSTOMER_NAME).isString();
        }
        return false;
    }

    public String getCustomerName() {
        return params.queryParameter(SPEC_CUSTOMER_NAME).getString();
    }

    public int getCustomerIdFromQuery() {
        return params.queryParameter(SPEC_CUSTOMER_ID).getInteger();
    }

    public String getRequestedPickupDateFromBody() {
        try {
            return params.body().getJsonObject().getString(SPEC_PICKUP_REQUESTED_PICKUP_DATE);
        } catch (IllegalArgumentException ex) {
            LOGGER.log(Level.INFO, LOG_MESSAGE, ex);
            throw new MalformedRequestException(EXCEPTION);
        }
    }

    public Double getAmountOfGarbageFromBody() {
        try {
            return params.body().getJsonObject().getDouble(SPEC_AMOUNT_OF_GARBAGE);
        } catch (IllegalArgumentException ex) {
            LOGGER.log(Level.INFO, LOG_MESSAGE, ex);
            throw new MalformedRequestException(EXCEPTION);
        }
    }


    public int getMonthlyLimitInKgFromBody() {
        try {
            return params.body().getJsonObject().getInteger(SPEC_MONTHLY_LIMIT_IN_KG);
        } catch (IllegalArgumentException ex) {
            LOGGER.log(Level.INFO, LOG_MESSAGE, ex);
            throw new MalformedRequestException(EXCEPTION);
        }
    }

    public double getFeeAdditionalDisposalPerKgFromBody() {
        try {
            return params.body().getJsonObject().getDouble(SPEC_FEE_ADDITIONAL_DISPOSAL_PER_KG);
        } catch (IllegalArgumentException ex) {
            LOGGER.log(Level.INFO, LOG_MESSAGE, ex);
            throw new MalformedRequestException(EXCEPTION);
        }
    }

    public int getFreePickupsFromBody() {
        try {
            return params.body().getJsonObject().getInteger(SPEC_FREE_PICKUPS);
        } catch (IllegalArgumentException ex) {
            LOGGER.log(Level.INFO, LOG_MESSAGE, ex);
            throw new MalformedRequestException(EXCEPTION);
        }
    }

    public double getPickupFeePerKgFromBody() {
        try {
            return params.body().getJsonObject().getDouble(SPEC_PICKUP_FEE_PER_KG);
        } catch (IllegalArgumentException ex) {
            LOGGER.log(Level.INFO, LOG_MESSAGE, ex);
            throw new MalformedRequestException(EXCEPTION);
        }
    }

    public int getPaymentPerMonthFromBody() {
        try {
            return params.body().getJsonObject().getInteger(SPEC_PAYMENT_PER_MONTH);
        } catch (IllegalArgumentException ex) {
            LOGGER.log(Level.INFO, LOG_MESSAGE, ex);
            throw new MalformedRequestException(EXCEPTION);
        }
    }
}
