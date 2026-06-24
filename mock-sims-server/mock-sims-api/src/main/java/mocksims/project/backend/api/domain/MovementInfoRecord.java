package mocksims.project.backend.api.domain;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class MovementInfoRecord {
    String upcNumber;
    String productName;
    String movementType;
    Integer transactionId;
    String userEuid;
    Integer qodBeforeTransaction;
    Integer qomBeforeTransaction;
    LocalDateTime actionTime;
    Integer quantityChanged;
    String reasonCode;
    Double originalPrice;
    Double newPrice;
}
