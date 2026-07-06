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
    String orderStatus;
    String actionByUserEuid;
    LocalDateTime orderActionTime;
    String upcNumber;
    String productName;
    Integer quantity;
}