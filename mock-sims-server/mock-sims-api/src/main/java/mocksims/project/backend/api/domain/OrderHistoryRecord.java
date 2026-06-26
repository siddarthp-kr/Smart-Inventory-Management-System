package mocksims.project.backend.api.domain;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class OrderHistoryRecord {
    Integer orderId;
    String placedByUserEuid;
    LocalDateTime orderPlacedTime;
    Boolean orderReceived;
    String receivedByUserEuid;
    LocalDateTime orderReceivedTime;
    String upcNumber;
    String productName;
    Integer quantity;
}
