package mocksims.project.backend.controller;

import mocksims.project.backend.api.domain.PushBackExpirationRequest;
import mocksims.project.backend.api.domain.PushBackExpirationResponse;
import mocksims.project.backend.domain.MockSimsConstants;
import mocksims.project.backend.exception.MockSimsCustomException;
import mocksims.project.backend.service.PushBackExpirationService;
import org.apache.coyote.Response;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.dao.DataAccessException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;

@RestController
@CrossOrigin(origins = "http://localhost:4200")
@RequestMapping("/api/pdm")
public class PushBackExpirationController {

    private static final Logger LOG = LoggerFactory.getLogger(PushBackExpirationController.class);

    private final PushBackExpirationService pushBackExpirationService;

    public PushBackExpirationController (PushBackExpirationService pushBackExpirationService){
        this.pushBackExpirationService = pushBackExpirationService;
    }

    @PostMapping(value = MockSimsConstants.PUSH_BACK_EXPIRATION_ENDPOINT)
    public PushBackExpirationResponse pushBackExpirationDate(@RequestBody PushBackExpirationRequest pushBackExpirationRequest){
        PushBackExpirationResponse pushBackExpirationResponse = new PushBackExpirationResponse();
        if(pushBackExpirationRequest.getNewExpirationDate().isBefore(LocalDate.now())){
            pushBackExpirationResponse.setResponseMessage("Failed to push back expiration date for alert " + pushBackExpirationRequest.getAlertId() +". Invalid request parameters.");
            pushBackExpirationResponse.setResponseCode(400);
            LOG.error("Failed to push back expiration date for alert {}. Invalid request parameters.", pushBackExpirationRequest.getAlertId());
            return pushBackExpirationResponse;
        } else {
            try {
                pushBackExpirationService.pushBackExpirationDate(pushBackExpirationRequest);
                pushBackExpirationResponse.setResponseMessage("Successfully expiration date for alert " + pushBackExpirationRequest.getAlertId() +".");
                pushBackExpirationResponse.setResponseCode(200);
                return pushBackExpirationResponse;
            } catch (MockSimsCustomException e){
                pushBackExpirationResponse.setResponseMessage("Failed to push back expiration date for alert " + pushBackExpirationRequest.getAlertId() +" due to internal server error. Details: " + e.getMessage());
                pushBackExpirationResponse.setResponseCode(e.getErrorCode());
                return pushBackExpirationResponse;
            }
        }


    }
}
