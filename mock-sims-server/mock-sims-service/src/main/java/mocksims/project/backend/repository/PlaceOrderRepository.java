package mocksims.project.backend.repository;

public interface PlaceOrderRepository {

    //change this so that it will return a ResponseEntity or SuccessResponse or something
    public boolean updateBohInfo(String storeNumber, String divisionNumber, String upcNumber, int quantity);
    public boolean updateOrderTransactionInfo(String storeNumber, String divisionNumber, String userEuid);
    public boolean updateProductInventoryInfo(String upcNumber, int quantity);

}