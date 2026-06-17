package mocksims.project.backend.repository;

import java.time.LocalDate;
import java.time.LocalDateTime;

public interface PlaceOrderRepository {

    //change this so that it will return a ResponseEntity or SuccessResponse or something

    /**
     * Updates the QOD_NUMBER number in the PRODUCT_BOH_INFO table when an item order is placed.
     * @param storeNumber
     *      The store number from which the order was placed
     * @param divisionNumber
     *      The division number from which the order was placed
     * @param upcNumber
     *      The identifier for the item that was ordered
     * @param quantity
     *      The number of units of the item that were ordered
     * @throws RowNotFoundException
     *      If no matching BOH information record exists for the given upcNumber, storeNumber, and divisionNumber
     * @throws DataAccessException
     *      If an error occurs while accessing the database
     */
    public void updateBohInfo(String storeNumber, String divisionNumber, String upcNumber, int quantity);

    /**
     * Creates a row in the ORDER_TRANSACTION_INFO table which contains information about the order transaction.
     * @param storeNumber
     *      The store number from which the order was placed
     * @param divisionNumber
     *      divisionNumber
     * @param userEuid
     *      EUID of the user who placed the order
     * @param timeOrderPlaced
     *      Time when the order was placed
     * @param timeOrderReceived
     *      The time when the order was received
     * @return
     *      The primary key (product_order_id) of the row created in ORDER_TRANSACTION_TABLE
     * @throws IllegalStateException
     *      If the generated product_order_id could not be retrieved after the insert
     * @throws DataAccessException
     *      If an error occurs while accessing the database
     */
    public Long insertOrderTransactionInfo(String storeNumber, String divisionNumber, String userEuid, LocalDateTime timeOrderPlaced, LocalDateTime timeOrderReceived);

    /**
     * Creates a row in the PRODUCT_INVENTORY_INFO table that records inventory details for a specific
     * ordered product, linking it back to its corresponding order transaction.
     * @param upcNumber
     *      The identifier for the item that was ordered
     * @param quantity
     *      The number of units of the item that were ordered
     * @param orderId
     *      The primary key (product_order_id) of the associated row in the ORDER_TRANSACTION_INFO table
     * @param orderDate
     *      The date on which the order was placed
     * @param expirationDate
     *      The date on which the ordered product is set to expire
     * @param orderIsActive
     *      Whether or not the record should have a PDM alert created for it
     * @throws DataAccessException
     *      If an error occurs while accessing the database
     */
    public void insertProductInventoryInfo(String upcNumber, int quantity, long orderId, LocalDate orderDate, LocalDate expirationDate, boolean orderIsActive);

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