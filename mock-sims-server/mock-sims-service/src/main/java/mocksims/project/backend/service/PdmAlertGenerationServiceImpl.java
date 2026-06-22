package mocksims.project.backend.service;

import mocksims.project.backend.repository.PdmAlertGenerationRepository;
import org.springframework.stereotype.Service;

@Service
public class PdmAlertGenerationServiceImpl implements PdmAlertGenerationService{

    private final PdmAlertGenerationRepository pdmAlertGenerationRepository;

    public PdmAlertGenerationServiceImpl(PdmAlertGenerationRepository pdmAlertGenerationRepository){
        this.pdmAlertGenerationRepository = pdmAlertGenerationRepository;
    }

    public void generatePdmAlerts(){

        //first, query for all the active alerts in inventory table,
        //second, check which ones are still valid based on boh,
        //third, check which ones are valid based on the markdown rules (actually, check if this is already done by the order api)
        //mark the inventory rows as inactive
        //for each inventory row,

    }

}
