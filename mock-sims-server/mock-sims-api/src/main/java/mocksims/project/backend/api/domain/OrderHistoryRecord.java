package mocksims.project.backend.api.domain;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class OrderHistoryRecord {
    Integer orderId;
    String userEuid;
    LocalDate orderPlacedDate;
    String upcNumber;
    String productName;
    Integer quantity;
}
