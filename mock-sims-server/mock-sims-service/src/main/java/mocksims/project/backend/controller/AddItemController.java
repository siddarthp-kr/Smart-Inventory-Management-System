package mocksims.project.backend.controller;

import mocksims.project.backend.api.domain.AddItemRequest;
import mocksims.project.backend.api.domain.AddItemResponse;
import mocksims.project.backend.domain.MockSimsConstants;
import mocksims.project.backend.exception.MockSimsCustomException;
import mocksims.project.backend.service.AddItemService;
import mocksims.project.backend.util.ValidationHelper;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@RestController
@RequestMapping(value = "/api/items")
@CrossOrigin(origins = "http://localhost:4200/")
public class AddItemController {
    private final AddItemService addItemService;

    private static final Logger LOG = LoggerFactory.getLogger(AddItemController.class);

    public AddItemController(AddItemService addItemService) {
        this.addItemService = addItemService;
    }

    /**
     * Post request for add new item
     * Validate the request parameters (UPC, division, store) before sending request
     * @param request for item contains, mark-down rules, and BOH info
     * @return the response entity for add item and Http status update
     */
    @PostMapping(value = MockSimsConstants.ADD_ITEM_ENDPOINT)
    public ResponseEntity <AddItemResponse> addItem(@RequestBody AddItemRequest request){
        AddItemResponse response = new AddItemResponse();
        //Validating fields before advancing to service layer
        if (ValidationHelper.validateUpcNumber(request.getUpcNumber()) && ValidationHelper.validateDivisionNumber(request.getDivisionNumber()) && ValidationHelper.validateStoreNumber(request.getStoreNumber())) {

            try{
                response = addItemService.addItem(request);
                return ResponseEntity.ok(response);
            } catch (MockSimsCustomException error){
                LOG.error("Add Item request failed", error);

                response.setResponseCode(error.getErrorCode());
                response.setResponseMessage(error.getMessage());
                return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
            }
        }
        //Return 400 for failed validation
        else {
            response.setResponseCode(400);
            response.setResponseMessage("Error: Invalid input");
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
        }
    }

}
