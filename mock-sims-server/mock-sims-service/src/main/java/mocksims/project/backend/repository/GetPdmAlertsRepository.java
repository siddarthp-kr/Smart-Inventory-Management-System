package mocksims.project.backend.repository;

import mocksims.project.backend.api.domain.GetPdmAlertRecord;

import java.util.List;

public interface GetPdmAlertsRepository {
    public List<GetPdmAlertRecord> getPdmAlerts(String storeNumber, String divisionNumber);

    Integer getPdmAlertCount(String storeNumber, String divisionNumber);
}
