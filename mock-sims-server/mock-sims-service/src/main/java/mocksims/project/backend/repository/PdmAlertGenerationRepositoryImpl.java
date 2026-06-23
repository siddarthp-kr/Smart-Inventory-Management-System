package mocksims.project.backend.repository;

import mocksims.project.backend.api.domain.PdmAlertInfo;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class PdmAlertGenerationRepositoryImpl implements PdmAlertGenerationRepository{

    @Override
    public List<PdmAlertInfo> getPdmAlertsInfo(){
        //get all the information for each alert (before checking whether it is eligible)
        //only gets the most recent row for each upc
        return null;
    }

    public void insertNewAlerts(List<PdmAlertInfo> alerts){
        //generate alerts and mark the corresponding inventory rows as inactive
    }

}
