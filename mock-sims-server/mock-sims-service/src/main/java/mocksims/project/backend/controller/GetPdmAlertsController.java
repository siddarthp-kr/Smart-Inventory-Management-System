package mocksims.project.backend.controller;

import mocksims.project.backend.api.domain.GetPdmAlertsResponse;
import mocksims.project.backend.exception.MockSimsCustomException;
import mocksims.project.backend.service.GetPdmAlertsService;
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

        try {
            getPdmAlertsResponse.setPdmAlerts(getPdmAlertsService.getPdmAlerts(storeNumber, divisionNumber));
            getPdmAlertsResponse.setResponseMessage("Successfully retrieved " + getPdmAlertsResponse.getPdmAlerts().size() + " PDM alerts.");
            LOG.info("Successfully retrieved PDM alerts for division {} store {}.", divisionNumber, storeNumber);
        } catch (MockSimsCustomException e){
            LOG.error("Failed to get PDM alerts", e);
            getPdmAlertsResponse.setResponseMessage("Failed to retrieve PDM alerts.");
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(getPdmAlertsResponse);
        }

        return ResponseEntity.ok(getPdmAlertsResponse);
    }

}
