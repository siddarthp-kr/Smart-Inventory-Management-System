package mocksims.project.backend.api.domain;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class OrderHistoryRequest {
    String storeNumber;
    String divisionNumber;
}
