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


/*
        first, query for all the active alerts in inventory table,
        second, check which ones are still valid based on boh,
        third, check which ones are valid based on the markdown rules (actually, check if this is already done by the order api)
        for each inventory row, check the markdown rules and see which ones are eligible to have an alert based on the current date

        do a join to get the following information about each active inventory table row:
             - store number, division number from ORDER_TRANSACTION_INFO
             - upc, quantity, expiration date from PRODUCT_INVENTORY_INFO
             - department_number from PRODUCT_BASIC_INFO
             - first markdown percent, can be marked down, day before to md, days before to rfi from MARKDOWN_RULES - add a where clause to just get the ones that can be marked down
             - The BOH for that UPC from PRODUCT_BOH_INFO

        Store this info in a POJO with the following information:
            Integer productOrderId
            String storeNumber;
            String divisionNumber;
            String departmentNumber;
            String upcNumber;
            Integer quantity;
            LocalDate expirationDate;
            Integer firstMarkdownPercent;
            Integer daysBeforeExpToMD;
            Integer daysBeforeExpToRFI;
            Integer qodNumber;
            Integer qomNumber;

        Notes: only display the most recent alert for each upc
        How we are dividing it up:
            What is completed between the inventory and alerts table
                - Get the most recent inventory item for each upc
                - See if it is eligible based on MD rules
                - See if it is eligible based on BOH

            What is completed between the alerts table and frontend
                - Get active alerts that are for today
                - Calculate markdown_before date and rfi_before date - EDIT API CONTRACT

        Once that is done, check which ones are eligible - do the BOH check and the date check
        If they are eligible, insert a row into the pdm_alerts table with all the information necessary

        Mark the inventory rows as inactive
*/

    }

    private List<PdmAlertInfoRecord> filterForEligibleAlerts(List<PdmAlertInfoRecord> potentialAlertsInfo) {
        List<PdmAlertInfoRecord> filteredAlerts = new ArrayList<>();

        //check whether today is between MD day and expiration date

        //check whether BOH makes sense - EDIT THIS to mark products no longer on the shelves as inactive
        for(PdmAlertInfoRecord potentialAlert: potentialAlertsInfo){
            if(validateDateIsEligible(potentialAlert) && validateItemsStillOnShelf(potentialAlert)){
                filteredAlerts.add(potentialAlert);
            }
        }

        return filteredAlerts;
    }

    private boolean validateItemsStillOnShelf(PdmAlertInfoRecord potentialAlert) {

        return false;
    }

    private boolean validateDateIsEligible(PdmAlertInfoRecord potentialAlert) {
        LocalDate markdownDate = potentialAlert.getExpirationDate().minusDays(potentialAlert.getDaysBeforeExpToMD());
        return markdownDate.isEqual(LocalDate.now()) || markdownDate.isBefore(LocalDate.now());
    }

}
