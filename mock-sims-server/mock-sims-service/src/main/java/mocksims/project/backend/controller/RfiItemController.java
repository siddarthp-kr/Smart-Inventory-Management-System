package mocksims.project.backend.controller;

import mocksims.project.backend.api.domain.RfiItemRequest;
import mocksims.project.backend.api.domain.RfiItemResponse;
import mocksims.project.backend.domain.MockSimsConstants;
import mocksims.project.backend.exception.MockSimsCustomException;
import mocksims.project.backend.service.RfiItemService;
import mocksims.project.backend.util.ValidationHelper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(value = "/api/pdm")
@CrossOrigin(origins = "http://localhost:4200")
public class RfiItemController {

    private static final Logger LOG = LoggerFactory.getLogger(RfiItemController.class);

    private final RfiItemService rfiItemService;

    public RfiItemController (RfiItemService rfiItemService){
        this.rfiItemService = rfiItemService;
    }

    @PostMapping(value = MockSimsConstants.RFI_ITEM_ENDPOINT)
    public ResponseEntity<RfiItemResponse> rfiItem(@RequestBody RfiItemRequest rfiItemRequest){
        RfiItemResponse rfiItemResponse = new RfiItemResponse();

        if(rfiItemRequest.getQuantity() > 0
                && ValidationHelper.validateUserEuid(rfiItemRequest.getUserEuid())
                && ValidationHelper.validateStoreNumber(rfiItemRequest.getStoreNumber())
                && ValidationHelper.validateDivisionNumber(rfiItemRequest.getDivisionNumber())
                && ValidationHelper.validateUpcNumber(rfiItemRequest.getUpcNumber()))
        {
            try {
                rfiItemService.rfiItem(rfiItemRequest);
                rfiItemResponse.setResponseMessage("Successfully removed item " + rfiItemRequest.getUpcNumber() + " from inventory");
                return ResponseEntity.ok(rfiItemResponse);
            } catch (MockSimsCustomException e){
                LOG.error("Failed to remove item from inventory.", e);
                rfiItemResponse.setResponseMessage("Failed to markdown item due to internal server error.");
                return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(rfiItemResponse);
            }
        } else {
            LOG.error("Failed to remove item from inventory. Invalid request parameters.");
            rfiItemResponse.setResponseMessage("Failed to remove item from inventory: invalid request parameters");
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(rfiItemResponse);
        }
    }
}
