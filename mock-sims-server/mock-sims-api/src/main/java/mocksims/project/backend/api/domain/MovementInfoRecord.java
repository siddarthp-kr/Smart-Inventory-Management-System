package mocksims.project.backend.api.domain;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class MovementInfoRecord {
    private String upcNumber;
    private String productName;
    private String movementType;
    private String userEuid;
    private Integer qodBeforeTransaction;
    private Integer qomBeforeTransaction;
    private LocalDateTime actionTime;
    private Integer quantityChanged;
    private String sourceBucket;
    private String reasonCode;
    private BigDecimal originalPrice;
    private BigDecimal newPrice;
}