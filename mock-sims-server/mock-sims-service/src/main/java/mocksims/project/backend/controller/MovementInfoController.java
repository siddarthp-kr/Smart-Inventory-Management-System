package mocksims.project.backend.controller;

import mocksims.project.backend.api.domain.MovementInfoRequest;
import mocksims.project.backend.api.domain.MovementInfoResponse;
import mocksims.project.backend.domain.MockSimsConstants;
import mocksims.project.backend.exception.MockSimsCustomException;
import mocksims.project.backend.service.MovementInfoService;
import mocksims.project.backend.util.ValidationHelper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(value = "/api/boh")
@CrossOrigin(origins = "http://localhost:4200")
public class MovementInfoController {
    private static final Logger LOG = LoggerFactory.getLogger(MovementInfoController.class);
    private final MovementInfoService movementInfoService;


    public MovementInfoController(MovementInfoService movementInfoService){
        this.movementInfoService = movementInfoService;
    }

    @GetMapping (value = MockSimsConstants.GET_MOVEMENT_INFO_ENDPOINT)
    public ResponseEntity<MovementInfoResponse> getMovementInfo(
            @RequestParam String storeNumber,
            @RequestParam String divisionNumber,
            @RequestParam String upcNumber){
        MovementInfoResponse response = new MovementInfoResponse();

        if (ValidationHelper.validateStoreNumber(storeNumber) && ValidationHelper.validateDivisionNumber(divisionNumber) && ValidationHelper.validateUpcNumber(upcNumber)) {

            try {
                MovementInfoRequest request = new MovementInfoRequest(storeNumber, divisionNumber, upcNumber);
                response = movementInfoService.getMovementInfo(request);
                return ResponseEntity.ok(response);
            } catch (MockSimsCustomException error) {
                LOG.error("Failed to get movement info.", error);
                response.setResponseCode((error.getErrorCode()));
                response.setResponseMessage(error.getMessage());
                return ResponseEntity.status(error.getErrorCode()).body(response);
            }
        } else {
            response.setResponseMessage("Failed to get movement info due to invalid request parameters.");
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
        }
    }
}
