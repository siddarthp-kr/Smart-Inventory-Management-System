package mocksims.project.backend.api.domain;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class GetPdmAlertsResponse {
    String responseMessage;
    List<GetPdmAlertRecord> pdmAlerts;
}
