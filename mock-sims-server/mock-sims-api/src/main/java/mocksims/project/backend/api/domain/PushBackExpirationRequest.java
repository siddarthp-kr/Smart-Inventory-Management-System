package mocksims.project.backend.api.domain;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class PushBackExpirationRequest {
    Integer alertId;
    LocalDate newExpirationDate;
    String userEuid;
    String storeNumber;
    String divisionNumber;
    String upcNumber;
}
