package mocksims.project.backend.repository;

import org.springframework.stereotype.Repository;

@Repository
public class PlaceOrderRepositoryImpl implements PlaceOrderRepository {
    public boolean updateBohInfo(String storeNumber, String divisionNumber, String upcNumber, int quantity) {
        return false;
    }
    public boolean updateOrderTransactionInfo(String storeNumber, String divisionNumber, String userEuid){
        return false;
    }
    public boolean updateProductInventoryInfo(String upcNumber, int quantity){
        return false;
    }
}