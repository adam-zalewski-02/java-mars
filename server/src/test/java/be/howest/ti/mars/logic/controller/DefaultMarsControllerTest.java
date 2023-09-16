package be.howest.ti.mars.logic.controller;

import be.howest.ti.mars.logic.data.Repositories;
import be.howest.ti.mars.logic.domain.Order;
import be.howest.ti.mars.logic.domain.Pickup;
import be.howest.ti.mars.logic.domain.Quote;
import be.howest.ti.mars.logic.domain.Status;
import io.vertx.core.json.JsonObject;
import org.apache.commons.lang3.StringUtils;
import org.junit.Assert;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;

import java.util.Map;
import java.util.NoSuchElementException;

import static org.junit.jupiter.api.Assertions.*;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class DefaultMarsControllerTest {

    private static final String URL = "jdbc:h2:./db-08";

    @BeforeAll
    void setupTestSuite() {
        Repositories.shutdown();
        JsonObject dbProperties = new JsonObject(Map.of("url", "jdbc:h2:./db-08",
                "username", "",
                "password", "",
                "webconsole.port", 9000));
        Repositories.configure(dbProperties);
    }

    @BeforeEach
    void setupTest() {
        Repositories.getH2Repo().generateData();
    }

    @Test
    void getQuote() {
        // Arrange
        MarsController sut = new DefaultMarsController();

        // Act
        Quote quote = sut.getQuote(0);

        //Assert
        assertTrue(quote != null && StringUtils.isNoneBlank(quote.getValue()));
    }

    @Test
    void deleteQuote() {
        // Arrange
        MarsController sut = new DefaultMarsController();

        // Act
        sut.deleteQuote(0);

        //Assert
        assertThrows(NoSuchElementException.class, () -> sut.getQuote(0));
    }

    @Test
    void updateQuote() {
        // Arrange
        MarsController sut = new DefaultMarsController();
        Quote quote = sut.createQuote("some value");

        // Act
        Quote updatedQuoted = sut.updateQuote(quote.getId(), "new value");

        //Assert
        assertEquals("new value", updatedQuoted.getValue());
    }

    @Test
    void createQuote() {
        // Arrange
        MarsController sut = new DefaultMarsController();

        // Act
        Quote quote = sut.createQuote("new value");

        //Assert
        assertEquals("new value", quote.getValue());
    }

    @Test
    void getQuoteWithUnknownIdThrowsNotFound() {
        // Arrange
        MarsController sut = new DefaultMarsController();

        // Act + Assert
        assertThrows(NoSuchElementException.class, () -> sut.getQuote(-1));
    }

    @Test
    void createQuoteWithEmptyQuoteThrowsIllegalArgument() {
        // Arrange
        MarsController sut = new DefaultMarsController();

        // Act + Assert
        assertThrows(IllegalArgumentException.class, () -> sut.createQuote(""));
    }

    @Test
    void updateQuoteWithWrongIdThrowsIllegalArgument() {
        // Arrange
        MarsController sut = new DefaultMarsController();

        // Act + Assert
        assertThrows(IllegalArgumentException.class, () -> sut.updateQuote(-1, "some quote"));
    }

    @Test
    void updateQuoteWithUnknownIdThrowsNoSuchElementException() {
        // Arrange
        MarsController sut = new DefaultMarsController();

        // Act + Assert
        assertThrows(NoSuchElementException.class, () -> sut.updateQuote(1000, "some quote"));
    }

    @Test
    void updateQuoteWithEmptyQuoteThrowsIllegalArgument() {
        // Arrange
        MarsController sut = new DefaultMarsController();

        // Act + Assert
        assertThrows(IllegalArgumentException.class, () -> sut.updateQuote(1, ""));
    }

    @Test
    void deleteQuoteWithUnknownIdThrowsNotFound() {
        // Arrange
        MarsController sut = new DefaultMarsController();

        // Act + Assert
        assertThrows(NoSuchElementException.class, () -> sut.deleteQuote(-1));
    }

    @Test
    void getOrder() {
        MarsController sut = new DefaultMarsController();
        int id = 1;

        Order order = sut.getOrder(id);
        assertNotNull(order);
    }

    @Test
    void getPickup() {
        MarsController sut = new DefaultMarsController();
        int id = 1;

        Pickup pickup = sut.getPickup(id);

        assertNotNull(pickup);
    }

    @Test
    void getPickups() {
        MarsController sut = new DefaultMarsController();

        Map<Integer, Pickup> pickupMap = sut.getPickups();

        assertNotNull(pickupMap);
    }

    @Test
    void getPickupsByCustomerId() {
        MarsController sut = new DefaultMarsController();

        int customerId = 1;
        Map<Integer, Pickup> pickupMap = sut.getPickups(customerId);

        assertNotNull(pickupMap);
    }

    @Test
    void insertPickup() {
        MarsController sut = new DefaultMarsController();
        int customerId = 1;
        String requestedPickupDate = "2022-01-01";
        Pickup pickup = sut.createPickup(customerId, requestedPickupDate);

        assertEquals(1, pickup.getCustomer().getId());
    }

    @Test
    void updatePickup() {
        MarsController sut = new DefaultMarsController();
        int pickupId = 1;
        Status status = Status.SUCCES;

        Pickup pickup = sut.updatePickup(pickupId,status);
        
        assertEquals("SUCCES", pickup.getPickupStatus().toString());
        assertEquals(1, pickup.getId());
    }
}
