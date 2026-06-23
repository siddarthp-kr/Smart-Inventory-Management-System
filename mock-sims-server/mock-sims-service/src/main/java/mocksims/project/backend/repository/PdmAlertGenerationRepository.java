package mocksims.project.backend.repository;

import mocksims.project.backend.api.domain.PdmAlertInfo;

import java.util.List;

public interface PdmAlertGenerationRepository {

    public List<PdmAlertInfo> getPdmAlertsInfo();

    public void insertNewAlerts(List<PdmAlertInfo> alerts);

}
