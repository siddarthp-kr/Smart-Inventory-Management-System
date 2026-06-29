package mocksims.project.backend.api.domain;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class GetMarkdownRuleRecord {
    String subcommodityNumber;
    Integer firstMarkdownPercent;
    Boolean canBeMarkedDown;
    Integer daysBeforeExpToMD;
    Integer daysBeforeExpToRFI;
    Integer daysAfterOrderToSetExp;
}