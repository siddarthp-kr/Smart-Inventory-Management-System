package mocksims.project.backend.service;

import mocksims.project.backend.api.domain.GetPdmAlertRecord;

import java.util.List;

public interface GetPdmAlertsService {

    public List<GetPdmAlertRecord> getPdmAlerts(String storeNumber, String divisionNumber);

}
