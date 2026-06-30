package mocksims.project.backend.api.domain;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class GetPdmAlertRecord {
    Integer alertId;
    String departmentNumber;
    String upcNumber;
    LocalDate expirationDate;
    LocalDate mdBeforeDate;
    LocalDate rfiBeforeDate;
}
