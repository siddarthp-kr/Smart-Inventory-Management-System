package mocksims.project.backend.service;

import mocksims.project.backend.api.domain.GetPdmAlertRecord;
import mocksims.project.backend.repository.GetPdmAlertsRepository;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class GetPdmAlertsServiceImpl implements GetPdmAlertsService{

    private final GetPdmAlertsRepository getPdmAlertsRepository;

    public GetPdmAlertsServiceImpl (GetPdmAlertsRepository getPdmAlertsRepository){
        this.getPdmAlertsRepository = getPdmAlertsRepository;
    }

    @Override
    public List<GetPdmAlertRecord> getPdmAlerts(String storeNumber, String divisionNumber){
        return getPdmAlertsRepository.getPdmAlerts(storeNumber, divisionNumber);
    }
}
