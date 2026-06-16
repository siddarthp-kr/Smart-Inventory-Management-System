package mocksims.project.backend.api.domain;

import lombok.*;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AddItemRequest {
    String storeNumber;
    String divisionNumber;
    String upcNumber;
    String subcommodityNumber;
    String departmentNumber;
    String productName;
    double standardPrice;
    int firstMarkdownPercent;
    boolean canBeMarkedDown;
    int daysBeforeExpToMD;
    int daysBeforeExpToRFI;
    int daysAfterOrderToSetExp;

}
