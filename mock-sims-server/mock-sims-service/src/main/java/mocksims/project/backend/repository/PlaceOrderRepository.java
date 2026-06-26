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
     * Creates a row in the PRODUCT_INVENTORY_INFO table that records inventory details for a specific
     * ordered product, linking it back to its corresponding order transaction.
     * Inventory rows are creates as inactive and becomes active once order is received
     * @param orderId
     *      The primary key (product_order_id) of the associated row in the ORDER_TRANSACTION_INFO table
     * @param orderDate
     *      The date on which the order was placed
     * @param items
     *      List containing item information for each item being ordered
     * @throws DataAccessException
     *      If an error occurs while accessing the database
     */
    public void insertProductInventoryInfo(long orderId, LocalDate orderDate, List<PlaceOrderItem> items);

    /**
     * Creates rows in ORDER_MOVEMENT_TRANSACTIONS for each ordered UPC.
     * qod_before_transaction is NULL since inventory is not received yet
     * @param orderId
     *      The date which the order is placed
     * @param items
     *      List containing item information for each item being ordered
     */
    public void insertOrderMovementTransactions(long orderId, List<PlaceOrderItem> items);

    /**
     * Retrieves the subcommodity number associated with a given product from the PRODUCT_BASIC_INFO table.
     * @param upcNumber
     *      The identifier for the item whose subcommodity number is being requested
     * @return
     *      The subcommodity number corresponding to the provided upcNumber
     * @throws DataAccessException
     *      If an error occurs while accessing the database
     */
    public String getSubcommodityNumber(String upcNumber);

    /**
     * Retrieves the number of days after an order is placed before the product should be marked as expired,
     * based on the markdown rules for the provided subcommodity from the MARKDOWN_RULES table.
     * Expiration date will be assigned once received
     * @param subcommodityNumber
     *      The subcommodity number whose markdown expiration rule is being requested
     * @return
     *      The number of days after the order date before the product is set to expire
     * @throws org.springframework.dao.IncorrectResultSizeDataAccessException
     *      If more than one row is returned for the provided subcommodityNumber
     * @throws DataAccessException
     *      If an error occurs while accessing the database
     */
    public Integer getNumberOfDaysBeforeExpiration(String subcommodityNumber);

}