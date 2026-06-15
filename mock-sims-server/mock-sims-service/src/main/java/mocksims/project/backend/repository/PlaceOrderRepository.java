package mocksims.project.backend.repository;

import java.time.LocalDateTime;

public interface PlaceOrderRepository {

    //change this so that it will return a ResponseEntity or SuccessResponse or something
    public void updateBohInfo(String storeNumber, String divisionNumber, String upcNumber, int quantity);
    public long updateOrderTransactionInfo(String storeNumber, String divisionNumber, String userEuid, LocalDateTime timeOrderPlaced, LocalDateTime timeOrderReceived);
    public void updateProductInventoryInfo(String upcNumber, int quantity, long orderId);

}