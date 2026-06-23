package mocksims.project.backend.api.domain;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class PdmAlertInfoRecord {
    Integer productOrderId;
    String storeNumber;
    String divisionNumber;
    String departmentNumber;
    String upcNumber;
    Integer quantity;
    LocalDate expirationDate;
    Integer firstMarkdownPercent;
    Integer daysBeforeExpToMD;
    Integer daysBeforeExpToRFI;
    Integer qodNumber;
    Integer qomNumber;
}
