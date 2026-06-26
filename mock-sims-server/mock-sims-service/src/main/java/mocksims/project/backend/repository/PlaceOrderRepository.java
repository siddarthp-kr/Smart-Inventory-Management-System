package mocksims.project.backend.repository;

import mocksims.project.backend.api.domain.PlaceOrderItem;
import org.springframework.dao.DataAccessException;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

public interface PlaceOrderRepository {

    /**
     * Creates a row in the ORDER_TRANSACTION_INFO table which contains information about the order transaction.
     * Records that order has been placed and not received yet
     * @param storeNumber
     *      The store number from which the order was placed
     * @param divisionNumber
     *      divisionNumber
     * @param userEuid
     *      EUID of the user who placed the order
     * @param timeOrderPlaced
     *      Time when the order was placed
     * @return
     *      The primary key (product_order_id) of the row created in ORDER_TRANSACTION_TABLE
     * @throws IllegalStateException
     *      If the generated product_order_id could not be retrieved after the insert
     * @throws DataAccessException
     *      If an error occurs while accessing the database
     */
    public Long insertOrderTransactionInfo(String storeNumber, String divisionNumber, String userEuid, LocalDateTime timeOrderPlaced);


    /**
     * Creates rows in ORDER_MOVEMENT_TRANSACTIONS for each ordered UPC.
     * qod_before_transaction is NULL since inventory is not received yet
     * @param orderId
     *      The date which the order is placed
     * @param items
     *      List containing item information for each item being ordered
     */
    public void insertOrderMovementTransactions(long orderId, List<PlaceOrderItem> items);


}