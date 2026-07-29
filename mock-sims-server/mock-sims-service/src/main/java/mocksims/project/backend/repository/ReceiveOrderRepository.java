package mocksims.project.backend.repository;

import mocksims.project.backend.api.domain.ReceiveOrderItemRecord;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

public interface ReceiveOrderRepository {

    String getOrderStatus(String storeNumber, String divisionNumber, Long orderId);

    public List<ReceiveOrderItemRecord> getOrderItems(Long orderId);

    public Map<String, Integer> getQodNumbersByUpc(String storeNumber, String divisionNumber, List<String> upcNumbers
    );

    public Map<String, Integer> getDaysAfterOrderToSetExpByUpc(List<String> upcNumbers
    );

    public void batchUpdateQodBeforeTransaction(Long orderId, Map<String, Integer> qodBeforeTransactionByUpc
    );

    public void batchIncrementQod(String storeNumber, String divisionNumber, List<ReceiveOrderItemRecord> orderItems
    );

    public void batchInsertProductInventoryInfo(Long orderId, List<ReceiveOrderItemRecord> orderItems, Map<String, LocalDate> expirationDateByUpc, LocalDate orderDate
    );

    public void updateOrderStatusToReceived(String storeNumber, String divisionNumber, Long orderId, String actionByUserEuid, LocalDateTime orderActionTime);

    public void updateOrderStatusToCancelled(String storeNumber, String divisionNumber, Long orderId, String actionByUserEuid, LocalDateTime orderActionTime
    );

}