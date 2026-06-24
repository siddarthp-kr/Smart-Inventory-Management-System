package mocksims.project.backend.service;

import mocksims.project.backend.api.domain.PdmAlertInfoRecord;
import mocksims.project.backend.repository.PdmAlertGenerationRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Service
public class PdmAlertGenerationServiceImpl implements PdmAlertGenerationService{

    private final PdmAlertGenerationRepository pdmAlertGenerationRepository;

    public PdmAlertGenerationServiceImpl(PdmAlertGenerationRepository pdmAlertGenerationRepository){
        this.pdmAlertGenerationRepository = pdmAlertGenerationRepository;
    }

    @Transactional
    public void generatePdmAlerts(){

        List<PdmAlertInfoRecord> potentialAlertsInfo = pdmAlertGenerationRepository.getPdmAlertsInfo();

        List<PdmAlertInfoRecord> eligibleAlertsInfo = filterForEligibleAlerts(potentialAlertsInfo);

        pdmAlertGenerationRepository.insertNewAlerts(eligibleAlertsInfo);

    }

    private List<PdmAlertInfoRecord> filterForEligibleAlerts(List<PdmAlertInfoRecord> potentialAlertsInfo) {
        List<PdmAlertInfoRecord> filteredAlerts = new ArrayList<>();
        List<Integer> orderIdsToSetInactive = new ArrayList<>();

        for(PdmAlertInfoRecord potentialAlert: potentialAlertsInfo){
            boolean alertIsEligible = validateItemCanBeMarkedDown(potentialAlert);
            if(alertIsEligible && !validateItemsStillOnShelf(potentialAlert)){
                alertIsEligible = false;
                orderIdsToSetInactive.add(potentialAlert.getProductOrderId());
            }
            if(alertIsEligible && !validateDateIsEligible(potentialAlert)){
                alertIsEligible = false;
            }
            if(alertIsEligible){
                filteredAlerts.add(potentialAlert);
            }
        }

        pdmAlertGenerationRepository.markInventoryAsInactive(orderIdsToSetInactive);

        return filteredAlerts;
    }

    private boolean validateItemCanBeMarkedDown(PdmAlertInfoRecord potentialAlert){
        return pdmAlertGenerationRepository.getWhetherItemCanBeMarkedDown(potentialAlert.getUpcNumber());
    }

    private boolean validateItemsStillOnShelf(PdmAlertInfoRecord potentialAlert) {
        Integer bohAmount = potentialAlert.getQodNumber() + potentialAlert.getQomNumber(); //pdmAlertGenerationRepository.getItemTotalBoh(potentialAlert);
        Integer totalQuantity = pdmAlertGenerationRepository.getItemTotalQuantity(potentialAlert);

        return totalQuantity < bohAmount + potentialAlert.getQuantity();
    }

    private boolean validateDateIsEligible(PdmAlertInfoRecord potentialAlert) {
        LocalDate markdownDate = potentialAlert.getExpirationDate().minusDays(potentialAlert.getDaysBeforeExpToMD());
        return markdownDate.isEqual(LocalDate.now()) || markdownDate.isBefore(LocalDate.now());
    }

}
