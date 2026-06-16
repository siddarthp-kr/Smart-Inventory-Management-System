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
     */
    public Long insertOrderTransactionInfo(String storeNumber, String divisionNumber, String userEuid, LocalDateTime timeOrderPlaced, LocalDateTime timeOrderReceived);
    public void insertProductInventoryInfo(String upcNumber, int quantity, long orderId, LocalDate orderDate, LocalDate expirationDate);
    public String getSubcommodityNumber(String upcNumber);
    public Integer getNumberOfDaysBeforeExpiration(String subcommodityNumber);

}