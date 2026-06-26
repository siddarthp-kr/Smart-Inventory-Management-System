package mocksims.project.backend.api.domain;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class RfiItemRequest {
    Integer alertId;
    Integer quantity;
    String userEuid;
    String storeNumber;
    String divisionNumber;
    String upcNumber;
}
