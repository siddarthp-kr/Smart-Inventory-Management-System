package mocksims.project.backend.controller;

import mocksims.project.backend.api.domain.BohRequest;
import mocksims.project.backend.api.domain.BohResponse;
import mocksims.project.backend.domain.MockSimsConstants;
import mocksims.project.backend.exception.MockSimsCustomException;
import mocksims.project.backend.service.BohService;
import mocksims.project.backend.util.ValidationHelper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(value = "/api/boh")
@CrossOrigin(origins = "http://localhost:4200")
public class BohController {
    private static final Logger LOG = LoggerFactory.getLogger(BohController.class);

    private final BohService bohService;

    public BohController(BohService bohService){
        this.bohService = bohService;
    }

    /**
     * GET request to retrieve BOH info for specific store and vision
     * @param storeNumber filter BOH results
     * @param divisionNumber filter BOH results
     * @return response entity of BOH product details and status
     */
    @GetMapping(value = MockSimsConstants.GET_BOH_ENDPOINT)
    public ResponseEntity<BohResponse> getBohInfo(
            @RequestParam String storeNumber,
            @RequestParam String divisionNumber) {

        BohResponse response = new BohResponse();
        if (ValidationHelper.validateStoreNumber(storeNumber) && ValidationHelper.validateDivisionNumber(divisionNumber)) {
            try {
                BohRequest request = BohRequest.builder()
                        .storeNumber(storeNumber)
                        .divisionNumber(divisionNumber)
                        .build();
                response = bohService.getBohInfo(request);
                return ResponseEntity.ok(response);
            } catch (MockSimsCustomException error) {
                LOG.error("Failed to get BOH information.", error);
                return ResponseEntity.status(error.getErrorCode()).body(response);
            }
        }
        else {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
        }
    }
}
