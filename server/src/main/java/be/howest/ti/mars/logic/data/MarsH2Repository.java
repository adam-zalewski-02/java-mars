package be.howest.ti.mars.logic.data;

import be.howest.ti.mars.logic.domain.*;
import be.howest.ti.mars.logic.exceptions.RepositoryException;
import org.h2.tools.Server;

import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.sql.*;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

/*
This is only a starter class to use an H2 database.
In this start project there was no need for a Java interface MarsRepository.
Please always use interfaces when needed.

To make this class useful, please complete it with the topics seen in the module OOA & SD
 */

public class MarsH2Repository {
    private static final Logger LOGGER = Logger.getLogger(MarsH2Repository.class.getName());
    private static final String SQL_QUOTA_BY_ID = "select id, quote from quotes where id = ?;";
    private static final String SQL_INSERT_QUOTE = "insert into quotes (`quote`) values (?);";
    private static final String SQL_UPDATE_QUOTE = "update quotes set quote = ? where id = ?;";
    private static final String SQL_DELETE_QUOTE = "delete from quotes where id = ?;";
    private static final String SQL_ORDER_BY_ID = "select * from orders where id = ?;";
    private static final String SQL_INSERT_ORDER = "insert into orders (`productId`, `customerId`, `trackNumber`, `orderStatus`, `orderDate`) values (?,?,?,?,?)";
    private static final String SQL_UPDATE_ORDER_STATUS = "update orders set orderStatus = ? where id = ?;";
    private static final String SQL_ORDERS = "select * from orders;";
    private static final String SQL_PRODUCT_BY_ID = "select * from products where id = ?;";
    private static final String SQL_PRODUCTS = "select * from products;";
    private static final String SQL_INSERT_PRODUCT = "insert into products (`name`,`pricePerKg`,`amountInStockInKg`) values(?,?,?)";
    private static final String SQL_UPDATE_PRODUCT = "update products set name = ?, pricePerKg = ?, amountInStockInKg = ? where id = ?;";
    private static final String SQL_PRODUCT_SUBSTRACT_STOCK = "update products set amountInStockInKg = ? where id = ?;";
    private static final String SQL_TIER_BY_ID = "select * from tiers where id = ?;";
    private static final String SQL_TIERS = "select * from tiers;";
    private static final String SQL_INSERT_TIER = "insert into tiers (`name`, `monthlyLimitInKg`, `additionalDisposalPerKg`, `freePickUps`, `pickupFeePerKg`, `paymentPerMonth`) values(?,?,?,?,?,?);";
    private static final String SQL_UPDATE_TIER = "update tiers set name = ?, monthlyLimitInKg = ?, additionalDisposalPerkg = ?, freePickUps = ?, pickupFeePerKg = ?, paymentPerMonth = ? where id = ?;";
    private static final String SQL_CUSTOMER_BY_ID = "select customers.id, customers.name, customers.address, tiers.id, tiers.name, tiers.monthlyLimitInKg, tiers.additionalDisposalPerKg, tiers.freePickUps, tiers.pickupFeePerKg, tiers.paymentPerMonth, customers.garbageDisposed, customers.amountLeftToDispose, customers.pickupsLeft from customers inner join tiers on customers.tierId = tiers.id where customers.id = ?;";
    private static final String SQL_CUSTOMERS = "select customers.id, customers.name, customers.address, tiers.id, tiers.name, tiers.monthlyLimitInKg, tiers.additionalDisposalPerKg, tiers.freePickUps, tiers.pickupFeePerKg, tiers.paymentPerMonth, customers.garbageDisposed, customers.amountLeftToDispose, customers.pickupsLeft from customers inner join tiers on customers.tierId = tiers.id;";
    private static final String SQL_CUSTOMERS_BY_NAME = "select customers.id, customers.name, customers.address, tiers.id, tiers.name, tiers.monthlyLimitInKg, tiers.additionalDisposalPerKg, tiers.freePickUps, tiers.pickupFeePerKg, tiers.paymentPerMonth, customers.garbageDisposed, customers.amountLeftToDispose, customers.pickupsLeft from customers inner join tiers on customers.tierId = tiers.id where customers.name = ?;";
    private static final String SQL_INSERT_CUSTOMER = "insert into customers (`name`, `address`, `tierId`, `garbageDisposed`) values(?,?,?,0);";
    private static final String SQL_UPDATE_CUSTOMER = "update customers set name = ?, address = ?, tierId = ?, amountLeftToDispose = ?, pickupsLeft = ? where id = ?;";
    private static final String SQL_UPDATE_CUSTOMER_DISPOSED_GARBAGE = "update customers set garbageDisposed = ?, amountLeftToDispose = ? where id = ?";
    private static final String SQL_UPDATE_CUSTOMERS_REMOVE_PICKUPS_LEFT = "update customers set pickupsLeft = ? where id = ?;";

    private static final String SQL_PICKUP_BY_ID = "select * from pickups where id = ?;";
    private static final String SQL_PICKUPS_BY_CUSTOMER = "select * from pickups where customerId = ?;";
    private static final String SQL_INSERT_PICKUP = "insert into pickups (`customerId`, `dateOfRequest`, `requestedPickupDate`, `status`) values(?,?,?,?);";
    private static final String SQL_PICKUPS = "select * from pickups;";
    public static final String SQL_UPDATE_PICKUP_STATUS = "update pickups set status = ? where id = ?";
    private static final String TIER_ID = "tiers.id";
    private static final String TIER_NAME = "tiers.name";
    private static final String TIER_PICKUP_FEE = "tiers.pickupFeePerKg";
    private static final String TIER_MONTHLY_LIMIT = "tiers.monthlyLimitInKg";
    private static final String TIER_ADDITIONAL_DISPOSAL = "tiers.additionalDisposalPerKg";
    private static final String TIER_PAYMENT_PER_MONTH = "tiers.paymentPerMonth";
    private static final String CUSTOMER_ID = "customerId";
    private static final String CUSTOMER_ADDRESS = "address";
    private static final String CUSTOMER_GARBAGE_DISPOSED = "garbageDisposed";
    private static final String CUSTOMER_LEFT_AMOUNT_TO_DISPOSE = "amountLeftToDispose";
    private static final String CUSTOMER_FREE_PICKUPS_LEFT = "pickupsLeft";
    private static final String STATUS = "status";
    private static final String REQUESTED_PICKUP_DATE = "requestedPickupDate";
    private static final String DATE_OF_REQUEST = "dateOfRequest";
    private final Server dbWebConsole;
    private final String username;
    private final String password;
    private final String url;

    public MarsH2Repository(String url, String username, String password, int console) {
        try {
            this.username = username;
            this.password = password;
            this.url = url;
            this.dbWebConsole = Server.createWebServer(
                    "-ifNotExists",
                    "-webPort", String.valueOf(console)).start();
            LOGGER.log(Level.INFO, "Database web console started on port: {0}", console);
            this.generateData();
        } catch (SQLException ex) {
            LOGGER.log(Level.SEVERE, "DB configuration failed", ex);
            throw new RepositoryException("Could not configure MarsH2repository");
        }
    }

    public Quote getQuote(int id) {
        try (
                Connection conn = getConnection();
                PreparedStatement stmt = conn.prepareStatement(SQL_QUOTA_BY_ID)
        ) {
            stmt.setInt(1, id);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return new Quote(rs.getInt("id"), rs.getString("quote"));
                } else {
                    return null;
                }
            }
        } catch (SQLException ex) {
            LOGGER.log(Level.SEVERE, "Failed to get quote.", ex);
            throw new RepositoryException("Could not get quote.");
        }
    }

    public Quote insertQuote(String quoteValue) {
        try (Connection conn = getConnection();
             PreparedStatement stmt = conn.prepareStatement(SQL_INSERT_QUOTE, Statement.RETURN_GENERATED_KEYS)) {

            stmt.setString(1, quoteValue);

            int affectedRows = stmt.executeUpdate();

            if (affectedRows == 0) {
                throw new SQLException("Creating user failed, no rows affected.");
            }

            Quote quote = new Quote(quoteValue);
            try (ResultSet generatedKeys = stmt.getGeneratedKeys()) {
                if (generatedKeys.next()) {
                    quote.setId(generatedKeys.getInt(1));
                    return quote;
                }
                else {
                    throw new SQLException("Creating quote failed, no ID obtained.");
                }
            }
        } catch (SQLException ex) {
            LOGGER.log(Level.SEVERE, "Failed to create quote.", ex);
            throw new RepositoryException("Could not create quote.");
        }
    }

    public Quote updateQuote(int quoteId, String quote) {
        try (Connection conn = getConnection();
             PreparedStatement stmt = conn.prepareStatement(SQL_UPDATE_QUOTE)) {

            stmt.setString(1, quote);
            stmt.setInt(2, quoteId);
            stmt.executeUpdate();
            return new Quote(quoteId, quote);
        } catch (SQLException ex) {
            LOGGER.log(Level.SEVERE, "Failed to update quote.", ex);
            throw new RepositoryException("Could not update quote.");
        }
    }

    public void deleteQuote(int quoteId) {
        try (Connection conn = getConnection();
             PreparedStatement stmt = conn.prepareStatement(SQL_DELETE_QUOTE)) {

            stmt.setInt(1, quoteId);
            stmt.execute();
        } catch (SQLException ex) {
            LOGGER.log(Level.SEVERE, "Failed to delete quote.", ex);
            throw new RepositoryException("Could not delete quote.");
        }
    }

    public void cleanUp() {
        if (dbWebConsole != null && dbWebConsole.isRunning(false))
            dbWebConsole.stop();

        try {
            Files.deleteIfExists(Path.of("./db-08.mv.db"));
            Files.deleteIfExists(Path.of("./db-08.trace.db"));
        } catch (IOException e) {
            LOGGER.log(Level.SEVERE, "Database cleanup failed.", e);
            throw new RepositoryException("Database cleanup failed.");
        }
    }

    public void generateData() {
        cleanUp();
        try {
            executeScript("db-create.sql");
            executeScript("db-populate.sql");
        } catch (IOException | SQLException ex) {
            LOGGER.log(Level.SEVERE, "Execution of database scripts failed.", ex);
        }
    }

    private void executeScript(String fileName) throws IOException, SQLException {
        String createDbSql = readFile(fileName);
        try (
                Connection conn = getConnection();
                PreparedStatement stmt = conn.prepareStatement(createDbSql);
        ) {
            stmt.executeUpdate();
        }
    }

    private String readFile(String fileName) throws IOException {
        InputStream inputStream = getClass().getClassLoader().getResourceAsStream(fileName);
        if (inputStream == null)
            throw new RepositoryException("Could not read file: " + fileName);

        return new String(inputStream.readAllBytes(), StandardCharsets.UTF_8);
    }

    public Connection getConnection() throws SQLException {
        return DriverManager.getConnection(url, username, password);
    }

    public Order insertOrder(int productId, int customerId, int amountInKg) {
        try (
                Connection conn = getConnection();
                PreparedStatement stmt = conn.prepareStatement(SQL_INSERT_ORDER);
        ) {
            Order order = new Order(getCustomer(customerId), getProduct(productId), amountInKg);
            stmt.setInt(1, productId);
            stmt.setInt(2, customerId);
            stmt.setString(3, order.getTrackNumber());
            stmt.setString(4, order.getOrderStatus().toString());
            stmt.setString(5, order.getDate());
            stmt.execute();
            return order;
        } catch (SQLException ex) {
            LOGGER.log(Level.SEVERE, "Failed to insert order.", ex);
            throw new RepositoryException("Could not insert order.");
        }
    }

    public Order getOrder(int orderId) {
        try (Connection conn = getConnection();
        PreparedStatement stmt = conn.prepareStatement(SQL_ORDER_BY_ID)
        ) {
            stmt.setInt(1, orderId);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return new Order(rs.getInt("id"), getCustomer(rs.getInt(CUSTOMER_ID)), getProduct(rs.getInt("productId")), rs.getInt("amountInKg"), rs.getString("trackNumber"), Status.valueOf(rs.getString("orderStatus")), rs.getString("orderDate"));
                }
                return null;
            }

        } catch (SQLException ex) {
            LOGGER.log(Level.SEVERE, "Failed to get order.", ex);
            throw new RepositoryException("Could not get order.");
        }
    }

    public Map<Integer, Order> getOrders() {
        try (Connection conn = getConnection();
        PreparedStatement stmt = conn.prepareStatement(SQL_ORDERS)) {
            try (ResultSet rs = stmt.executeQuery()) {
                Map<Integer, Order> orders = new HashMap<>();
                while (rs.next()) {
                    orders.put(rs.getInt("id"),new Order(rs.getInt("id"), getCustomer(rs.getInt(CUSTOMER_ID)), getProduct(rs.getInt("productId")), rs.getInt("amountInKg"), rs.getString("trackNumber"), Status.valueOf(rs.getString("orderStatus")), rs.getString("orderDate")));
                }
                return orders;
            }
        } catch (SQLException ex) {
            LOGGER.log(Level.SEVERE, "Failed to get orders", ex);
            throw new RepositoryException("Could not get order");
        }
    }
    public Order updateOrderStatus(int orderId, Status orderStatus) {
        try (Connection conn = getConnection();
        PreparedStatement stmt = conn.prepareStatement(SQL_UPDATE_ORDER_STATUS);) {
            stmt.setString(1, orderStatus.toString());
            stmt.setInt(2, orderId);
            stmt.execute();
            return getOrder(orderId);
        } catch (SQLException ex) {
            LOGGER.log(Level.SEVERE, "Failed to update order", ex);
            throw new RepositoryException("Could not update order");
        }
    }

    public Product getProduct(int productId) {
        try (Connection conn = getConnection();
        PreparedStatement stmt = conn.prepareStatement(SQL_PRODUCT_BY_ID)) {
            stmt.setInt(1, productId);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return new Product(rs.getInt("id"), rs.getString("name"), rs.getDouble("pricePerKg"), rs.getInt("amountInStockInKg"));
                }
                return null;
            }
        } catch (SQLException ex) {
            LOGGER.log(Level.SEVERE, "Failed to get product");
            throw new RepositoryException("Could not get product");
        }
    }

    public Map<Integer,Product> getProducts() {
        try (Connection conn = getConnection();
        PreparedStatement stmt = conn.prepareStatement(SQL_PRODUCTS)) {
            try (ResultSet rs = stmt.executeQuery()) {
                Map<Integer,Product> products = new HashMap<>();
                while (rs.next()) {
                    products.put(rs.getInt("id"),new Product(rs.getInt("id"), rs.getString("name"), rs.getDouble("pricePerKg"), rs.getInt("amountInStockInKg")));
                }
                return products;
            }
        } catch (SQLException ex) {
            LOGGER.log(Level.SEVERE, "Failed to get products");
            throw new RepositoryException("Could not get products");
        }
    }

    public Product insertProduct(String name, double pricePerKg, double amountInStockInKg) {
        try (Connection conn = getConnection();
        PreparedStatement stmt = conn.prepareStatement(SQL_INSERT_PRODUCT, Statement.RETURN_GENERATED_KEYS)) {
            stmt.setString(1, name);
            stmt.setDouble(2, pricePerKg);
            stmt.setDouble(3, amountInStockInKg);
            int affectedRows = stmt.executeUpdate();
            if (affectedRows == 0) {
                throw new SQLException("Creating product failed, no rows affected");
            }
            Product product = new Product(name, pricePerKg, amountInStockInKg);

            try (ResultSet generatedKeys = stmt.getGeneratedKeys()) {
                if(generatedKeys.next()) {
                    product.setId(generatedKeys.getInt(1));
                    return product;
                }
                throw new SQLException("Adding product failed, no ID obtained");
            }

        } catch (SQLException ex) {
            LOGGER.log(Level.SEVERE, "Failed to insert product");
            throw new RepositoryException("Could not insert product");
        }
    }

    public Product updateProduct(int productId, String name, double pricePerKg, double amountInStockInKg) {
        try (Connection conn = getConnection();
        PreparedStatement stmt = conn.prepareStatement(SQL_UPDATE_PRODUCT);) {
            stmt.setString(1, name);
            stmt.setDouble(2, pricePerKg);
            stmt.setDouble(3, amountInStockInKg);
            stmt.setInt(4, productId);
            stmt.executeUpdate();
            return new Product(productId, name, pricePerKg, amountInStockInKg);
        } catch (SQLException ex) {
            LOGGER.log(Level.SEVERE, "Failed to update product.", ex);
            throw new RepositoryException("Could not update product.");
        }
    }

    public void substractStock(int productId, double amount) {
        try (Connection conn = getConnection();
             PreparedStatement stmt = conn.prepareStatement(SQL_PRODUCT_SUBSTRACT_STOCK);) {
            Product product = getProduct(productId);

            if (product.getAmountInStockInKg() == 0 || product.getAmountInStockInKg() < amount) {
                throw new RepositoryException("There is not enough stock for this molecule");
            }
            product.substractStock(amount);
            stmt.setDouble(1, product.getAmountInStockInKg());
            stmt.setInt(2, productId);
            stmt.executeUpdate();
        } catch (SQLException ex) {
            LOGGER.log(Level.SEVERE, "Failed to substract stock.", ex);
            throw new RepositoryException("Could not substract stock.");
        }
    }

    public Tier getTier(int tierId) {
        try (Connection conn = getConnection();
        PreparedStatement stmt = conn.prepareStatement(SQL_TIER_BY_ID)) {
            stmt.setInt(1, tierId);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return new Tier(rs.getInt("id"), rs.getString("name"), rs.getInt("monthlyLimitInKg"), rs.getDouble("additionalDisposalPerKg"), rs.getInt("freePickUps"), rs.getDouble("pickupFeePerKg"),rs.getDouble("paymentPerMonth"));
                }
                return null;
            }
        } catch (SQLException ex) {
            LOGGER.log(Level.SEVERE, "Failed to get tier");
            throw new RepositoryException("Could not get tier");
        }
    }

    public Map<Integer, Tier> getTiers() {
        try (Connection conn = getConnection();
        PreparedStatement stmt = conn.prepareStatement(SQL_TIERS);) {
            try (ResultSet rs = stmt.executeQuery()) {
                Map<Integer, Tier> tiers = new HashMap<>();
                while (rs.next()) {
                    tiers.put(rs.getInt("id"), new Tier(rs.getInt("id"), rs.getString("name"), rs.getInt("monthlyLimitInKg"), rs.getDouble("additionalDisposalPerKg"), rs.getInt("freePickUps"), rs.getDouble("pickupFeePerKg"),rs.getDouble("paymentPerMonth")));
                }
                return tiers;
            }
        } catch (SQLException ex) {
            LOGGER.log(Level.SEVERE, "Failed to get tiers", ex);
            throw new RepositoryException("Could not get tiers");
        }
    }

    public Tier insertTier(String name, int monthlyLimitInKg, double additionalDisposalPerKg, int freePickUps, double pickupFeePerKg, double paymentPerMonth) {
        try (Connection conn = getConnection();
        PreparedStatement stmt = conn.prepareStatement(SQL_INSERT_TIER, Statement.RETURN_GENERATED_KEYS);) {
            stmt.setString(1, name);
            stmt.setInt(2,monthlyLimitInKg);
            stmt.setDouble(3, additionalDisposalPerKg);
            stmt.setInt(4, freePickUps);
            stmt.setDouble(5, pickupFeePerKg);
            stmt.setDouble(6, paymentPerMonth);
            int affectedRows = stmt.executeUpdate();
            if (affectedRows == 0) {
                throw new SQLException("Creating tier failed, no rows affected");
            }
            Tier tier = new Tier(name, monthlyLimitInKg, additionalDisposalPerKg, freePickUps, pickupFeePerKg, paymentPerMonth);
            try(ResultSet rs = stmt.getGeneratedKeys()) {
                if (rs.next()) {
                    tier.setId(rs.getInt("id"));
                    return tier;
                }
                throw new SQLException("Adding tier failed, no ID obtained");
            }
        } catch (SQLException ex) {
            LOGGER.log(Level.SEVERE, "Failed to create new tier", ex);
            throw new RepositoryException("Could not create tier");
        }
    }

    public Tier updateTier(int tierId, String name, int monthlyLimitInKg, double additionalDisposalPerKg, int freePickUps, double pickupFeePerKg, double paymentPerMonth) {
        try (Connection conn = getConnection();
        PreparedStatement stmt = conn.prepareStatement(SQL_UPDATE_TIER)) {
            stmt.setString(1, name);
            stmt.setInt(2,monthlyLimitInKg);
            stmt.setDouble(3, additionalDisposalPerKg);
            stmt.setInt(4, freePickUps);
            stmt.setDouble(5, pickupFeePerKg);
            stmt.setDouble(6, paymentPerMonth);
            stmt.setInt(7, tierId);
            stmt.executeUpdate();
            return new Tier(tierId, name, monthlyLimitInKg, additionalDisposalPerKg, freePickUps, pickupFeePerKg, paymentPerMonth);
        } catch (SQLException ex) {
            LOGGER.log(Level.SEVERE, "Failed to update tier", ex);
            throw new RepositoryException("Could not update tier");
        }
    }

    public Customer getCustomer(int customerId) {
        try (
                Connection conn = getConnection();
                PreparedStatement stmt = conn.prepareStatement(SQL_CUSTOMER_BY_ID)
        ) {
            stmt.setInt(1, customerId);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return new Customer(rs.getInt("id"), rs.getString("name"), rs.getString(CUSTOMER_ADDRESS), new Tier(rs.getInt(TIER_ID), rs.getString(TIER_NAME), rs.getInt(TIER_MONTHLY_LIMIT), rs.getDouble(TIER_ADDITIONAL_DISPOSAL), rs.getInt(TIER_PICKUP_FEE), rs.getDouble(TIER_PICKUP_FEE),rs.getDouble(TIER_PAYMENT_PER_MONTH)),rs.getDouble(CUSTOMER_GARBAGE_DISPOSED), rs.getDouble(CUSTOMER_LEFT_AMOUNT_TO_DISPOSE), rs.getInt(CUSTOMER_FREE_PICKUPS_LEFT));
                }
                return null;
            }
        } catch (SQLException ex) {
            LOGGER.log(Level.SEVERE, "Failed to get customer.", ex);
            throw new RepositoryException("Could not get customer.");
        }
    }

    public Customer getCustomer(String customerName) {
        try (
                Connection conn = getConnection();
                PreparedStatement stmt = conn.prepareStatement(SQL_CUSTOMERS_BY_NAME)
        ) {
            stmt.setString(1, customerName);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return new Customer(rs.getInt("id"), rs.getString("name"), rs.getString(CUSTOMER_ADDRESS), new Tier(rs.getInt(TIER_ID), rs.getString(TIER_NAME), rs.getInt(TIER_MONTHLY_LIMIT), rs.getDouble(TIER_ADDITIONAL_DISPOSAL), rs.getInt("tiers.freePickUps"), rs.getDouble(TIER_PICKUP_FEE),rs.getDouble(TIER_PAYMENT_PER_MONTH)),rs.getDouble(CUSTOMER_GARBAGE_DISPOSED), rs.getDouble(CUSTOMER_LEFT_AMOUNT_TO_DISPOSE), rs.getInt(CUSTOMER_FREE_PICKUPS_LEFT));
                }
                return null;
            }
        } catch (SQLException ex) {
            LOGGER.log(Level.SEVERE, "Failed to get customer.", ex);
            throw new RepositoryException("Could not get customer.");
        }
    }

    public Map<Integer, Customer> getCustomers() {
        try (Connection conn = getConnection();
        PreparedStatement stmt = conn.prepareStatement(SQL_CUSTOMERS);) {
            try (ResultSet rs = stmt.executeQuery()) {
                Map<Integer, Customer> customerMap = new HashMap<>();
                while (rs.next()) {
                    customerMap.put(rs.getInt("id"), new Customer(rs.getInt("id"), rs.getString("name"), rs.getString(CUSTOMER_ADDRESS), new Tier(rs.getInt(TIER_ID), rs.getString(TIER_NAME), rs.getInt(TIER_MONTHLY_LIMIT), rs.getDouble(TIER_ADDITIONAL_DISPOSAL), rs.getInt("tiers.freePickUps"), rs.getDouble(TIER_PICKUP_FEE),rs.getDouble(TIER_PAYMENT_PER_MONTH)),rs.getDouble(CUSTOMER_GARBAGE_DISPOSED), rs.getDouble(CUSTOMER_LEFT_AMOUNT_TO_DISPOSE), rs.getInt(CUSTOMER_FREE_PICKUPS_LEFT)));

                }
                return customerMap;
            }
        } catch (SQLException ex) {
            LOGGER.log(Level.SEVERE, "Failed to get customers.", ex);
            throw new RepositoryException("Could not get customers.");
        }
    }

    public Customer insertCustomer(String name, String address) {
        try (Connection conn = getConnection();
        PreparedStatement stmt = conn.prepareStatement(SQL_INSERT_CUSTOMER, Statement.RETURN_GENERATED_KEYS);) {
            stmt.setString(1, name);
            stmt.setString(2, address);
            stmt.setInt(3,0);
            int affectedRows = stmt.executeUpdate();
            if (affectedRows == 0) {
                throw new SQLException("Creating customer failed, no rows affected");
            }
            Customer customer = new Customer(name, address);
            try (ResultSet generatedKeys = stmt.getGeneratedKeys()) {
                if (generatedKeys.next()) {
                    customer.setId(generatedKeys.getInt(1));
                    customer.setTier(getTier(0));
                    return customer;
                }
                throw new SQLException("Adding customer failed, no ID obtained");
            }
        } catch (SQLException ex) {
            LOGGER.log(Level.SEVERE, "Failed to insert customer.", ex);
            throw new RepositoryException("Could not insert customer.");
        }
    }

    public Customer updateCustomer(int customerId, String name, String address, int tierId) {
        try (Connection conn = getConnection();
        PreparedStatement stmt = conn.prepareStatement(SQL_UPDATE_CUSTOMER)) {
            Tier tier = getTier(tierId);
            double monthlyLimit = tier.getMonthlyLimitInkg();
            int freePickupsLeft = tier.getFreePickUps();
            stmt.setString(1, name);
            stmt.setString(2, address);
            stmt.setInt(3, tierId);
            stmt.setDouble(4, monthlyLimit);
            stmt.setInt(5, freePickupsLeft);
            stmt.setInt(6, customerId);
            stmt.executeUpdate();
            return new Customer(customerId, name, address, getTier(tierId), 0, monthlyLimit, freePickupsLeft);
        } catch (SQLException ex) {
            LOGGER.log(Level.SEVERE, "Failed to update customer.", ex);
            throw new RepositoryException("Could not update customer.");
        }
    }

    public Customer updateDisposedGarbage(int customerId, double amountOfGarbage) {
        try(Connection conn = getConnection();
        PreparedStatement stmt = conn.prepareStatement(SQL_UPDATE_CUSTOMER_DISPOSED_GARBAGE)) {
            Customer customer = getCustomer(customerId);
            customer.addGarbage(amountOfGarbage);
            stmt.setDouble(1, customer.getAmountOfGarbageDisposed());
            stmt.setDouble(2, customer.getAmountLeftToDispose());
            stmt.setInt(3, customerId);
            stmt.executeUpdate();
            return getCustomer(customerId);
        } catch (SQLException ex) {
            LOGGER.log(Level.SEVERE, "Failed to update amount of disposed garbage", ex);
            throw new RepositoryException("Could not update amount of diposed garbage.");
        }
    }

    public void updatePickupsLeft(int customerId) {
        try(Connection conn = getConnection();
            PreparedStatement stmt = conn.prepareStatement(SQL_UPDATE_CUSTOMERS_REMOVE_PICKUPS_LEFT)) {
            Customer customer = getCustomer(customerId);
            customer.reduceAmountOfFreePickupsLeft();
            stmt.setDouble(1, customer.getPickupsLeft());
            stmt.setInt(2, customerId);
            stmt.executeUpdate();
        } catch (SQLException ex) {
            LOGGER.log(Level.SEVERE, "Failed to update amount of pickups left", ex);
            throw new RepositoryException("Could not update amount of pickups lefr.");
        }
    }

    public Pickup getPickup(int id) {
        try (Connection conn = getConnection();
        PreparedStatement stmt = conn.prepareStatement(SQL_PICKUP_BY_ID)) {
            stmt.setInt(1, id);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return new Pickup(rs.getInt("id"), getCustomer(rs.getInt(CUSTOMER_ID)), rs.getString(DATE_OF_REQUEST),rs.getString(REQUESTED_PICKUP_DATE), Status.valueOf(rs.getString(STATUS)));
                }
                return null;
            }
        } catch (SQLException ex) {
            LOGGER.log(Level.SEVERE, "Failed to get pickup.", ex);
            throw new RepositoryException("Could not get pickup.");
        }
    }

    public Map<Integer, Pickup> getPickups() {
        try (Connection conn = getConnection();
             PreparedStatement stmt = conn.prepareStatement(SQL_PICKUPS);) {
            try (ResultSet rs = stmt.executeQuery()) {
                Map<Integer, Pickup> pickupMap = new HashMap<>();
                while (rs.next()) {
                    pickupMap.put(rs.getInt("id"), new Pickup(rs.getInt("id"), getCustomer(rs.getInt(CUSTOMER_ID)), rs.getString(DATE_OF_REQUEST),rs.getString(REQUESTED_PICKUP_DATE), Status.valueOf(rs.getString(STATUS))));
                }
                return pickupMap;
            }
        } catch (SQLException ex) {
            LOGGER.log(Level.SEVERE, "Failed to get customers.", ex);
            throw new RepositoryException("Could not get customers.");
        }
    }

    public Map<Integer, Pickup> getPickups(int customerId) {
        try (Connection conn = getConnection();
             PreparedStatement stmt = conn.prepareStatement(SQL_PICKUPS_BY_CUSTOMER);) {
            stmt.setInt(1,customerId);
            try (ResultSet rs = stmt.executeQuery()) {
                Map<Integer, Pickup> pickupMap = new HashMap<>();
                while (rs.next()) {
                    pickupMap.put(rs.getInt("id"), new Pickup(rs.getInt("id"), getCustomer(rs.getInt(CUSTOMER_ID)), rs.getString(DATE_OF_REQUEST),rs.getString(REQUESTED_PICKUP_DATE), Status.valueOf(rs.getString(STATUS))));
                }
                return pickupMap;
            }
        } catch (SQLException ex) {
            LOGGER.log(Level.SEVERE, "Failed to get pickups.", ex);
            throw new RepositoryException("Could not get pickups.");
        }
    }

    public Map<Integer, Pickup> getPickups(String customerName) {
        try (Connection conn = getConnection();
             PreparedStatement stmt = conn.prepareStatement(SQL_PICKUPS_BY_CUSTOMER);) {
            Customer customer = getCustomer(customerName);
            stmt.setInt(1, customer.getId());
            try (ResultSet rs = stmt.executeQuery()) {
                Map<Integer, Pickup> pickupMap = new HashMap<>();
                while (rs.next()) {
                    pickupMap.put(rs.getInt("id"), new Pickup(rs.getInt("id"), getCustomer(rs.getInt(CUSTOMER_ID)), rs.getString(DATE_OF_REQUEST),rs.getString(REQUESTED_PICKUP_DATE), Status.valueOf(rs.getString(STATUS))));
                }
                return pickupMap;
            }
        } catch (SQLException ex) {
            LOGGER.log(Level.SEVERE, "Failed to get pickups.", ex);
            throw new RepositoryException("Could not get pickups.");
        }
    }

    public Pickup insertPickup(int customerId, String requestedPickupDate) {
        try (Connection conn = getConnection();
             PreparedStatement stmt = conn.prepareStatement(SQL_INSERT_PICKUP, Statement.RETURN_GENERATED_KEYS);) {
            Pickup pickup = new Pickup(getCustomer(customerId), requestedPickupDate);
            stmt.setInt(1, customerId);
            stmt.setString(2, pickup.getDateOfRequest());
            stmt.setString(3, requestedPickupDate);
            stmt.setString(4, pickup.getPickupStatus().toString());
            int affectedRows = stmt.executeUpdate();
            if (affectedRows == 0) {
                throw new SQLException("Creating pickup failed, no rows affected");
            }
            try (ResultSet generatedKeys = stmt.getGeneratedKeys()) {
                if (generatedKeys.next()) {
                    pickup.setId(generatedKeys.getInt(1));
                    return pickup;
                }
                throw new SQLException("Adding pickup failed, no ID obtained");
            }
        } catch (SQLException ex) {
            LOGGER.log(Level.SEVERE, "Failed to insert pickup.", ex);
            throw new RepositoryException("Could not insert pickup.");
        }
    }

    public Pickup updatePickupStatus(int id, Status status) {
        try (Connection conn = getConnection();
             PreparedStatement stmt = conn.prepareStatement(SQL_UPDATE_PICKUP_STATUS);) {
            stmt.setString(1, status.toString());
            stmt.setInt(2, id);
            stmt.execute();
            return getPickup(id);
        } catch (SQLException ex) {
            LOGGER.log(Level.SEVERE, "Failed to update pickup", ex);
            throw new RepositoryException("Could not update pickup");
        }
    }
}
