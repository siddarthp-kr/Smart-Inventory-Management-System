package mocksims.project.backend.repository;

import mocksims.project.backend.api.domain.ReceiveOrderItemRecord;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

public interface ReceiveOrderRepository {

    Boolean getOrderReceivedStatus(String storeNumber, String divisionNumber, Long orderId);

    List<ReceiveOrderItemRecord> getOrderItems(Long orderId);

    Integer getQodNumber(String storeNumber, String divisionNumber, String upcNumber);

    void incrementQod(String storeNumber, String divisionNumber, String upcNumber, Integer quantity);

    void updateQodBeforeTransaction(Long orderId, String upcNumber, Integer qodBeforeTransaction);

    String getSubcommodityNumber(String upcNumber);

    Integer getNumberOfDaysBeforeExpiration(String subcommodityNumber);

    void insertProductInventoryInfo(Long orderId, String upcNumber, Integer quantity, LocalDate expirationDate, LocalDate orderDate, Boolean isActive);

    void updateOrderReceived(String storeNumber, String divisionNumber, Long orderId, String receivedByUserEuid, LocalDateTime orderReceivedTime);
}