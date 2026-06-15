package mocksims.project.backend.controller;

import mocksims.project.backend.api.domain.AddItemRequest;
import mocksims.project.backend.api.domain.AddItemResponse;
import mocksims.project.backend.domain.MockSimsConstants;
import mocksims.project.backend.service.AddItemService;
import mocksims.project.backend.util.ValidationHelper;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(value = "/api/items")
@CrossOrigin(origins = "http://localhost:4200/")
public class AddItemController {
    private final AddItemService addItemService;

    public AddItemController(AddItemService addItemService) {
        this.addItemService = addItemService;
    }

    @PostMapping(value = MockSimsConstants.ADD_ITEM_ENDPOINT)
    public AddItemResponse addItem(@RequestBody AddItemRequest request){
        AddItemResponse response = new AddItemResponse();
        if (ValidationHelper.validateUpcNumber(request.getUpcNumber()) && ValidationHelper.validateDivisionNumber(request.getDivisionNumber()) && ValidationHelper.validateStoreNumber(request.getStoreNumber())) {
            response = addItemService.addItem(request);
        }
        else {
            response.setResponseCode(400);
            response.setResponseMessage("Error: Invalid input");
        }
        return response;
    }

}
