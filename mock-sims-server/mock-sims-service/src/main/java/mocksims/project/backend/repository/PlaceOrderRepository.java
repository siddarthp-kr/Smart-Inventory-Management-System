package mocksims.project.backend.repository;

public interface PlaceOrderRepository {

    //change this so that it will return a ResponseEntity or SuccessResponse or something
    public void updateBohInfo(String storeNumber, String divisionNumber, String upcNumber, int quantity);
    public void updateOrderTransactionInfo(String storeNumber, String divisionNumber, String userEuid);
    public void updateProductInventoryInfo(String upcNumber, int quantity);

}