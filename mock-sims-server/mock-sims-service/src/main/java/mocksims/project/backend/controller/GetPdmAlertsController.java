package mocksims.project.backend.controller;

import mocksims.project.backend.api.domain.GetAlertCountResponse;
import mocksims.project.backend.api.domain.GetPdmAlertsResponse;
import mocksims.project.backend.exception.MockSimsCustomException;
import mocksims.project.backend.service.GetPdmAlertsService;
import mocksims.project.backend.util.ValidationHelper;
import org.apache.coyote.Response;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import mocksims.project.backend.domain.MockSimsConstants;

import java.time.LocalDate;

@RestController
@RequestMapping(value = "/api/pdm")
@CrossOrigin(origins = "http://localhost:4200")
public class GetPdmAlertsController {

    private static final Logger LOG = LoggerFactory.getLogger(GetPdmAlertsController.class);

    private final GetPdmAlertsService getPdmAlertsService;

    public GetPdmAlertsController(GetPdmAlertsService getPdmAlertsService){
        this.getPdmAlertsService = getPdmAlertsService;
    }

    @GetMapping(value = MockSimsConstants.GET_PDM_ALERTS_ENDPOINT)
    public ResponseEntity<GetPdmAlertsResponse> getPdmAlerts(@RequestParam String storeNumber, @RequestParam String divisionNumber){
        GetPdmAlertsResponse getPdmAlertsResponse = new GetPdmAlertsResponse();

        if(ValidationHelper.validateDivisionNumber(divisionNumber) && ValidationHelper.validateStoreNumber(storeNumber)){
            try {
                getPdmAlertsResponse.setPdmAlerts(getPdmAlertsService.getPdmAlerts(storeNumber, divisionNumber));
                getPdmAlertsResponse.setResponseMessage("Successfully retrieved " + getPdmAlertsResponse.getPdmAlerts().size() + " PDM alerts.");
                LOG.info("Successfully retrieved PDM alerts for division {} store {}.", divisionNumber, storeNumber);
            } catch (MockSimsCustomException e){
                LOG.error("Failed to get PDM alerts", e);
                getPdmAlertsResponse.setResponseMessage("Failed to retrieve PDM alerts.");
                return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(getPdmAlertsResponse);
            }
        } else {
            LOG.error("Failed to get PDM alerts. Invalid request parameters store {}, division {}.", storeNumber, divisionNumber);
            getPdmAlertsResponse.setResponseMessage("Failed to retrieve PDM alerts: invalid store or division number.");
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(getPdmAlertsResponse);
        }

        return ResponseEntity.ok(getPdmAlertsResponse);
    }

    @GetMapping(value = MockSimsConstants.GET_ALERT_COUNT_ENDPOINT)
    public ResponseEntity<GetAlertCountResponse> getAlertCount (@RequestParam String storeNumber, @RequestParam String divisionNumber){
        GetAlertCountResponse getAlertCountResponse = new GetAlertCountResponse();

        if(ValidationHelper.validateStoreNumber(storeNumber) && ValidationHelper.validateDivisionNumber(divisionNumber)){
            try {
                getAlertCountResponse.setAlertCount(getPdmAlertsService.getPdmAlertCount(storeNumber, divisionNumber));
                return ResponseEntity.ok(getAlertCountResponse);
            } catch (MockSimsCustomException e){
                LOG.error("Failed to get alert count for store {} in division {}.", storeNumber, divisionNumber);
                return ResponseEntity.status(e.getErrorCode()).body(getAlertCountResponse);
            }
        } else {
            LOG.error("Failed to get alert count for store {} in division {}. Invalid request parameters", storeNumber, divisionNumber);
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(getAlertCountResponse);
        }
        
    }

}
