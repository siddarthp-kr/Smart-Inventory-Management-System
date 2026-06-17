package mocksims.project.backend.domain;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class OrderHistoryRecord {
    Integer orderId;
    String userEuid;
    LocalDateTime orderPlacedTime;
    LocalDateTime orderReceivedTime;
    String upcNumber;
    Integer quantity;
}
