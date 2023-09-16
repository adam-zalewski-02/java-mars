package be.howest.ti.mars.logic.data;

import be.howest.ti.mars.logic.domain.Quote;
import be.howest.ti.mars.logic.domain.Status;
import be.howest.ti.mars.logic.exceptions.RepositoryException;
import io.netty.util.internal.StringUtil;
import io.vertx.core.json.JsonObject;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.sql.SQLException;
import java.util.Map;

class MarsH2RepositoryExceptionsTest {

    private static final String URL = "jdbc:h2:./db-08";

    @Test
    void getH2RepoWithNoDbFails() {
        // Arrange
        Repositories.shutdown();

        // Act + Assert
        Assertions.assertThrows(RepositoryException.class, Repositories::getH2Repo);
    }

    @Test
    void functionsWithSQLExceptionFailsNicely() {
        // Arrange
        int id = 1;
        String name = "name";
        Status status = Status.SUCCES;
        String address = "address";
        int tierId = 1;
        double price = 3.50;
        double amount = 300;
        int limit = 300;

        JsonObject dbProperties = new JsonObject(Map.of("url",URL,
                "username", "",
                "password", "",
                "webconsole.port", 9000 ));
        Repositories.shutdown();
        Repositories.configure(dbProperties);
        MarsH2Repository repo = Repositories.getH2Repo();
        repo.cleanUp();

        // Act + Assert
        Assertions.assertThrows(RepositoryException.class, () -> repo.getQuote(id));
        Assertions.assertThrows(RepositoryException.class, () -> repo.deleteQuote(id));
        Assertions.assertThrows(RepositoryException.class, () -> repo.updateQuote(id, "update"));
        Assertions.assertThrows(RepositoryException.class, () -> repo.getOrder(id));
        Assertions.assertThrows(RepositoryException.class, repo::getOrders);
        Assertions.assertThrows(RepositoryException.class, () -> repo.updateOrderStatus(id, status));
        Assertions.assertThrows(RepositoryException.class, () -> repo.getCustomer(name));
        Assertions.assertThrows(RepositoryException.class, () -> repo.getCustomer(id));
        Assertions.assertThrows(RepositoryException.class, repo::getCustomers);
        Assertions.assertThrows(RepositoryException.class, () -> repo.updateCustomer(id, name, address, tierId));
        Assertions.assertThrows(RepositoryException.class, () -> repo.getPickup(id));
        Assertions.assertThrows(RepositoryException.class, () -> repo.getPickups(name));
        Assertions.assertThrows(RepositoryException.class, () -> repo.getPickups(id));
        Assertions.assertThrows(RepositoryException.class, repo::getPickups);
        Assertions.assertThrows(RepositoryException.class, () -> repo.updatePickupStatus(id, status));
        Assertions.assertThrows(RepositoryException.class, () -> repo.getProduct(id));
        Assertions.assertThrows(RepositoryException.class, repo::getProducts);
        Assertions.assertThrows(RepositoryException.class, () -> repo.updateProduct(id, name, price, amount));
        Assertions.assertThrows(RepositoryException.class, () -> repo.getTier(id));
        Assertions.assertThrows(RepositoryException.class, repo::getTiers);
        Assertions.assertThrows(RepositoryException.class, () -> repo.updateTier(id, name, limit, price, tierId, price, price));
    }


}
