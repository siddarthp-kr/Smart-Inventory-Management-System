package mocksims.project.backend.repository;

import mocksims.project.backend.api.domain.PdmAlertInfoRecord;

import java.util.List;

public interface PdmAlertGenerationRepository {

    public List<PdmAlertInfoRecord> getPdmAlertsInfo();

    public void insertNewAlerts(List<PdmAlertInfoRecord> alerts);

}
